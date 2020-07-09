package com.google.tripmeout.serialization.testdata;

import com.google.common.io.CharStreams;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public final class TestDataAccessUtil {
  public static String getPlaceVisitWithUnkownField() throws IOException {
    return getTestDataFileContents("PlaceVisitWithUnknownField.json");
  }

  public static String getPlaceVisitWithoutPlaceId() throws IOException {
    return getTestDataFileContents("PlaceVisitWithoutPlaceId.json");
  }

  public static String getPlaceVisitWithoutName() throws IOException {
    return getTestDataFileContents("PlaceVisitWithoutName.json");
  }

  public static String getPlaceVisitWithoutTripId() throws IOException {
    return getTestDataFileContents("PlaceVisitWithoutTripId.json");
  }

  public static String getPlaceVisitWithoutLatitude() throws IOException {
    return getTestDataFileContents("PlaceVisitWithoutLatitude.json");
  }

  public static String getPlaceVisitWithoutLongitude() throws IOException {
    return getTestDataFileContents("PlaceVisitWithoutLongitude.json");
  }

  public static String getWellFormedPlaceVisit() throws IOException {
    return getTestDataFileContents("WellFormedPlaceVisit.json");
  }

  private static String getTestDataFileContents(String fileName) throws IOException {
    try (InputStream inputStream = TestDataAccessUtil.class.getResourceAsStream(fileName);
         Reader reader = new InputStreamReader(inputStream, "UTF-8")) {
      return CharStreams.toString(reader);
    }
  }

  private TestDataAccessUtil() {}
}
