package com.google.tripmeout.serialization.testdata;

import com.google.common.io.CharStreams;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public final class TripTestDataAccessUtil {
  public static String getTripModelUnknownField() throws IOException {
    return getTestDataFileContents("TripModelUnknownField.json");
  }

  public static String getTripModelWellFormed() throws IOException {
    return getTestDataFileContents("TripModelWellFormed.json");
  }

  public static String getTripModelWithoutId() throws IOException {
    return getTestDataFileContents("TripModelWithoutId.json");
  }

  public static String getTripModelWithoutUserId() throws IOException {
    return getTestDataFileContents("TripModelWithoutUserId.json");
  }

  public static String getTripModelWithoutName() throws IOException {
    return getTestDataFileContents("TripModelWithoutName.json");
  }

  public static String getTripModelWithoutLatitude() throws IOException {
    return getTestDataFileContents("TripModelWithoutLatitude.json");
  }
  public static String getTripModelWithoutLongitude() throws IOException {
    return getTestDataFileContents("TripModelWithoutLongitude.json");
  }

  private static String getTestDataFileContents(String fileName) throws IOException {
    try (InputStream inputStream = TripTestDataAccessUtil.class.getResourceAsStream(fileName);

         Reader reader = new InputStreamReader(inputStream, "UTF-8")) {
      return CharStreams.toString(reader);
    }
  }

  private TripTestDataAccessUtil() {}
}
