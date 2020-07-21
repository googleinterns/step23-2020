package com.google.tripmeout.frontend.servlet;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.tripmeout.frontend.PlaceVisitModel;
import com.google.tripmeout.frontend.TripModel;
import com.google.tripmeout.frontend.error.BadUriException;
import com.google.tripmeout.frontend.error.EmptyRequestBodyException;
import com.google.tripmeout.frontend.serialization.GsonModelSerializationModule;
import com.google.tripmeout.frontend.servlet.ServletUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
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
  public void extractFromRequestBody_supplyListClass_returnsOptionalList()
      throws IOException, EmptyRequestBodyException {
    when(request.getReader())
        .thenReturn(new BufferedReader(new StringReader("['hello', 'my', 'name', 'is']")));

    List<String> extractedList = ServletUtil.extractFromRequestBody(
        request.getReader(), gson, new TypeToken<List<String>>() {}.getType());
    assertThat(extractedList).containsExactly("hello", "my", "name", "is");
  }

  @Test
  public void extractFromRequestBody_supplyTripObject_returnsOptionalTrip()
      throws IOException, EmptyRequestBodyException {
    TripModel testTrip = TripModel.builder()
                             .setId("abc123")
                             .setName("trip1")
                             .setUserId("a")
                             .setLocationLat(33.2)
                             .setLocationLong(-22.77)
                             .build();

    when(request.getReader())
        .thenReturn(new BufferedReader(new StringReader(gson.toJson(testTrip))));

    assertThat(ServletUtil.extractFromRequestBody(request.getReader(), gson, TripModel.class))
        .isEqualTo(testTrip);
  }

  @Test
  public void extractFromRequestBody_supplyTripObjectNotAllFields_returnsOptionalTrip()
      throws IOException, EmptyRequestBodyException {
    TripModel testTrip = TripModel.builder().setName("abc123").setUserId("a").build();

    when(request.getReader())
        .thenReturn(new BufferedReader(new StringReader(gson.toJson(testTrip))));

    assertThat(ServletUtil.extractFromRequestBody(request.getReader(), gson, TripModel.class))
        .isEqualTo(testTrip);
  }

  @Test
  public void extractFromRequestBody_missingName_throwsError()
      throws IOException, EmptyRequestBodyException {
    when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{id: a}")));
    assertThrows(JsonParseException.class,
        () -> ServletUtil.extractFromRequestBody(request.getReader(), gson, TripModel.class));
  }

  @Test
  public void extractFromRequestBody_supplyPlaceVisitObject_returnsOptionalPlaceVisitModel()
      throws IOException, EmptyRequestBodyException {
    when(request.getReader())
        .thenReturn(new BufferedReader(new StringReader(
            "{tripId: abc123, placesApiPlaceId: 123, placeName: trip1, userMark: YES}")));

    PlaceVisitModel testPlace1 = PlaceVisitModel.builder()
                                     .setTripId("abc123")
                                     .setPlacesApiPlaceId("123")
                                     .setPlaceName("trip1")
                                     .setUserMark(PlaceVisitModel.UserMark.YES)
                                     .build();

    assertThat(ServletUtil.extractFromRequestBody(request.getReader(), gson, PlaceVisitModel.class))
        .isEqualTo(testPlace1);
  }

  @Test
  public void extractFromRequestBody_emptyRequest_throwsError() throws IOException {
    when(request.getReader()).thenReturn(new BufferedReader(new StringReader("")));

    assertThrows(EmptyRequestBodyException.class,
        () -> ServletUtil.extractFromRequestBody(request.getReader(), gson, PlaceVisitModel.class));
  }

  @Test
  public void matchUriOrThrowError_noMatchesTripNamePattern_throwsError() {
    when(request.getRequestURI()).thenReturn("/trip/abc123");

    assertThrows(
        BadUriException.class, () -> ServletUtil.matchUriOrThrowError(request, TRIP_URI_PATTERN));
  }

  @Test
  public void matchUriOrThrowError_matchesTripNamePattern_matcherHasCorrectGroup()
      throws BadUriException {
    when(request.getRequestURI()).thenReturn("/trips/abc123");

    Matcher matcher = ServletUtil.matchUriOrThrowError(request, TRIP_URI_PATTERN);

    assertThat(matcher.matches()).isTrue();
    assertThat(matcher.group(1)).isEqualTo("abc123");
  }

  @Test
  public void matchUriOrThrowError_noMatchesTripAndPlaceNamePattern_throwsError() {
    when(request.getRequestURI()).thenReturn("/trip/abc123");

    assertThrows(
        BadUriException.class, () -> ServletUtil.matchUriOrThrowError(request, PLACE_URI_PATTERN));
  }

  @Test
  public void matchUriOrThrowError_matchesTripAndPlaceNamePattern_matcherHasCorrectGroups()
      throws BadUriException {
    when(request.getRequestURI()).thenReturn("/trips/abc123/places/123");

    Matcher matcher = ServletUtil.matchUriOrThrowError(request, PLACE_URI_PATTERN);

    assertThat(matcher.matches()).isTrue();
    assertThat(matcher.group(1)).isEqualTo("abc123");
    assertThat(matcher.group(2)).isEqualTo("123");
  }
}
