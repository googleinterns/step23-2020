package com.google.tripmeout.frontend.servlet;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth8.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.gson.JsonParseException;
import com.google.tripmeout.frontend.PlaceVisitModel;
import com.google.tripmeout.frontend.TripModel;
import com.google.tripmeout.frontend.serialization.GsonModelSerializationModule;
import com.google.tripmeout.frontend.servlets.ServletUtil;
import com.google.tripmeout.frontend.error.WrongFormatUriException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;

@RunWith(JUnit4.class)
public class ServletUtilTest {
  @Mock HttpServletRequest request;
  private Gson gson;

  private final Pattern TRIP_URI_PATTERN = Pattern.compile(".*/trips/([^/]+)");
  private final Pattern PLACE_URI_PATTERN = Pattern.compile(".*/trips/([^/]+)/places/([^/]+)");

  @Before
  public void setup() {
    initMocks(this);
    Injector injector = Guice.createInjector(new GsonModelSerializationModule());
    this.gson = injector.getInstance(Gson.class);
  }

  @Test
  public void extractFromRequestBody_supplyListClass_returnsOptionalList() throws IOException {
    when(request.getReader())
        .thenReturn(new BufferedReader(new StringReader("['hello', 'my', 'name', 'is']")));

    Optional<List<String>> extractedList = ServletUtil.extractFromRequestBody(
        request, gson, new TypeToken<List<String>>() {}.getType());
    assertThat(extractedList).isPresent();
    assertThat(extractedList.get()).containsExactly("hello", "my", "name", "is");
  }

  @Test
  public void extractFromRequestBody_supplyTripObject_returnsOptionalTrip() throws IOException {
    when(request.getReader())
        .thenReturn(new BufferedReader(new StringReader(
            "{id: abc123, name: trip1, userId: a, locationLat: 33.2, locationLong: -22.77}")));

    TripModel testTrip = TripModel.builder()
                             .setId("abc123")
                             .setName("trip1")
                             .setUserId("a")
                             .setLocationLat(33.2)
                             .setLocationLong(-22.77)
                             .build();

    assertThat(ServletUtil.extractFromRequestBody(request, gson, TripModel.class))
        .hasValue(testTrip);
  }

  @Test
  public void extractFromRequestBody_supplyTripObjectNotAllFields_returnsOptionalTrip()
      throws IOException {
    when(request.getReader())
        .thenReturn(new BufferedReader(new StringReader("{name: abc123, userId: a}")));

    TripModel testTrip = TripModel.builder().setName("abc123").setUserId("a").build();

    assertThat(ServletUtil.extractFromRequestBody(request, gson, TripModel.class))
        .hasValue(testTrip);
  }

  @Test
  public void extractFromRequestBody_missingName_returnsOptionalTrip() throws IOException {
    when(request.getReader())
        .thenReturn(new BufferedReader(
            new StringReader("{name: trip1, id: a, locationLat: 33.2, locationLong: -22.77}")));
    assertThrows(JsonParseException.class,
        () -> ServletUtil.extractFromRequestBody(request, gson, TripModel.class));
  }

  @Test
  public void extractFromRequestBody_supplyPlaceVisitObject_returnsOptionalPlaceVisitModel()
      throws IOException {
    when(request.getReader())
        .thenReturn(new BufferedReader(new StringReader(
            "{tripId: abc123, placeId: 123, name: trip1, userMark: YES, latitude: 33.2, longitude: -22.77}")));

    PlaceVisitModel testPlace1 = PlaceVisitModel.builder()
                                     .setTripId("abc123")
                                     .setPlaceId("123")
                                     .setName("trip1")
                                     .setUserMark(PlaceVisitModel.UserMark.YES)
                                     .setLatitude(33.2)
                                     .setLongitude(-22.77)
                                     .build();

    assertThat(ServletUtil.extractFromRequestBody(request, gson, PlaceVisitModel.class))
        .hasValue(testPlace1);
  }

  @Test
  public void matchUriOrThrowError_noMatchesTripNamePattern_throwsError() {
    when(request.getRequestURI()).thenReturn("/trip/abc123");

    assertThrows(WrongFormatUriException.class,
        () -> ServletUtil.matchUriOrThrowError(request, TRIP_URI_PATTERN));
  }

  @Test
  public void matchUriOrThrowError_matchesTripNamePattern_matcherHasCorrectGroup() throws WrongFormatUriException {
    when(request.getRequestURI()).thenReturn("/trips/abc123");

    Matcher matcher = ServletUtil.matchUriOrThrowError(request, TRIP_URI_PATTERN);

    assertThat(matcher.matches()).isTrue();
    assertThat(matcher.group(1)).isEqualTo("abc123");
  }

  @Test
  public void matchUriOrThrowError_noMatchesTripAndPlaceNamePattern_throwsError() {
    when(request.getRequestURI()).thenReturn("/trip/abc123");

    assertThrows(WrongFormatUriException.class,
        () -> ServletUtil.matchUriOrThrowError(request, PLACE_URI_PATTERN));
  }

  @Test
  public void matchUriOrThrowError_matchesTripAndPlaceNamePattern_matcherHasCorrectGroups() throws WrongFormatUriException {
    when(request.getRequestURI()).thenReturn("/trips/abc123/places/123");

    Matcher matcher = ServletUtil.matchUriOrThrowError(request, PLACE_URI_PATTERN);

    assertThat(matcher.matches()).isTrue();
    assertThat(matcher.group(1)).isEqualTo("abc123");
    assertThat(matcher.group(2)).isEqualTo("123");
  }
}
