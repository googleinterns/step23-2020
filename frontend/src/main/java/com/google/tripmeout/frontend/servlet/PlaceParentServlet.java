package com.google.tripmeout.frontend.servlet;

import com.google.common.flogger.FluentLogger;
import com.google.gson.Gson;
import com.google.maps.GeoApiContext;
import com.google.maps.PlaceDetailsRequest;
import com.google.maps.errors.InvalidRequestException;
import com.google.maps.model.PlaceDetails;
import com.google.tripmeout.frontend.PlaceVisitModel;
import com.google.tripmeout.frontend.storage.PlaceVisitStorage;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PlaceParentServlet extends HttpServlet {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private final PlaceVisitStorage placeStorage;
  private final Gson gson;

  private static final Pattern TRIP_NAME_PATTERN = Pattern.compile(".*/trips/([^/]+)/placeVisits");

  private final GeoApiContext CONTEXT =
      new GeoApiContext.Builder().apiKey("AIzaSyBbXCXC2uWv3baNmirLtqUYbFsFwCXqLV8").build();

  public PlaceParentServlet(PlaceVisitStorage placeStorage, Gson gson) {
    this.placeStorage = placeStorage;
    this.gson = gson;
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    try {
      Optional<PlaceVisitModel> place =
          ServletUtil.extractFromRequestBody(request, gson, PlaceVisitModel.class);

      String tripId = ServletUtil.matchUriOrThrowError(request, TRIP_NAME_PATTERN).group(1);
      logger.atInfo().log("finished matching");

      if (!place.isPresent()) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return;
      }

      PlaceDetailsRequest placeRequest =
          new PlaceDetailsRequest(CONTEXT)
              .placeId(place.get().placeId())
              .fields(PlaceDetailsRequest.FieldMask.GEOMETRY_LOCATION,
                  PlaceDetailsRequest.FieldMask.NAME);

      PlaceDetails details;
      details = placeRequest.await();

      PlaceVisitModel newPlace = PlaceVisitModel.builder()
                                     .setTripId(tripId)
                                     .setPlaceId(place.get().placeId())
                                     .setUserMark(PlaceVisitModel.UserMark.YES)
                                     .setName(details.name)
                                     .setLatitude(details.geometry.location.lat)
                                     .setLongitude(details.geometry.location.lng)
                                     .build();

      placeStorage.updateUserMarkOrAddPlaceVisit(newPlace, PlaceVisitModel.UserMark.YES);
      PlaceVisitModel newOrUpdatedPlace =
          placeStorage.getPlaceVisit(tripId, place.get().placeId()).get();

      response.setContentType("application/json");
      response.setStatus(HttpServletResponse.SC_OK);
      PrintWriter writer = response.getWriter();
      writer.println(gson.toJson(newOrUpdatedPlace));
      writer.flush();
      writer.close();

    } catch (IllegalArgumentException e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

    } catch (InvalidRequestException e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

    } catch (Exception e) {
      logger.atWarning().log(e.getMessage());
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    try {
      String tripId = ServletUtil.matchUriOrThrowError(request, TRIP_NAME_PATTERN).group(1);
      List<PlaceVisitModel> nearbyPlaces = placeStorage.getTripPlaceVisits(tripId);

      response.setContentType("application/json");
      response.setStatus(HttpServletResponse.SC_OK);
      PrintWriter writer = response.getWriter();
      writer.println(gson.toJson(nearbyPlaces));
      writer.flush();
      writer.close();

    } catch (IllegalArgumentException e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

    } catch (Exception e) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }
}
