package com.google.tripmeout.frontend.servlet;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.tripmeout.frontend.TripModel;
import com.google.tripmeout.frontend.error.TripAlreadyExistsException;
import com.google.tripmeout.frontend.error.TripNotFoundException;
import com.google.tripmeout.frontend.serialization.GsonTripModelTypeAdapter;
import com.google.tripmeout.frontend.servlet.TripServlet;
import com.google.tripmeout.frontend.storage.InMemoryTripModelStorage;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TripServletTest {
  private Gson gson;
  private final static TripModel PARIS = TripModel.builder()
                                             .setId("CDG")
                                             .setName("Paris, France")
                                             .setUserId("12345")
                                             .setLocationLat(48.3288)
                                             .setLocationLong(34.0)
                                             .build();

  @Mock HttpServletRequest request;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    this.gson = new GsonBuilder()
                    .registerTypeAdapter(TripModel.class, new GsonTripModelTypeAdapter())
                    .create();
  }

  @Test
  public void returnsErrorCodeForBadIdForDoGet()
      throws ServletException, IOException, TripNotFoundException, TripAlreadyExistsException {
    InMemoryTripModelStorage storage = new InMemoryTripModelStorage();
    storage.addTrip(PARIS);
    FakeHttpServletResponse response = new FakeHttpServletResponse();
    TripServlet servlet = new TripServlet(storage, gson);

    when(request.getRequestURI()).thenReturn("/trips/Not_CDG");

    servlet.doGet(request, response);
    assertThat(response.getStatus()).isEqualTo(404);
  }

  @Test
  public void returnsErrorCodeForBadUriForDoGet()
      throws ServletException, IOException, TripNotFoundException, TripAlreadyExistsException {
    InMemoryTripModelStorage storage = new InMemoryTripModelStorage();
    storage.addTrip(PARIS);
    FakeHttpServletResponse response = new FakeHttpServletResponse();
    TripServlet servlet = new TripServlet(storage, gson);

    when(request.getRequestURI()).thenReturn("/bad_trips_uri/CDG");

    servlet.doGet(request, response);
    assertThat(response.getStatus()).isEqualTo(500);
  }

  @Test
  public void returnsTripWithGivenIdForDoGet()
      throws ServletException, IOException, TripNotFoundException, TripAlreadyExistsException {
    InMemoryTripModelStorage storage = new InMemoryTripModelStorage();
    storage.addTrip(PARIS);
    FakeHttpServletResponse response = new FakeHttpServletResponse();
    TripServlet servlet = new TripServlet(storage, gson);

    when(request.getRequestURI()).thenReturn("/trips/CDG");

    servlet.doGet(request, response);

    TripModel responseTrip = gson.fromJson(response.getResponseString(), TripModel.class);
    assertThat(responseTrip).isEqualTo(PARIS);
  }

  @Test
  public void returnsSuccessCodeWhenTripIsDeletedForDoDelete()
      throws ServletException, IOException, TripNotFoundException, TripAlreadyExistsException {
    InMemoryTripModelStorage storage = new InMemoryTripModelStorage();
    storage.addTrip(PARIS);
    FakeHttpServletResponse response = new FakeHttpServletResponse();
    TripServlet servlet = new TripServlet(storage, gson);

    when(request.getRequestURI()).thenReturn("/trips/CDG");

    servlet.doDelete(request, response);
    assertThat(response.getStatus()).isEqualTo(200);
  }

  @Test
  public void returnsErrorCodeWhenTripNotFoundForDoDelete()
      throws ServletException, IOException, TripNotFoundException, TripAlreadyExistsException {
    InMemoryTripModelStorage storage = new InMemoryTripModelStorage();
    storage.addTrip(PARIS);
    FakeHttpServletResponse response = new FakeHttpServletResponse();
    TripServlet servlet = new TripServlet(storage, gson);

    when(request.getRequestURI()).thenReturn("/trips/Not_CDG");

    servlet.doDelete(request, response);
    assertThat(response.getStatus()).isEqualTo(404);
  }

  @Test
  public void returnsErrorCodeForBadUriForDoDelete()
      throws ServletException, IOException, TripNotFoundException, TripAlreadyExistsException {
    InMemoryTripModelStorage storage = new InMemoryTripModelStorage();
    storage.addTrip(PARIS);
    FakeHttpServletResponse response = new FakeHttpServletResponse();
    TripServlet servlet = new TripServlet(storage, gson);

    when(request.getRequestURI()).thenReturn("/bad_trips_uri/CDG");

    servlet.doDelete(request, response);
    assertThat(response.getStatus()).isEqualTo(500);
  }
}
