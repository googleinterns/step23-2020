package com.google.tripmeout.frontend.servlet;

import com.google.common.flogger.FluentLogger;
import com.google.gson.Gson;
import com.google.tripmeout.frontend.PlaceVisitModel;
import com.google.tripmeout.frontend.error.TripMeOutException;
import com.google.tripmeout.frontend.places.PlaceService;
import com.google.tripmeout.frontend.storage.PlaceVisitStorage;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Singleton
public class PlaceVisitIndividualServlet extends HttpServlet {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private static final Pattern URI_NAME_PATTERN =
      Pattern.compile(".*/trips/([^/]+)/placeVisits/([^/]+)");

  private final PlaceVisitStorage placeStorage;
  private final Gson gson;
  private final PlaceService placeService;

  @Inject
  public PlaceVisitIndividualServlet(
      PlaceVisitStorage placeStorage, Gson gson, PlaceService placeService) {
    this.placeStorage = placeStorage;
    this.gson = gson;
    this.placeService = placeService;
  }

  @Override
  public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
    logger.atInfo().log("%s serving PUT %s", PlaceVisitIndividualServlet.class.getSimpleName(),
        request.getRequestURI());
    try (PrintWriter writer = response.getWriter()) {
      Matcher matcher = ServletUtil.matchUriOrThrowError(request, URI_NAME_PATTERN);

      String tripId = matcher.group(1);
      String id = matcher.group(2);

      PlaceVisitModel place =
          ServletUtil.extractFromRequestBody(request.getReader(), gson, PlaceVisitModel.class);

      placeService.validatePlaceId(place.placesApiPlaceId());

      PlaceVisitModel.UserMark status = place.userMark();

      PlaceVisitModel newPlace = PlaceVisitModel.builder()
                                     .setTripId(tripId)
                                     .setId(id)
                                     .setPlacesApiPlaceId(place.placesApiPlaceId())
                                     .build();

      PlaceVisitModel updatedPlace = placeStorage.updateUserMarkOrAddPlaceVisit(newPlace, status);
      response.setContentType("application/json");
      writer.println(gson.toJson(updatedPlace));
      response.setStatus(HttpServletResponse.SC_OK);

    } catch (TripMeOutException e) {
      logger.atWarning().withCause(e).log("business logic exception");
      response.setStatus(e.restStatusCode());
    } catch (Exception e) {
      logger.atWarning().withCause(e).log("internal error");
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public void doDelete(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    logger.atInfo().log("%s serving DELETE %s", PlaceVisitIndividualServlet.class.getSimpleName(),
        request.getRequestURI());
    try {
      Matcher matcher = ServletUtil.matchUriOrThrowError(request, URI_NAME_PATTERN);
      String tripId = matcher.group(1);
      String placeId = matcher.group(2);

      placeStorage.removePlaceVisit(tripId, placeId);
      response.setStatus(HttpServletResponse.SC_OK);

    } catch (TripMeOutException e) {
      logger.atWarning().withCause(e).log("business logic exception");
      response.setStatus(e.restStatusCode());
    } catch (Exception e) {
      logger.atWarning().withCause(e).log("internal error");
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    logger.atInfo().log("%s serving GET %s", PlaceVisitIndividualServlet.class.getSimpleName(),
        request.getRequestURI());
    try (PrintWriter writer = response.getWriter()) {
      Matcher matcher = ServletUtil.matchUriOrThrowError(request, URI_NAME_PATTERN);
      String tripId = matcher.group(1);
      String placeId = matcher.group(2);

      Optional<PlaceVisitModel> optionalPlace = placeStorage.getPlaceVisit(tripId, placeId);

      if (!optionalPlace.isPresent()) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return;
      }
      response.setContentType("application/json");
      writer.println(gson.toJson(optionalPlace.get()));
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
