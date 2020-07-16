package com.google.sps;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.tripmeout.frontend.TripModel;
import com.google.tripmeout.frontend.serialization.GsonTripModelTypeAdapter;
import com.google.tripmeout.frontend.servlet.TripParentServlet;
import com.google.tripmeout.frontend.storage.TripStorage;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

@RunWith(JUnit4.class)
public class TripParentServletTest {
  @Mock TripStorage storage;
  @Mock HttpServletRequest request;
  @Mock HttpServletResponse response;

  Gson gson;
  TripParentServlet tripParentServlet;

  @Before
  public void setup() {
    initMocks(this);
    gson = new GsonBuilder()
               .registerTypeAdapter(TripModel.class, new GsonTripModelTypeAdapter())
               .create();
    tripParentServlet = new TripParentServlet(storage, gson);
  }

  @Test
  public void doGet_noTrips() throws Exception {
    when(storage.getAllUserTrips("user1")).thenReturn(ImmutableList.of());
    StringWriter responseCapture = new StringWriter();
    when(response.getWriter()).thenReturn(new PrintWriter(responseCapture));

    tripParentServlet.doGet(request, response);

    verify(response).setContentType("application/json");
    assertThat(responseCapture.toString()).isEqualTo("[]");
  }
  @Test
  public void doGet_someTrips() throws Exception {
    TripModel trip1 = createTripWithUserId("user1");
    TripModel trip2 = createTripWithUserId("user1");
    when(storage.getAllUserTrips("user1")).thenReturn(ImmutableList.of(trip1, trip2));
    StringWriter responseCapture = new StringWriter();
    when(response.getWriter()).thenReturn(new PrintWriter(responseCapture));
    tripParentServlet.doGet(request, response);
    verify(response).setContentType("application/json");
    List<TripModel> tripResponse =
        gson.fromJson(responseCapture.toString(), new TypeToken<List<TripModel>>() {}.getType());
    assertThat(tripResponse).containsExactly(trip1, trip2);
  }

  @Test
  public void doPost_ioExceptionWhenReadingRequest_internalServerErrorResponse() throws Exception {
    when(request.getParameter("name")).thenThrow(new IOException("something bad happened"));
    when(request.getContentType()).thenReturn("application/json;");

    tripParentServlet.doPost(request, response);

    verify(response).setStatus(500);
  }

  @Test
  public void doPost_validTripJson() throws Exception {
    TripModel trip = createTripWithUserId("user1");
    when(request.getParameter("name")).thenReturn("name");
    when(request.getParameter("locationLong")).thenReturn("44.2");
    when(request.getParameter("locationLat")).thenReturn("50.3");
    when(request.getParameter("userId")).thenReturn("user1");
    when(request.getContentType()).thenReturn("application/json;");
    StringWriter responseCapture = new StringWriter();
    when(response.getWriter()).thenReturn(new PrintWriter(responseCapture));

    tripParentServlet.doPost(request, response);
    verify(response).setContentType("application/json");
    TripModel responseTrip = gson.fromJson(responseCapture.toString(), TripModel.class);
    ArgumentCaptor<TripModel> storageCaptor = ArgumentCaptor.forClass(TripModel.class);
    verify(storage).addTrip(storageCaptor.capture());
    assertThat(responseTrip).isEqualTo(storageCaptor.getValue());
    assertThat(responseTrip.id()).isNotEmpty();
    assertThat(responseTrip.userId()).isEqualTo(trip.userId());
    assertThat(responseTrip.name()).isEqualTo(trip.name());
    assertThat(responseTrip.locationLat()).isEqualTo(trip.locationLat());
    assertThat(responseTrip.locationLong()).isEqualTo(trip.locationLong());
  }
  @Test
  public void doPost_invalidTrip_NoTripName() throws Exception {
    when(request.getParameter("locationLong")).thenReturn("44.2");
    when(request.getParameter("locationLat")).thenReturn("50.3");
    when(request.getParameter("userId")).thenReturn("user1");
    tripParentServlet.doPost(request, response);
    verify(response).setStatus(400);
  }

  @Test
  public void doPost_invalidTrip_NoUserId() throws Exception {
    when(request.getParameter("name")).thenReturn("name");
    when(request.getParameter("locationLong")).thenReturn("44.2");
    when(request.getParameter("locationLat")).thenReturn("50.3");
    tripParentServlet.doPost(request, response);
    verify(response).setStatus(400);
  }
  @Test
  public void doPost_invalidTrip_NoLongitude() throws Exception {
    when(request.getParameter("name")).thenReturn("name");
    when(request.getParameter("locationLat")).thenReturn("50.3");
    when(request.getParameter("userId")).thenReturn("user1");
    tripParentServlet.doPost(request, response);
    verify(response).setStatus(400);
  }
  @Test
  public void doPost_invalidTrip_NoLatitude() throws Exception {
    when(request.getParameter("name")).thenReturn("name");
    when(request.getParameter("locationLong")).thenReturn("44.2");
    when(request.getParameter("userId")).thenReturn("user1");
    tripParentServlet.doPost(request, response);
    verify(response).setStatus(400);
  }

  @Test
  public void doPost_noContentTypeSet_badRequestResponse() throws Exception {
    tripParentServlet.doPost(request, response);

    verify(response).setStatus(400);
  }

  private static TripModel createTripWithUserId(String userId) {
    return TripModel.builder()
        .setId("id")
        .setName("name")
        .setUserId(userId)
        .setLocationLat(50.3)
        .setLocationLong(44.2)
        .build();
  }
}
