package com.google.tripmeout.frontend.servlet;

import com.google.common.flogger.FluentLogger;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.maps.GeoApiContext;
import com.google.maps.PlaceDetailsRequest;
import com.google.maps.errors.InvalidRequestException;
import com.google.maps.model.PlaceDetails;
import com.google.tripmeout.frontend.PlaceVisitModel;
import com.google.tripmeout.frontend.error.TripMeOutException;
import com.google.tripmeout.frontend.storage.PlaceVisitStorage;
import com.google.tripmeout.frontend.error.TripMeOutException;
import com.google.gson.JsonParseException;
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
  private static final Pattern TRIP_NAME_PATTERN = Pattern.compile(".*/trips/([^/]+)/placeVisits");

  private final PlaceVisitStorage placeStorage;
  private final Gson gson;
  //private final PlaceService placeService;

  public PlaceParentServlet(PlaceVisitStorage placeStorage, Gson gson) {
    this.placeStorage = placeStorage;
    this.gson = gson;
    //this.placeService = placeService;
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    try {
      PlaceVisitModel place = ServletUtil.extractFromRequestBody(request.getReader(), gson, PlaceVisitModel.class);
      //validatePlaceId(place.placesApiPlaceId());

      String tripId = ServletUtil.matchUriOrThrowError(request, TRIP_NAME_PATTERN).group(1);
      logger.atInfo().log("finished matching");

      PlaceVisitModel newPlace = PlaceVisitModel.builder()
                                     .setTripId(tripId)
                                     .setPlacesApiPlaceId(place.placesApiPlaceId())
                                     .setUserMark(PlaceVisitModel.UserMark.YES)
                                     .build();

      placeStorage.updateUserMarkOrAddPlaceVisit(newPlace, PlaceVisitModel.UserMark.YES);

      response.setStatus(HttpServletResponse.SC_OK);

    } catch (TripMeOutException e) {
      response.setStatus(e.restStatusCode());
    } catch (JsonParseException e) {
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
    PrintWriter writer = response.getWriter();
    try {
      String tripId = ServletUtil.matchUriOrThrowError(request, TRIP_NAME_PATTERN).group(1);
      List<PlaceVisitModel> nearbyPlaces = placeStorage.getTripPlaceVisits(tripId);
      response.setContentType("application/json");
      writer.println(gson.toJson(nearbyPlaces));
      response.setStatus(HttpServletResponse.SC_OK);
    } catch (TripMeOutException e) {
      response.setStatus(e.restStatusCode());
    } catch (Exception e) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    } finally {
      writer.close();
    }
  }
}
