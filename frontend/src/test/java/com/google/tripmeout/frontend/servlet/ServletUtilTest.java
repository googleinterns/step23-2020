package com.google.tripmeout.frontend.servlet;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth8.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.io.PatternFilenameFilter;
import com.google.gson.Gson;
import com.google.tripmeout.frontend.PlaceVisitModel;
import com.google.tripmeout.frontend.TripModel;
import com.google.tripmeout.frontend.error.TripMeOutException;
import com.google.tripmeout.frontend.servlet.FakeHttpServletResponse;
import com.google.tripmeout.frontend.servlets.ServletUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class ServletUtilTest {
    @Mock HttpServletRequest request;
    private Gson gson;

    private final Pattern URI_PATTERN = Pattern.compile(".*/trips/([^/]+)");

    @Before
    public void setup() {
        initMocks(this);
        this.gson = new Gson();
    }

    @Test
    public void extractFromRequestBody_supplyListClass_returnsOptionalList() throws IOException {
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("['hello', 'my', 'name', 'is']")));

        Optional<List> extractedList = ServletUtil.extractFromRequestBody(request, gson, List.class);
        assertThat(extractedList).isPresent();
        assertThat(extractedList.get()).containsExactly("hello", "my", "name", "is");
    }

    @Test
    public void extractFromRequestBody_supplyTripObject_returnsOptionalTrip() throws IOException {
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{id: abc1234, name: trip1, userId: a, locationLat: 33, locationLong: -22")));

        TripModel testTrip = TripModel.builder()
          .setId("abc123")
          .setName("trip1")
          .setUserId("a")
          .setLocationLat(33)
          .setLocationLong(-22)
          .build();

        assertThat(ServletUtil.extractFromRequestBody(request, gson, TripModel.class)).hasValue(testTrip);
    }

    @Test
    public void parseUri_noMatches_returnsEmptyMatcher() {
        when(request.getRequestURI()).thenReturn("http://tripmeout.com/trip/abc123");

        assertThrows(IllegalArgumentException.class, () -> ServletUtil.parseUri(request, URI_PATTERN));
    }

    @Test
    public void parseUri_matches_matcherHasCorrectGroup() {
        when(request.getRequestURI()).thenReturn("http://tripmeout.com/trips/abc123");

        Matcher matcher = ServletUtil.parseUri(request, URI_PATTERN);

        assertThat(matcher.matches()).isTrue();
        assertThat(matcher.group(1)).isEqualTo("abc123");
    }
}