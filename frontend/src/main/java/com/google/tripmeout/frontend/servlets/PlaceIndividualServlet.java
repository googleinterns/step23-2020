package com.google.tripmeout.frontend.servlets;

import com.google.gson.Gson;
import com.google.maps.GeoApiContext;
import com.google.maps.PlaceDetailsRequest;
import com.google.maps.errors.InvalidRequestException;
import com.google.maps.model.PlaceDetails;
import com.google.tripmeout.frontend.PlaceVisitModel;
import com.google.tripmeout.frontend.error.PlaceVisitNotFoundException;
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
    PrintWriter writer = response.getWriter();
    try {
      Matcher matcher = ServletUtil.matchUriOrThrowError(request, URI_NAME_PATTERN);

      String tripId = matcher.group(1);
      String id = matcher.group(2);

      PlaceVisitModel place = ServletUtil.extractFromRequestBody(request.getReader(), gson, PlaceVisitModel.class);

      PlaceVisitModel.UserMark status = place.userMark();

      PlaceVisitModel newPlace = PlaceVisitModel.builder()
                                     .setTripId(tripId)
                                     .setId(id)
                                     .build();

      placeStorage.updateUserMarkOrAddPlaceVisit(newPlace, status);
      response.setStatus(HttpServletResponse.SC_OK);

    } catch (IllegalArgumentException e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

    } catch (InvalidRequestException e) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);

    } catch (Exception e) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    } finally {
      writer.close();
    }
  }

  @Override
  public void doDelete(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    try {
      Matcher matcher = ServletUtil.matchUriOrThrowError(request, URI_NAME_PATTERN);
      String tripId = matcher.group(1);
      String placeId = matcher.group(2);

      placeStorage.removePlaceVisit(tripId, placeId);
      response.setStatus(HttpServletResponse.SC_OK);

    } catch (IllegalArgumentException e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

    } catch (PlaceVisitNotFoundException e) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);

    } catch (Exception e) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    PrintWriter writer = response.getWriter();
    try {
      Matcher matcher = ServletUtil.matchUriOrThrowError(request, URI_NAME_PATTERN);
      String tripId = matcher.group(1);
      String placeId = matcher.group(2);

      Optional<PlaceVisitModel> optionalPlace = placeStorage.getPlaceVisit(tripId, placeId);

      String userMark;

      if (optionalPlace.isPresent()) {
        userMark = optionalPlace.get().userMark().toString();
      } else {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return;
      }

      response.setStatus(HttpServletResponse.SC_OK);
      response.setContentType("application/json");
      writer.println(gson.toJson(optionalPlace.get()));

    } catch (IllegalArgumentException e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

    } catch (InvalidRequestException e) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);

    } catch (Exception e) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    } finally {
      writer.close();
    }
  }
}
