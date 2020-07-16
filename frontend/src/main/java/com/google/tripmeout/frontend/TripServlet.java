package com.google.tripmeout.frontend;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.tripmeout.frontend.error.TripMeOutException;
import com.google.tripmeout.frontend.error.TripNotFoundException;
import com.google.tripmeout.frontend.serialization.GsonTripModelTypeAdapter;
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
  private final static TripModel PARIS = TripModel.builder()
                                             .setId("CDG")
                                             .setName("Paris, France")
                                             .setUserId("12345")
                                             .setLocationLat(48.3288)
                                             .setLocationLong(34.0)
                                             .build();

  private final Gson gson;

  @Inject
  public TripServlet(final InMemoryTripModelStorage storage, Gson gson) {
    this.storage = storage;
    this.gson = gson;
  }

  @Override
  public void doGet(final HttpServletRequest request, final HttpServletResponse response)
      throws IOException {
    final String path = request.getRequestURI();
    final String tripID = path; // TODO: Should parse out URI into just the trip ID
    try {
        

      TripModel theTrip = storage.getTrip(tripID);
      response.setContentType(APPLICATION_JSON_CONTENT_TYPE + ";");
      response.getWriter().print(gson.toJson(theTrip));
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
      response.setStatus(HttpServletResponse.SC_ACCEPTED);
    } catch (TripMeOutException e) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
  }
}
