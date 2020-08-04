package com.google.tripmeout.frontend.servlet;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.maps.errors.ApiException;
import com.google.tripmeout.frontend.PlaceVisitModel;
import com.google.tripmeout.frontend.error.InvalidPlaceIdException;
import com.google.tripmeout.frontend.error.PlacesApiRequestException;
import com.google.tripmeout.frontend.error.TripMeOutException;
import com.google.tripmeout.frontend.places.PlaceService;
import com.google.tripmeout.frontend.serialization.GsonModelSerializationModule;
import com.google.tripmeout.frontend.storage.InMemoryPlaceVisitStorage;
import com.google.tripmeout.frontend.storage.PlaceVisitStorage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class PlaceVisitParentServletTest {
  @Mock HttpServletRequest request;
  @Mock PlaceService placeService;
  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
  private Gson gson;

  private static final PlaceVisitModel UK = PlaceVisitModel.builder()
                                                .setId("123")
                                                .setPlaceName("UK Trip")
                                                .setTripId("abc123")
                                                .setPlacesApiPlaceId("ChIJ3S-JXmauEmsRUcIaWtf4MzE")
                                                .setUserMark(PlaceVisitModel.UserMark.NO)
                                                .build();

  private static final PlaceVisitModel NY = PlaceVisitModel.builder()
                                                .setId("456")
                                                .setPlaceName("NY Trip")
                                                .setTripId("abc123")
                                                .setPlacesApiPlaceId("NY, USA")
                                                .setUserMark(PlaceVisitModel.UserMark.MAYBE)
                                                .build();

  private static final PlaceVisitModel CA = PlaceVisitModel.builder()
                                                .setId("789")
                                                .setPlaceName("CA Trip")
                                                .setTripId("abc123")
                                                .setPlacesApiPlaceId("CA, USA")
                                                .setUserMark(PlaceVisitModel.UserMark.YES)
                                                .build();

  private static final PlaceVisitModel UK_B =
      PlaceVisitModel.builder()
          .setId("123")
          .setPlaceName("UK Trip")
          .setTripId("123abc")
          .setPlacesApiPlaceId("ChIJ3S-JXmauEmsRUcIaWtf4MzE")
          .setUserMark(PlaceVisitModel.UserMark.MAYBE)
          .build();

  @Before
  public void setUp() {
    helper.setUp();
    initMocks(this);
    Injector injector = Guice.createInjector(new GsonModelSerializationModule());
    gson = injector.getInstance(Gson.class);
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test
  public void doPost_badUri_setsInternalServerError() throws IOException {
    FakeHttpServletResponse response = new FakeHttpServletResponse();
    PlaceVisitStorage placeStorage = new InMemoryPlaceVisitStorage();
    PlaceVisitParentServlet servlet = new PlaceVisitParentServlet(placeStorage, gson, placeService);
    when(request.getRequestURI()).thenReturn("/trips-number/abc123/placeVisits");
    when(request.getReader()).thenReturn(new BufferedReader(new StringReader(gson.toJson(UK))));

    servlet.doPost(request, response);

    assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
  }

  @Test
  public void doPost_badPlaceId_setsNotFoundStatus()
      throws IOException, InvalidPlaceIdException, PlacesApiRequestException {
    FakeHttpServletResponse response = new FakeHttpServletResponse();
    PlaceVisitStorage placeStorage = new InMemoryPlaceVisitStorage();
    PlaceVisitParentServlet servlet = new PlaceVisitParentServlet(placeStorage, gson, placeService);

    doThrow(InvalidPlaceIdException.class).when(placeService).validatePlaceId("NY, USA");
    when(request.getRequestURI()).thenReturn("/trips/abc123/placeVisits");
    when(request.getReader()).thenReturn(new BufferedReader(new StringReader(gson.toJson(NY))));

    servlet.doPost(request, response);

    assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_BAD_REQUEST);
  }

  @Test
  public void doPost_goodRequest_returnsAddedPlaceVisitModel()
      throws IOException, ApiException, InterruptedException {
    FakeHttpServletResponse response = new FakeHttpServletResponse();
    PlaceVisitStorage placeStorage = new InMemoryPlaceVisitStorage();
    PlaceVisitParentServlet servlet = new PlaceVisitParentServlet(placeStorage, gson, placeService);
    Key parentKey = KeyFactory.createKey("TripMeOut", NY.tripId());
    String id = KeyFactory.keyToString(
        KeyFactory.createKey(parentKey, NY.tripId(), UUID.randomUUID().toString()));
    when(request.getRequestURI()).thenReturn("/trips/" + id + "/placeVisits");
    when(request.getReader()).thenReturn(new BufferedReader(new StringReader(gson.toJson(NY))));

    servlet.doPost(request, response);
    PlaceVisitModel place = gson.fromJson(response.getResponseString(), PlaceVisitModel.class);

    assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_CREATED);
    assertThat(place.placeName()).isEqualTo("NY Trip");
    assertThat(place.tripId()).isEqualTo(id);
    assertThat(place.placesApiPlaceId()).isEqualTo("NY, USA");
    assertThat(place.userMark()).isEqualTo(PlaceVisitModel.UserMark.MAYBE);
  }

  @Test
  public void doPost_requestAlreadyInStorage_returnsAddedPlaceVisitModel()
      throws IOException, TripMeOutException {
    FakeHttpServletResponse response = new FakeHttpServletResponse();

    PlaceVisitStorage placeStorage = new InMemoryPlaceVisitStorage();
    placeStorage.addPlaceVisit(UK);

    PlaceVisitParentServlet servlet = new PlaceVisitParentServlet(placeStorage, gson, placeService);
    Key parentKey = KeyFactory.createKey("TripMeOut", UK.tripId());
    String id = KeyFactory.keyToString(
        KeyFactory.createKey(parentKey, UK.tripId(), UUID.randomUUID().toString()));
    when(request.getRequestURI()).thenReturn("/trips/" + id + "/placeVisits");
    when(request.getReader()).thenReturn(new BufferedReader(new StringReader(gson.toJson(UK))));

    servlet.doPost(request, response);
    PlaceVisitModel place = gson.fromJson(response.getResponseString(), PlaceVisitModel.class);

    assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_CREATED);
    assertThat(place.placeName()).isEqualTo("UK Trip");
    assertThat(place.tripId()).isEqualTo(id);
    assertThat(place.placesApiPlaceId()).isEqualTo("ChIJ3S-JXmauEmsRUcIaWtf4MzE");
    assertThat(place.userMark()).isEqualTo(PlaceVisitModel.UserMark.NO);
  }

  @Test
  public void doGet_badUri_setsInternalServerError() throws IOException {
    FakeHttpServletResponse response = new FakeHttpServletResponse();
    PlaceVisitStorage placeStorage = new InMemoryPlaceVisitStorage();
    PlaceVisitParentServlet servlet = new PlaceVisitParentServlet(placeStorage, gson, placeService);
    when(request.getRequestURI()).thenReturn("/trips-number/1234/placeVisits");

    servlet.doGet(request, response);

    assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
  }

  @Test
  public void doGet_emptyStorage_getsEmptyList() throws IOException {
    FakeHttpServletResponse response = new FakeHttpServletResponse();
    PlaceVisitStorage placeStorage = new InMemoryPlaceVisitStorage();
    PlaceVisitParentServlet servlet = new PlaceVisitParentServlet(placeStorage, gson, placeService);
    when(request.getRequestURI()).thenReturn("/trips/abc123/placeVisits");

    servlet.doGet(request, response);

    Type placeVisitListType = new TypeToken<ArrayList<PlaceVisitModel>>() {}.getType();
    List<PlaceVisitModel> result = gson.fromJson(response.getResponseString(), placeVisitListType);

    assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
    assertThat(result).isEmpty();
  }

  @Test
  public void doGet_nonEmptyStorage_getsPlaceVisitList() throws IOException, TripMeOutException {
    FakeHttpServletResponse response = new FakeHttpServletResponse();
    PlaceVisitStorage placeStorage = new InMemoryPlaceVisitStorage();
    placeStorage.addPlaceVisit(UK);
    placeStorage.addPlaceVisit(NY);
    placeStorage.addPlaceVisit(CA);
    placeStorage.addPlaceVisit(UK_B);

    PlaceVisitParentServlet servlet = new PlaceVisitParentServlet(placeStorage, gson, placeService);
    when(request.getRequestURI()).thenReturn("/trips/abc123/placeVisits");
    servlet.doGet(request, response);

    Type placeVisitListType = new TypeToken<ArrayList<PlaceVisitModel>>() {}.getType();
    List<PlaceVisitModel> result = gson.fromJson(response.getResponseString(), placeVisitListType);

    assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
    assertThat(result).containsExactly(NY, CA, UK);
  }
}
