package com.google.sps;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.tripmeout.frontend.TripModel;
import com.google.tripmeout.frontend.TripServlet;
import com.google.tripmeout.frontend.error.TripAlreadyExistsException;
import com.google.tripmeout.frontend.error.TripNotFoundException;
import com.google.tripmeout.frontend.serialization.GsonTripModelTypeAdapter;
import com.google.tripmeout.frontend.storage.InMemoryTripModelStorage;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

  @Mock HttpServletResponse response;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    this.gson = new GsonBuilder()
                    .registerTypeAdapter(TripModel.class, new GsonTripModelTypeAdapter())
                    .create();
  }

  @Test
  public void returnsErrorCodeForBadID()
      throws ServletException, IOException, TripNotFoundException, TripAlreadyExistsException {
    InMemoryTripModelStorage storage = new InMemoryTripModelStorage();
    storage.addTrip(PARIS);
    TripServlet servlet = new TripServlet(storage, gson);

    when(request.getRequestURI()).thenReturn("Not CDG");

    servlet.doGet(request, response);
    verify(response).setStatus(404);
  }

  @Test
  public void returnsTripWithGivenId()
      throws ServletException, IOException, TripNotFoundException, TripAlreadyExistsException {
    InMemoryTripModelStorage storage = new InMemoryTripModelStorage();
    storage.addTrip(PARIS);
    TripServlet servlet = new TripServlet(storage, gson);

    when(request.getRequestURI()).thenReturn("CDG");

    servlet.doGet(request, response);

    TripModel expectedResult = PARIS;
    assertThat(response).isEqualTo(expectedResult);
  }

  @Test
  public void returnsSuccessCodeWhenTripIsDeleted()
      throws ServletException, IOException, TripNotFoundException, TripAlreadyExistsException {
    InMemoryTripModelStorage storage = new InMemoryTripModelStorage();
    storage.addTrip(PARIS);
    TripServlet servlet = new TripServlet(storage, gson);

    when(request.getRequestURI()).thenReturn("CDG");

    servlet.doDelete(request, response);
    verify(response).setStatus(202);
  }

  @Test
  public void returnsErrorCodeWhenTripNotFound()
      throws ServletException, IOException, TripNotFoundException, TripAlreadyExistsException {
    InMemoryTripModelStorage storage = new InMemoryTripModelStorage();
    storage.addTrip(PARIS);
    TripServlet servlet = new TripServlet(storage, gson);

    when(request.getRequestURI()).thenReturn("Not CDG");

    servlet.doDelete(request, response);
    verify(response).setStatus(404);
  }
}