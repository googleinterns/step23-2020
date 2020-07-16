package com.google.tripmeout.frontend;

import com.google.gson.Gson;
import com.google.tripmeout.frontend.serialization.GsonTripModelTypeAdapter;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.tripmeout.frontend.error.TripMeOutException;
import com.google.tripmeout.frontend.error.TripNotFoundException;
import com.google.tripmeout.frontend.storage.TripStorage;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Singleton
public final class TripServlet extends HttpServlet {
  // The Trip get and delete servlet
  private static final long serialVersionUID = 1L;
  private final TripStorage storage;
  private static final String APPLICATION_JSON_CONTENT_TYPE = "application/json";
  private Gson gson= new GsonBuilder()
                    .registerTypeHierarchyAdapter(TripModel.class, new GsonTripModelTypeAdapter())
                    .create();

  @Inject
  public TripServlet(final TripStorage storage) {
    this.storage = storage;
  }

  @Override
  public void doGet(final HttpServletRequest request, final HttpServletResponse response)
      throws IOException {
    final String path = request.getRequestURI();
    final String tripID = path; // TODO @afeenster: Should parse out URI into just the trip ID
    try {
      response.setContentType(APPLICATION_JSON_CONTENT_TYPE + ";");
      response.getWriter().print(gson.toJson(storage.getTrip(tripID)));
      response.setStatus(HttpServletResponse.SC_OK);
    } catch (TripNotFoundException e) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
  }

  @Override
  public void doDelete(final HttpServletRequest request, final HttpServletResponse response)
      throws IOException {
    final String path = request.getRequestURI();
    final String tripID = path; // TODO @afeenster: Should parse out URI into just the trip ID
    try {
      storage.removeTrip(tripID);
      response.setStatus(HttpServletResponse.SC_OK);
    } catch (TripMeOutException e) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
  }
}
