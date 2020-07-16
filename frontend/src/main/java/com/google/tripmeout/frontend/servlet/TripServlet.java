package com.google.tripmeout.frontend.servlet;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.tripmeout.frontend.TripModel;
import com.google.tripmeout.frontend.error.TripMeOutException;
import com.google.tripmeout.frontend.error.TripNotFoundException;
import com.google.tripmeout.frontend.storage.InMemoryTripModelStorage;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Singleton
public final class TripServlet extends HttpServlet {
  // The Trip get and delete servlet
  private static final long serialVersionUID = 1L;
  private final InMemoryTripModelStorage storage;
  private static final String APPLICATION_JSON_CONTENT_TYPE = "application/json";
  private final Gson gson;

  @Inject
  public TripServlet(final InMemoryTripModelStorage storage, Gson gson) {
    this.storage = storage;
    this.gson = gson;
  }

  @Override
  public void doGet(final HttpServletRequest request, final HttpServletResponse response)
      throws IOException {
    try {
      final String tripId = TripName.fromRequestUri(request.getRequestURI()).id();
      TripModel theTrip = storage.getTrip(tripId);
      response.setContentType(APPLICATION_JSON_CONTENT_TYPE + ";");
      response.getWriter().print(gson.toJson(theTrip));
      response.getWriter().flush();
    } catch (TripNotFoundException e) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (Exception e) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public void doDelete(final HttpServletRequest request, final HttpServletResponse response)
      throws IOException {
    try {
      final String tripId = TripName.fromRequestUri(request.getRequestURI()).id();
      storage.removeTrip(tripId);
      response.setStatus(HttpServletResponse.SC_ACCEPTED);
    } catch (TripMeOutException e) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (Exception e) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }
}
