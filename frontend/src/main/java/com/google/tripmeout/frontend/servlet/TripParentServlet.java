package com.google.tripmeout.frontend.servlet;

import com.google.appengine.repackaged.com.google.api.client.util.Strings;
import com.google.common.flogger.FluentLogger;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.tripmeout.frontend.TripModel;
import com.google.tripmeout.frontend.error.TripAlreadyExistsException;
import com.google.tripmeout.frontend.storage.TripStorage;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.inject.Inject;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TripParentServlet extends HttpServlet {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private static final String TRIP_PARENT_PARAM_KEY = "name";
  private static final String LONG_PARAM_KEY = "locationLong";
  private static final String LAT_PARAM_KEY = "locationLat";
  private static final String USER_ID_PARAM_KEY = "userId";
  private final Gson gson;
  private final TripStorage storage;

  @Inject
  public TripParentServlet(TripStorage storage, Gson gson) {
    this.gson = gson;
    this.storage = storage;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String userId = getUserId();
    response.setContentType("application/json");
    response.getWriter().print(gson.toJson(storage.getAllUserTrips(userID)));
  }
  private String getUserId(){
      return "fakeUser";
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    logger.atInfo().log("received doPost");
      String userId = getUserId();
      Optional<TripModel> requestTrip = extractFromRequestBody(request, gson, TripModel.class);
     
      if (requestTrip.isPresent()) {
        try {
          storage.addTrip(requestTrip.get());
          response.setContentType("application/json");
          response.getWriter().print(gson.toJson(requestTrip.get()));
        } catch (TripAlreadyExistsException e) {
          response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

      } else {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      }

    
  }

private static TripModel resolveDefault(){
    return TripModel.builder().setId(UUID.randomUUID().toString()).build();
}

  //TODO: use servletutil onced pushed
  private static <T> Optional<T> extractFromRequestBody(
      HttpServletRequest request, Gson gson, Class<T> clazz) throws IOException {
    try (Reader reader = request.getReader()) {
      return Optional.ofNullable(gson.fromJson(reader, clazz));
    } catch (JsonParseException e) {
      logger.atWarning().withCause(e).log(
          "received exception while attempting to parse json from reader!");
      return Optional.empty();
    }
  }
}
