package com.google.tripmeout.frontend.servlet;

import com.google.common.flogger.FluentLogger;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.tripmeout.frontend.TripModel;
import com.google.tripmeout.frontend.error.TripNotFoundException;
import com.google.tripmeout.frontend.storage.TripStorage;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Singleton
public final class TripServlet extends HttpServlet {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  private static final long serialVersionUID = 1L;
  private static final String APPLICATION_JSON_CONTENT_TYPE = "application/json";

  private final Gson gson;
  private final TripStorage storage;

  @Inject
  public TripServlet(TripStorage storage, Gson gson) {
    this.storage = storage;
    this.gson = gson;
  }

  @Override
  public void doGet(final HttpServletRequest request, final HttpServletResponse response)
      throws IOException {
    logger.atInfo().log(
        "%s serving GET %s", TripServlet.class.getSimpleName(), request.getRequestURI());
    try {
      final String tripId = TripName.fromRequestUri(request.getRequestURI()).id();
      TripModel trip = storage.getTrip(tripId);
      response.setContentType(APPLICATION_JSON_CONTENT_TYPE);
      response.getWriter().print(gson.toJson(trip));
      response.getWriter().flush();
    } catch (TripNotFoundException e) {
      logger.atWarning().withCause(e).log("not found");
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (IllegalArgumentException e) {
      logger.atWarning().withCause(e).log("bad request");
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    } catch (Exception e) {
      logger.atWarning().withCause(e).log("internal error");
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public void doDelete(final HttpServletRequest request, final HttpServletResponse response)
      throws IOException {
    logger.atInfo().log(
        "%s serving DELETE %s", TripServlet.class.getSimpleName(), request.getRequestURI());
    try {
      final TripName tripName = TripName.fromRequestUri(request.getRequestURI());
      storage.removeTrip(tripName.id());
      response.setStatus(HttpServletResponse.SC_OK);
    } catch (TripNotFoundException e) {
      logger.atWarning().withCause(e).log("not found");
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (IllegalArgumentException e) {
      logger.atWarning().withCause(e).log("bad request");
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    } catch (Exception e) {
      logger.atWarning().withCause(e).log("internal error");
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }
}
