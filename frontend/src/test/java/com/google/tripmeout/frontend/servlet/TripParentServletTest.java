package com.google.tripmeout.frontend.servlet;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.tripmeout.frontend.TripModel;
import com.google.tripmeout.frontend.serialization.GsonTripModelTypeAdapter;
import com.google.tripmeout.frontend.storage.TripStorage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
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
  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
  Gson gson;
  TripParentServlet tripParentServlet;

  @Before
  public void setup() {
    helper.setUp();
    initMocks(this);
    gson = new GsonBuilder()
               .registerTypeAdapter(TripModel.class, new GsonTripModelTypeAdapter())
               .create();
    tripParentServlet = new TripParentServlet(storage, gson);
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test
  public void doGet_noTrips() throws Exception {
    // TODO(issues/66): Use specif userId string
    when(storage.getAllUserTrips(TripParentServlet.STUB_USER_ID)).thenReturn(ImmutableList.of());

    FakeHttpServletResponse response = new FakeHttpServletResponse();

    tripParentServlet.doGet(request, response);

    assertThat(response.getContentType()).isEqualTo("application/json");
    assertThat(response.getResponseString()).isEqualTo("[]");
  }

  @Test
  public void doGet_someTrips() throws Exception {
    // TODO(issues/66): Use specif userId string
    TripModel trip1 = createTripWithUserId(TripParentServlet.STUB_USER_ID);
    TripModel trip2 = createTripWithUserId(TripParentServlet.STUB_USER_ID);
    when(storage.getAllUserTrips(TripParentServlet.STUB_USER_ID))
        .thenReturn(ImmutableList.of(trip1, trip2));
    FakeHttpServletResponse response = new FakeHttpServletResponse();
    tripParentServlet.doGet(request, response);
    assertThat(response.getContentType()).isEqualTo("application/json");
    List<TripModel> tripResponse =
        gson.fromJson(response.getResponseString(), new TypeToken<List<TripModel>>() {}.getType());
    assertThat(tripResponse).containsExactly(trip1, trip2);
  }

  @Test
  public void doPost_ioExceptionWhenReadingRequest_internalServerErrorResponse() throws Exception {
    when(request.getReader()).thenThrow(new IOException("something bad happened"));
    when(request.getContentType()).thenReturn("application/json;");
    FakeHttpServletResponse response = new FakeHttpServletResponse();
    tripParentServlet.doPost(request, response);

    assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
  }
  @Test
  public void doPost_invalidTrip_badRequestResponse() throws Exception {
    when(request.getReader())
        .thenReturn(new BufferedReader(new StringReader("not really even json?")));
    when(request.getContentType()).thenReturn("application/json;");
    FakeHttpServletResponse response = new FakeHttpServletResponse();
    tripParentServlet.doPost(request, response);

    assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_BAD_REQUEST);
  }

  @Test
  public void doPost_wellFormedRequest_fillsInDefaultsAndPersistsTrip() throws Exception {
    // TODO(issues/66): Use specif userId string
    TripModel trip = createTripForPost();
    FakeHttpServletResponse response = new FakeHttpServletResponse();

    when(request.getReader()).thenReturn(new BufferedReader(new StringReader(gson.toJson(trip))));
    tripParentServlet.doPost(request, response);
    TripModel responseTrip = gson.fromJson(response.getResponseString(), TripModel.class);
    assertThat(responseTrip.name()).isEqualTo(trip.name());
    assertThat(responseTrip.placesApiPlaceId()).isEqualTo(trip.placesApiPlaceId());
    assertThat(responseTrip.id()).isNotEmpty();
    assertThat(responseTrip.userId()).isEqualTo(TripParentServlet.STUB_USER_ID);
    ArgumentCaptor<TripModel> storageCaptor = ArgumentCaptor.forClass(TripModel.class);
    verify(storage).addTrip(storageCaptor.capture());
    assertThat(responseTrip).isEqualTo(storageCaptor.getValue());
  }

  private static TripModel createTripWithUserId(String userId) {
    return TripModel.builder()
        .setId("id")
        .setName("name")
        .setUserId(userId)
        .setPlacesApiPlaceId("place-api-place-id")
        .build();
  }
  private static TripModel createTripForPost() {
    return TripModel.builder()
        .setId("id")
        .setName("name")
        .setUserId("")
        .setPlacesApiPlaceId("place-api-place-id")
        .build();
  }
}
