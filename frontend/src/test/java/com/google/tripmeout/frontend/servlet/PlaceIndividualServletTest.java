package com.google.tripmeout.frontend.servlet;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth8.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.tripmeout.frontend.PlaceVisitModel;
import com.google.tripmeout.frontend.error.InvalidPlaceIdException;
import com.google.tripmeout.frontend.error.PlacesApiRequestException;
import com.google.tripmeout.frontend.error.TripMeOutException;
import com.google.tripmeout.frontend.places.PlaceService;
import com.google.tripmeout.frontend.serialization.GsonModelSerializationModule;
import com.google.tripmeout.frontend.servlet.PlaceIndividualServlet;
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
  @Mock PlaceService placeService;
  private Gson gson;

  private static final PlaceVisitModel UK = PlaceVisitModel.builder()
                                                .setPlaceName("UK Trip")
                                                .setTripId("abc123")
                                                .setId("abc")
                                                .setPlacesApiPlaceId("123")
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
    PlaceIndividualServlet servlet = new PlaceIndividualServlet(placeStorage, gson, placeService);
    when(request.getRequestURI()).thenReturn("/trips/abc123/placesssss/1234");

    servlet.doPut(request, response);

    assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
  }

  @Test
  public void doPut_invalidPlacesApiPlaceId_setsBadRequestStatus()
      throws IOException, InvalidPlaceIdException, PlacesApiRequestException {
    FakeHttpServletResponse response = new FakeHttpServletResponse();
    PlaceVisitStorage placeStorage = new InMemoryPlaceVisitStorage();
    PlaceIndividualServlet servlet = new PlaceIndividualServlet(placeStorage, gson, placeService);

    doThrow(InvalidPlaceIdException.class).when(placeService).validatePlaceId("123");
    when(request.getRequestURI()).thenReturn("/trips/abc123/placeVisits/1234");
    Reader stringReader = new StringReader("{placesApiPlaceId: 123, userMark: YES}");
    when(request.getReader()).thenReturn(new BufferedReader(stringReader));

    servlet.doPut(request, response);

    assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_BAD_REQUEST);
  }

  @Test
  public void doPut_newPlaceVisitId_addsPlaceVisitToStorage()
      throws IOException, InvalidPlaceIdException, PlacesApiRequestException {
    FakeHttpServletResponse response = new FakeHttpServletResponse();
    PlaceVisitStorage placeStorage = new InMemoryPlaceVisitStorage();
    PlaceIndividualServlet servlet = new PlaceIndividualServlet(placeStorage, gson, placeService);
    when(request.getRequestURI()).thenReturn("/trips/abc123/placeVisits/1234");
    Reader stringReader = new StringReader("{placesApiPlaceId: 123, userMark: YES}");
    when(request.getReader()).thenReturn(new BufferedReader(stringReader));

    servlet.doPut(request, response);

    PlaceVisitModel expected = PlaceVisitModel.builder()
                                   .setTripId("abc123")
                                   .setId("1234")
                                   .setPlacesApiPlaceId("123")
                                   .setUserMark(PlaceVisitModel.UserMark.YES)
                                   .build();

    assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
    assertThat(placeStorage.getPlaceVisit("abc123", "1234")).hasValue(expected);
  }

  @Test
  public void doPut_validRequest_updatesUserMark() throws IOException, TripMeOutException {
    FakeHttpServletResponse response = new FakeHttpServletResponse();
    PlaceVisitStorage placeStorage = new InMemoryPlaceVisitStorage();
    placeStorage.addPlaceVisit(UK);

    PlaceIndividualServlet servlet = new PlaceIndividualServlet(placeStorage, gson, placeService);

    when(request.getRequestURI()).thenReturn("/trips/abc123/placeVisits/abc");

    Reader stringReader = new StringReader("{placesApiPlaceId: 123, userMark: YES}");

    when(request.getReader()).thenReturn(new BufferedReader(stringReader));

    servlet.doPut(request, response);

    PlaceVisitModel updatedPlace = UK.toBuilder().setUserMark(PlaceVisitModel.UserMark.YES).build();

    assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
    assertThat(placeStorage.getPlaceVisit("abc123", "abc")).hasValue(updatedPlace);
  }

  @Test
  public void doDelete_badUri_setsBadRequestStatus() throws IOException {
    FakeHttpServletResponse response = new FakeHttpServletResponse();
    PlaceVisitStorage placeStorage = new InMemoryPlaceVisitStorage();
    PlaceIndividualServlet servlet = new PlaceIndividualServlet(placeStorage, gson, placeService);
    when(request.getRequestURI()).thenReturn("/trips/abc123/placesVisit/abc");

    servlet.doDelete(request, response);

    assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
  }

  @Test
  public void doDelete_notInStorage_setsNotFoundStatus() throws IOException {
    FakeHttpServletResponse response = new FakeHttpServletResponse();
    PlaceVisitStorage placeStorage = new InMemoryPlaceVisitStorage();
    PlaceIndividualServlet servlet = new PlaceIndividualServlet(placeStorage, gson, placeService);
    when(request.getRequestURI()).thenReturn("/trips/abc123/placeVisits/1234");

    servlet.doDelete(request, response);

    assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_NOT_FOUND);
  }

  @Test
  public void doDelete_validRequestInStorage_removesPlaceVisit()
      throws IOException, TripMeOutException {
    FakeHttpServletResponse response = new FakeHttpServletResponse();
    PlaceVisitStorage placeStorage = new InMemoryPlaceVisitStorage();
    placeStorage.addPlaceVisit(UK);
    PlaceIndividualServlet servlet = new PlaceIndividualServlet(placeStorage, gson, placeService);

    when(request.getRequestURI()).thenReturn("/trips/abc123/placeVisits/abc");

    servlet.doDelete(request, response);

    assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
    assertThat(placeStorage.getPlaceVisit("abc123", "abc")).isEmpty();
  }

  @Test
  public void doGet_badUri_setsBadRequestStatus() throws IOException {
    FakeHttpServletResponse response = new FakeHttpServletResponse();
    PlaceVisitStorage placeStorage = new InMemoryPlaceVisitStorage();
    PlaceIndividualServlet servlet = new PlaceIndividualServlet(placeStorage, gson, placeService);
    when(request.getRequestURI()).thenReturn("/trips/abc123/placesVisit/abc");

    servlet.doGet(request, response);

    assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
  }

  @Test
  public void doGet_invalidPlaceId_setsNotFoundStatus() throws IOException {
    FakeHttpServletResponse response = new FakeHttpServletResponse();
    PlaceVisitStorage placeStorage = new InMemoryPlaceVisitStorage();
    PlaceIndividualServlet servlet = new PlaceIndividualServlet(placeStorage, gson, placeService);
    when(request.getRequestURI()).thenReturn("/trips/abc123/placeVisits/hello");

    servlet.doGet(request, response);

    assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_NOT_FOUND);
  }

  @Test
  public void doGet_validRequestInStorage_getPlaceDetails() throws IOException, TripMeOutException {
    FakeHttpServletResponse response = new FakeHttpServletResponse();
    PlaceVisitStorage placeStorage = new InMemoryPlaceVisitStorage();
    placeStorage.addPlaceVisit(UK);

    PlaceIndividualServlet servlet = new PlaceIndividualServlet(placeStorage, gson, placeService);

    when(request.getRequestURI()).thenReturn("/trips/abc123/placeVisits/abc");

    servlet.doGet(request, response);

    PlaceVisitModel place = gson.fromJson(response.getResponseString(), PlaceVisitModel.class);

    assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
    assertThat(response.getResponseString()).isNotEmpty();

    assertThat(place).isEqualTo(UK);
  }
}
