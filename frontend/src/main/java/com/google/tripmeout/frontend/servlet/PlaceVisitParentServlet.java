package com.google.tripmeout.frontend.servlet;

import com.google.common.flogger.FluentLogger;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.tripmeout.frontend.PlaceVisitModel;
import com.google.tripmeout.frontend.error.TripMeOutException;
import com.google.tripmeout.frontend.places.PlaceService;
import com.google.tripmeout.frontend.storage.PlaceVisitStorage;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Singleton
public class PlaceVisitParentServlet extends HttpServlet {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private static final Pattern TRIP_NAME_PATTERN = Pattern.compile(".*/trips/([^/]+)/placeVisits");

  private final PlaceVisitStorage placeStorage;
  private final Gson gson;
  private final PlaceService placeService;

  @Inject
  public PlaceVisitParentServlet(
      PlaceVisitStorage placeStorage, Gson gson, PlaceService placeService) {
    this.placeStorage = placeStorage;
    this.gson = gson;
    this.placeService = placeService;
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    logger.atInfo().log("%s serving POST %s", PlaceVisitParentServlet.class.getSimpleName(),
        request.getRequestURI());
    try (PrintWriter writer = response.getWriter()) {
      PlaceVisitModel place =
          ServletUtil.extractFromRequestBody(request.getReader(), gson, PlaceVisitModel.class);

      placeService.validatePlaceId(place.placesApiPlaceId());

      String tripId = ServletUtil.matchUriOrThrowError(request, TRIP_NAME_PATTERN).group(1);

      PlaceVisitModel newPlace =
          place.toBuilder().setId(UUID.randomUUID().toString()).setTripId(tripId).build();

      placeStorage.addPlaceVisit(newPlace);

      response.setContentType("applciation/json");
      writer.println(gson.toJson(newPlace));
      response.setStatus(HttpServletResponse.SC_CREATED);

    } catch (TripMeOutException e) {
      logger.atWarning().withCause(e).log("business logic exception");
      response.setStatus(e.restStatusCode());
    } catch (JsonParseException e) {
      logger.atWarning().withCause(e).log("business logic exception");
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    } catch (Exception e) {
      logger.atWarning().withCause(e).log("internal error");
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    logger.atInfo().log("%s serving GET %s", PlaceVisitParentServlet.class.getSimpleName(),
        request.getRequestURI());
    try (PrintWriter writer = response.getWriter()) {
      String tripId = ServletUtil.matchUriOrThrowError(request, TRIP_NAME_PATTERN).group(1);
      List<PlaceVisitModel> nearbyPlaces = placeStorage.getTripPlaceVisits(tripId);
      response.setContentType("application/json");
      writer.println(gson.toJson(nearbyPlaces));
      response.setStatus(HttpServletResponse.SC_OK);
    } catch (TripMeOutException e) {
      logger.atWarning().withCause(e).log("business logic exception");
      response.setStatus(e.restStatusCode());
    } catch (Exception e) {
      logger.atWarning().withCause(e).log("internal error");
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }
}
