package com.google.tripmeout.frontend.servlets;

import com.google.gson.Gson;
import com.google.maps.GeoApiContext;
import com.google.maps.PlaceDetailsRequest;
import com.google.maps.errors.InvalidRequestException;
import com.google.maps.model.PlaceDetails;
import com.google.tripmeout.frontend.PlaceVisitModel;
import com.google.tripmeout.frontend.error.TripMeOutException;
import com.google.tripmeout.frontend.storage.PlaceVisitStorage;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PlaceIndividualServlet extends HttpServlet {
  private final PlaceVisitStorage placeStorage;
  private final Gson gson;

  private static final Pattern URI_NAME_PATTERN =
      Pattern.compile(".*/trips/([^/]+)/placeVisits/([^/]+)");

  private final GeoApiContext CONTEXT =
      new GeoApiContext.Builder().apiKey("AIzaSyBbXCXC2uWv3baNmirLtqUYbFsFwCXqLV8").build();

  public PlaceIndividualServlet(PlaceVisitStorage placeStorage, Gson gson) {
    this.placeStorage = placeStorage;
    this.gson = gson;
  }

  @Override
  public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
    try {
      Matcher matcher = ServletUtil.parseUri(request, URI_NAME_PATTERN);

      String tripId = matcher.group(1);
      String placeId = matcher.group(2);

      Optional<PlaceVisitModel> place =
          ServletUtil.extractFromRequestBody(request, gson, PlaceVisitModel.class);

      if (!place.isPresent()) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return;
      }

      PlaceVisitModel.UserMark status = place.get().userMark();

      PlaceDetailsRequest placeRequest = new PlaceDetailsRequest(CONTEXT).placeId(placeId).fields(
          PlaceDetailsRequest.FieldMask.NAME, PlaceDetailsRequest.FieldMask.GEOMETRY_LOCATION);
      PlaceDetails details = placeRequest.await();

      PlaceVisitModel newPlace = PlaceVisitModel.builder()
                                     .setTripId(tripId)
                                     .setPlaceId(placeId)
                                     .setName(details.name)
                                     .setLatitude(details.geometry.location.lat)
                                     .setLongitude(details.geometry.location.lng)
                                     .build();

      placeStorage.updateUserMarkOrAddPlaceVisit(newPlace, status);
      response.setStatus(HttpServletResponse.SC_OK);

    } catch (IllegalArgumentException e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

    } catch (InvalidRequestException e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

    } catch (Exception e) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public void doDelete(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    try {
      Matcher matcher = ServletUtil.parseUri(request, URI_NAME_PATTERN);
      String tripId = matcher.group(1);
      String placeId = matcher.group(2);

      placeStorage.removePlaceVisit(tripId, placeId);
      response.setStatus(HttpServletResponse.SC_OK);

    } catch (IllegalArgumentException e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

    } catch (TripMeOutException e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

    } catch (Exception e) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    try {
      Matcher matcher = ServletUtil.parseUri(request, URI_NAME_PATTERN);
      String tripId = matcher.group(1);
      String placeId = matcher.group(2);

      Optional<PlaceVisitModel> optionalPlace = placeStorage.getPlaceVisit(tripId, placeId);

      String userMark;

      if (optionalPlace.isPresent()) {
        userMark = optionalPlace.get().userMark().toString();
      } else {
        userMark = PlaceVisitModel.UserMark.UNKNOWN.toString();
      }

      PlaceDetailsRequest place = new PlaceDetailsRequest(CONTEXT).placeId(placeId).fields(
          PlaceDetailsRequest.FieldMask.NAME, PlaceDetailsRequest.FieldMask.BUSINESS_STATUS,
          PlaceDetailsRequest.FieldMask.FORMATTED_ADDRESS,
          PlaceDetailsRequest.FieldMask.FORMATTED_PHONE_NUMBER,
          PlaceDetailsRequest.FieldMask.GEOMETRY_LOCATION,
          PlaceDetailsRequest.FieldMask.OPENING_HOURS, PlaceDetailsRequest.FieldMask.PHOTOS,
          PlaceDetailsRequest.FieldMask.PRICE_LEVEL, PlaceDetailsRequest.FieldMask.RATING,
          PlaceDetailsRequest.FieldMask.REVIEW, PlaceDetailsRequest.FieldMask.TYPES,
          PlaceDetailsRequest.FieldMask.WEBSITE);
      PlaceDetails details = place.await();

      String[] newHtmlAttributions = new String[details.htmlAttributions.length + 1];
      newHtmlAttributions[0] = userMark;
      for (int i = 1; i < details.htmlAttributions.length + 1; i++) {
        newHtmlAttributions[i] = details.htmlAttributions[i - 1];
      }
      details.htmlAttributions = newHtmlAttributions;
      response.setStatus(200);
      response.setContentType("application/json");
      PrintWriter writer = response.getWriter();
      writer.println(gson.toJson(details));
      writer.flush();
      writer.close();

    } catch (IllegalArgumentException e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

    } catch (InvalidRequestException e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

    } catch (Exception e) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }
}
