package com.google.tripmeout.frontend.servlet;

import com.google.common.flogger.FluentLogger;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.inject.Singleton;
import com.google.tripmeout.frontend.TripModel;
import com.google.tripmeout.frontend.error.TripMeOutException;
import com.google.tripmeout.frontend.servlets.ServletUtil;
import com.google.tripmeout.frontend.storage.TripStorage;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.Optional;
import java.util.UUID;
import javax.inject.Inject;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Singleton
public class TripParentServlet extends HttpServlet {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  static final String STUB_USER_ID = "fakeUserId";

  private final Gson gson;
  private final TripStorage storage;

  @Inject
  public TripParentServlet(TripStorage storage, Gson gson) {
    this.gson = gson;
    this.storage = storage;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    PrintWriter writer = response.getWriter();
    try {
      String userId = getUserId();
      response.setContentType("application/json");
      response.setStatus(HttpServletResponse.SC_OK);
      writer.print(gson.toJson(storage.getAllUserTrips(userId)));
    } finally {
      writer.close();
    }
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    logger.atInfo().log("received doPost");
    PrintWriter writer = response.getWriter();
    try {
      TripModel requestTrip =
          ServletUtil.extractFromRequestBody(request.getReader(), gson, TripModel.class);
      TripModel resolvedTrip = resolveDefaults(requestTrip);
      storage.addTrip(resolvedTrip);
      response.setContentType("application/json");
      writer.println(gson.toJson((resolvedTrip)));
    } catch (TripMeOutException e) {
      response.setStatus(e.restStatusCode());
    } catch (JsonParseException e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    } catch (IOException e) {
      logger.atWarning().withCause(e).log("Error serving post request");
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    } finally {
      writer.close();
    }
  }

  private String getUserId() {
    // TODO(issues/66): Gte userId from Userservice
    return STUB_USER_ID;
  }

  private TripModel resolveDefaults(TripModel trip) {
    return trip.toBuilder().setId(UUID.randomUUID().toString()).setUserId(getUserId()).build();
  }
}
