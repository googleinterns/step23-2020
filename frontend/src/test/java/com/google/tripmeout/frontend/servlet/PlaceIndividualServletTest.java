package com.google.tripmeout.frontend.servlet;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth8.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.maps.model.PlaceDetails;
import com.google.tripmeout.frontend.PlaceVisitModel;
import com.google.tripmeout.frontend.error.TripMeOutException;
import com.google.tripmeout.frontend.serialization.GsonModelSerializationModule;
import com.google.tripmeout.frontend.servlets.PlaceIndividualServlet;
import com.google.tripmeout.frontend.storage.InMemoryPlaceVisitStorage;
import com.google.tripmeout.frontend.storage.PlaceVisitStorage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class PlaceIndividualServletTest {
  @Mock HttpServletRequest request;
  private Gson gson;

  private static final PlaceVisitModel UK = PlaceVisitModel.builder()
                                                .setName("UK Trip")
                                                .setTripId("abc123")
                                                .setPlaceId("ChIJ3S-JXmauEmsRUcIaWtf4MzE")
                                                .setLatitude(111.111)
                                                .setLongitude(45.4)
                                                .setUserMark(PlaceVisitModel.UserMark.NO)
                                                .build();

  @Before
  public void setup() {
    initMocks(this);
    Injector injector = Guice.createInjector(new GsonModelSerializationModule());
    this.gson = injector.getInstance(Gson.class);
  }

  @Test
  public void doPut_badUri_setsBadRequestStatus() throws IOException {
    FakeHttpServletResponse response = new FakeHttpServletResponse();
    PlaceVisitStorage placeStorage = new InMemoryPlaceVisitStorage();
    PlaceIndividualServlet servlet = new PlaceIndividualServlet(placeStorage, gson);
    when(request.getRequestURI()).thenReturn("/trips/abc123/placesssss/1234");

    servlet.doPut(request, response);

    assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_BAD_REQUEST);
  }

  @Test
  public void doPut_invalidPlaceId_setsBadRequestStatus() throws IOException {
    FakeHttpServletResponse response = new FakeHttpServletResponse();
    PlaceVisitStorage placeStorage = new InMemoryPlaceVisitStorage();
    PlaceIndividualServlet servlet = new PlaceIndividualServlet(placeStorage, gson);
    when(request.getRequestURI()).thenReturn("/trips/abc123/placeVisits/1234");
    Reader stringReader = new StringReader("{tripId: abc123, placeId: 1234, userMark: YES}");
    when(request.getReader()).thenReturn(new BufferedReader(stringReader));

    servlet.doPut(request, response);

    assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_BAD_REQUEST);
  }

  @Test
  public void doPut_validRequest_updatesUserMark() throws IOException, TripMeOutException {
    FakeHttpServletResponse response = new FakeHttpServletResponse();
    PlaceVisitStorage placeStorage = new InMemoryPlaceVisitStorage();
    placeStorage.addPlaceVisit(UK);
    PlaceIndividualServlet servlet = new PlaceIndividualServlet(placeStorage, gson);
    when(request.getRequestURI())
        .thenReturn("/trips/abc123/placeVisits/ChIJ3S-JXmauEmsRUcIaWtf4MzE");
    Reader stringReader =
        new StringReader("{tripId:abc123, placeId: ChIJ3S-JXmauEmsRUcIaWtf4MzE, userMark: YES}");
    when(request.getReader()).thenReturn(new BufferedReader(stringReader));

    servlet.doPut(request, response);

    PlaceVisitModel updatedPlace = UK.toBuilder().setUserMark(PlaceVisitModel.UserMark.YES).build();

    assertThat(placeStorage.getPlaceVisit("abc123", "ChIJ3S-JXmauEmsRUcIaWtf4MzE"))
        .hasValue(updatedPlace);
  }

  @Test
  public void doDelete_badUri_setsBadRequestStatus() throws IOException {
    FakeHttpServletResponse response = new FakeHttpServletResponse();
    PlaceVisitStorage placeStorage = new InMemoryPlaceVisitStorage();
    PlaceIndividualServlet servlet = new PlaceIndividualServlet(placeStorage, gson);
    when(request.getRequestURI())
        .thenReturn("/trips/abc123/placesVisit/ChIJ3S-JXmauEmsRUcIaWtf4MzE");

    servlet.doDelete(request, response);

    assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_BAD_REQUEST);
  }

  @Test
  public void doDelete_notInStorage_setsBadRequestStatus() throws IOException {
    FakeHttpServletResponse response = new FakeHttpServletResponse();
    PlaceVisitStorage placeStorage = new InMemoryPlaceVisitStorage();
    PlaceIndividualServlet servlet = new PlaceIndividualServlet(placeStorage, gson);
    when(request.getRequestURI())
        .thenReturn("/trips/abc123/placesVisits/ChIJ3S-JXmauEmsRUcIaWtf4MzE");

    servlet.doDelete(request, response);

    assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_BAD_REQUEST);
  }

  @Test
  public void doDelete_validRequestInStorage_removesPlaceVisit()
      throws IOException, TripMeOutException {
    FakeHttpServletResponse response = new FakeHttpServletResponse();
    PlaceVisitStorage placeStorage = new InMemoryPlaceVisitStorage();
    placeStorage.addPlaceVisit(UK);
    PlaceIndividualServlet servlet = new PlaceIndividualServlet(placeStorage, gson);

    when(request.getRequestURI())
        .thenReturn("/trips/abc123/placeVisits/ChIJ3S-JXmauEmsRUcIaWtf4MzE");

    servlet.doDelete(request, response);

    assertThat(placeStorage.getPlaceVisit("abc123", "ChIJ3S-JXmauEmsRUcIaWtf4MzE")).isEmpty();
  }

  @Test
  public void doGet_badUri_setsBadRequestStatus() throws IOException {
    FakeHttpServletResponse response = new FakeHttpServletResponse();
    PlaceVisitStorage placeStorage = new InMemoryPlaceVisitStorage();
    PlaceIndividualServlet servlet = new PlaceIndividualServlet(placeStorage, gson);
    when(request.getRequestURI())
        .thenReturn("/trips/abc123/placesVisit/ChIJ3S-JXmauEmsRUcIaWtf4MzE");

    servlet.doGet(request, response);

    assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_BAD_REQUEST);
  }

  @Test
  public void doGet_invalidPlaceId_setsBadRequestStatus() throws IOException {
    FakeHttpServletResponse response = new FakeHttpServletResponse();
    PlaceVisitStorage placeStorage = new InMemoryPlaceVisitStorage();
    PlaceIndividualServlet servlet = new PlaceIndividualServlet(placeStorage, gson);
    when(request.getRequestURI()).thenReturn("/trips/abc123/placesVisits/hello");

    servlet.doGet(request, response);

    assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_BAD_REQUEST);
  }

  @Test
  public void doGet_validRequestNotInStorage_getPlaceDetails() throws IOException {
    FakeHttpServletResponse response = new FakeHttpServletResponse();
    PlaceVisitStorage placeStorage = new InMemoryPlaceVisitStorage();

    PlaceIndividualServlet servlet = new PlaceIndividualServlet(placeStorage, gson);

    when(request.getRequestURI())
        .thenReturn("/trips/abc123/placeVisits/ChIJ3S-JXmauEmsRUcIaWtf4MzE");

    servlet.doGet(request, response);

    PlaceDetails details = gson.fromJson(response.getResponseString(), PlaceDetails.class);

    assertThat(response.getStatus()).isEqualTo(200);
    assertThat(response.getResponseString()).isNotEmpty();

    assertThat(details.name).isEqualTo("Sydney Opera House");
    assertThat(details.htmlAttributions[0]).isEqualTo(PlaceVisitModel.UserMark.UNKNOWN.toString());
  }

  @Test
  public void doGet_validRequestInStorage_getPlaceDetails() throws IOException, TripMeOutException {
    FakeHttpServletResponse response = new FakeHttpServletResponse();
    PlaceVisitStorage placeStorage = new InMemoryPlaceVisitStorage();
    placeStorage.addPlaceVisit(UK);

    PlaceIndividualServlet servlet = new PlaceIndividualServlet(placeStorage, gson);

    when(request.getRequestURI())
        .thenReturn("/trips/abc123/placeVisits/ChIJ3S-JXmauEmsRUcIaWtf4MzE");

    servlet.doGet(request, response);

    PlaceDetails details = gson.fromJson(response.getResponseString(), PlaceDetails.class);

    assertThat(response.getStatus()).isEqualTo(200);
    assertThat(response.getResponseString()).isNotEmpty();

    assertThat(details.name).isEqualTo("Sydney Opera House");
    assertThat(details.htmlAttributions[0]).isEqualTo(PlaceVisitModel.UserMark.NO.toString());
  }
}
