package com.google.tripmeout.frontend.servlet;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth8.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.maps.GeoApiContext;
import com.google.maps.PlaceDetailsRequest;
import com.google.maps.errors.ApiException;
import com.google.maps.model.PlaceDetails;
import com.google.tripmeout.frontend.PlaceVisitModel;
import com.google.tripmeout.frontend.error.TripMeOutException;
import com.google.tripmeout.frontend.serialization.GsonModelSerializationModule;
import com.google.tripmeout.frontend.servlet.PlaceParentServlet;
import com.google.tripmeout.frontend.storage.InMemoryPlaceVisitStorage;
import com.google.tripmeout.frontend.storage.PlaceVisitStorage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class PlaceParentServletTest {
  @Mock HttpServletRequest request;
  @Mock PlaceDetailsRequest detailsRequest;
  private Gson gson;

  private final GeoApiContext CONTEXT =
      new GeoApiContext.Builder().apiKey("AIzaSyBbXCXC2uWv3baNmirLtqUYbFsFwCXqLV8").build();

  private static final PlaceVisitModel UK = PlaceVisitModel.builder()
                                                .setName("UK Trip")
                                                .setTripId("abc123")
                                                .setPlaceId("ChIJ3S-JXmauEmsRUcIaWtf4MzE")
                                                .setLatitude(111.111)
                                                .setLongitude(45.4)
                                                .setUserMark(PlaceVisitModel.UserMark.NO)
                                                .build();

  private static final PlaceVisitModel NY = PlaceVisitModel.builder()
                                                .setName("NY Trip")
                                                .setTripId("abc123")
                                                .setPlaceId("NY, USA")
                                                .setLatitude(111.111)
                                                .setLongitude(45.4)
                                                .setUserMark(PlaceVisitModel.UserMark.MAYBE)
                                                .build();

  private static final PlaceVisitModel CA = PlaceVisitModel.builder()
                                                .setName("CA Trip")
                                                .setTripId("abc123")
                                                .setPlaceId("CA, USA")
                                                .setLatitude(111.111)
                                                .setLongitude(45.4)
                                                .setUserMark(PlaceVisitModel.UserMark.YES)
                                                .build();

  private static final PlaceVisitModel UK_B = PlaceVisitModel.builder()
                                                  .setName("UK Trip")
                                                  .setTripId("123abc")
                                                  .setPlaceId("ChIJ3S-JXmauEmsRUcIaWtf4MzE")
                                                  .setLatitude(111.111)
                                                  .setLongitude(45.4)
                                                  .setUserMark(PlaceVisitModel.UserMark.MAYBE)
                                                  .build();

  @Before
  public void setUp() {
    initMocks(this);
    Injector injector = Guice.createInjector(new GsonModelSerializationModule());
    this.gson = injector.getInstance(Gson.class);
  }

  @Test
  public void doPost_badUri_setsBadRequestStatus() throws IOException {
    FakeHttpServletResponse response = new FakeHttpServletResponse();
    PlaceVisitStorage placeStorage = new InMemoryPlaceVisitStorage();
    PlaceParentServlet servlet = new PlaceParentServlet(placeStorage, gson);
    when(request.getRequestURI()).thenReturn("/trips-number/abc123/placeVisits");
    when(request.getReader())
        .thenReturn(new BufferedReader(
            new StringReader("{tripId: abc123, placeId: ChIJ3S-JXmauEmsRUcIaWtf4MzE}")));

    servlet.doPost(request, response);

    assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_BAD_REQUEST);
  }

  @Test
  public void doPost_badPlaceId_setsNotFoundStatus() throws IOException {
    FakeHttpServletResponse response = new FakeHttpServletResponse();
    PlaceVisitStorage placeStorage = new InMemoryPlaceVisitStorage();
    PlaceParentServlet servlet = new PlaceParentServlet(placeStorage, gson);
    when(request.getRequestURI()).thenReturn("/trips/abc123/placeVisits");
    when(request.getReader())
        .thenReturn(new BufferedReader(new StringReader("{tripId: abc123, placeId: hello}")));

    servlet.doPost(request, response);
    Optional<PlaceVisitModel> place = placeStorage.getPlaceVisit("abc123", "hello");

    assertThat(place).isEmpty();
    assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_NOT_FOUND);
  }

  @Test
  public void doPost_goodRequest_storesPlaceVisitModel()
      throws IOException, ApiException, InterruptedException {
    PlaceDetailsRequest placeRequest = new PlaceDetailsRequest(CONTEXT)
                                           .placeId("ChIJ3S-JXmauEmsRUcIaWtf4MzE")
                                           .fields(PlaceDetailsRequest.FieldMask.GEOMETRY_LOCATION,
                                               PlaceDetailsRequest.FieldMask.NAME);

    PlaceDetails details;
    details = placeRequest.await();

    PlaceVisitModel expected = PlaceVisitModel.builder()
                                   .setTripId("abc123")
                                   .setPlaceId("ChIJ3S-JXmauEmsRUcIaWtf4MzE")
                                   .setUserMark(PlaceVisitModel.UserMark.YES)
                                   .setName(details.name)
                                   .setLatitude(details.geometry.location.lat)
                                   .setLongitude(details.geometry.location.lng)
                                   .build();

    FakeHttpServletResponse response = new FakeHttpServletResponse();
    PlaceVisitStorage placeStorage = new InMemoryPlaceVisitStorage();
    PlaceParentServlet servlet = new PlaceParentServlet(placeStorage, gson);
    when(request.getRequestURI()).thenReturn("/trips/abc123/placeVisits");
    when(request.getReader())
        .thenReturn(new BufferedReader(
            new StringReader("{tripId: abc123, placeId: ChIJ3S-JXmauEmsRUcIaWtf4MzE}")));

    servlet.doPost(request, response);
    Optional<PlaceVisitModel> place =
        placeStorage.getPlaceVisit("abc123", "ChIJ3S-JXmauEmsRUcIaWtf4MzE");

    assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
    assertThat(place).hasValue(expected);
  }

  @Test
  public void doPost_requestAlreadyInStorage_updatesPlaceVisitModelUserMark()
      throws IOException, TripMeOutException {
    FakeHttpServletResponse response = new FakeHttpServletResponse();
    PlaceVisitModel updatedUK = UK.toBuilder().setUserMark(PlaceVisitModel.UserMark.YES).build();

    PlaceVisitStorage placeStorage = new InMemoryPlaceVisitStorage();
    placeStorage.addPlaceVisit(UK);
    PlaceParentServlet servlet = new PlaceParentServlet(placeStorage, gson);
    when(request.getRequestURI()).thenReturn("/trips/abc123/placeVisits");
    when(request.getReader())
        .thenReturn(new BufferedReader(
            new StringReader("{tripId: abc123, placeId: ChIJ3S-JXmauEmsRUcIaWtf4MzE}")));

    servlet.doPost(request, response);
    Optional<PlaceVisitModel> place =
        placeStorage.getPlaceVisit("abc123", "ChIJ3S-JXmauEmsRUcIaWtf4MzE");

    assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
    assertThat(place.get()).isEqualTo(updatedUK);
  }

  @Test
  public void doGet_badUri_setsBadRequestStatus() throws IOException {
    FakeHttpServletResponse response = new FakeHttpServletResponse();
    PlaceVisitStorage placeStorage = new InMemoryPlaceVisitStorage();
    PlaceParentServlet servlet = new PlaceParentServlet(placeStorage, gson);
    when(request.getRequestURI()).thenReturn("/trips-number/1234/placeVisits");

    servlet.doGet(request, response);

    assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_BAD_REQUEST);
  }

  @Test
  public void doGet_emptyStorage_getsEmptyList() throws IOException {
    FakeHttpServletResponse response = new FakeHttpServletResponse();
    PlaceVisitStorage placeStorage = new InMemoryPlaceVisitStorage();
    PlaceParentServlet servlet = new PlaceParentServlet(placeStorage, gson);
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

    PlaceParentServlet servlet = new PlaceParentServlet(placeStorage, gson);
    when(request.getRequestURI()).thenReturn("/trips/abc123/placeVisits");
    servlet.doGet(request, response);

    Type placeVisitListType = new TypeToken<ArrayList<PlaceVisitModel>>() {}.getType();
    List<PlaceVisitModel> result = gson.fromJson(response.getResponseString(), placeVisitListType);

    assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
    assertThat(result).containsExactly(NY, CA, UK);
  }
}
