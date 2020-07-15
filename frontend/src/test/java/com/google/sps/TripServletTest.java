package com.google.sps;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.tripmeout.frontend.TripModel;
import com.google.tripmeout.frontend.TripServlet;
import com.google.tripmeout.frontend.error.TripNotFoundException;
import com.google.tripmeout.frontend.serialization.GsonTripModelTypeAdapter;
import com.google.tripmeout.frontend.storage.TripStorage;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TripServletTest {
  private TripServlet servlet;
  private Gson gson;
  private final static TripModel PARIS = TripModel.builder()
                                             .setId("CDG")
                                             .setName("Paris, France")
                                             .setUserId("12345")
                                             .setLocationLat(48.3288)
                                             .setLocationLong(34.0)
                                             .build();

  @Mock TripStorage storage;

  @Mock HttpServletRequest request;

  @Mock HttpServletResponse response;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    servlet = new TripServlet(storage);
    this.gson = new GsonBuilder()
                    .registerTypeAdapter(TripModel.class, new GsonTripModelTypeAdapter())
                    .create();
  }

  @Test
  public void returnsExceptionForBadID()
      throws ServletException, IOException, TripNotFoundException {
    when(request.getRequestURI()).thenReturn("123");

    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    when(response.getWriter()).thenReturn(pw);

    servlet.doGet(request, response);

    String expectedResult = "Trip 123 can not be found.";
    when(storage.getTrip(any())).thenThrow(new TripNotFoundException("Trip 123 can not be found."));

    TripModel result = gson.fromJson(sw.getBuffer().toString().trim(), TripModel.class);
    assertThat(result.toString()).isEqualTo(expectedResult.toString());
  }

  @Test
  public void returnsTripWithGivenId() throws ServletException, IOException, TripNotFoundException {
    when(request.getRequestURI()).thenReturn("12345");

    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    when(response.getWriter()).thenReturn(pw);

    servlet.doGet(request, response);

    TripModel expectedResult = PARIS;
    when(storage.getTrip(any())).thenReturn(expectedResult);

    TripModel result = gson.fromJson(sw.getBuffer().toString().trim(), TripModel.class);
    assertThat(result.name().toString()).isEqualTo(expectedResult.name());
  }
}