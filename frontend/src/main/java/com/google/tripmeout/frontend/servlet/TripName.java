package com.google.tripmeout.frontend.servlet;

import com.google.auto.value.AutoValue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AutoValue
public abstract class TripName {
  private static Pattern TRIP_NAME_PATTERN = Pattern.compile(".*/trips/([^/]+)$");

  TripName() {}

  public abstract String id();

  @Override
  public final String toString() {
    return String.format("trips/%s", id());
  }

  public static TripName fromRequestUri(String uri) {
    Matcher matcher = TRIP_NAME_PATTERN.matcher(uri);
    if (matcher.matches()) {
      return new AutoValue_TripName(matcher.group(1));
    } else {
      throw new IllegalArgumentException(String.format(
          "URI '%s' does not match expected trip name pattern: '%s'", uri, TRIP_NAME_PATTERN));
    }
  }
}