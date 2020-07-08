package com.google.tripmeout.frontend.serialization;

import com.google.appengine.repackaged.com.google.api.client.json.Json;
import com.google.common.base.Strings;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.google.tripmeout.frontend.PlaceVisitModel;
import java.io.IOException;

public class GsonPlaceVisitTypeAdapter extends TypeAdapter<PlaceVisitModel> {
  private static final String PLACE_VISIT_MODEL_PLACE_ID_JSON_FIELD_NAME = "placeId";
  private static final String PLACE_VISIT_MODEL_TRIP_ID_JSON_FIELD_NAME = "tripId";
  private static final String PLACE_VISIT_MODEL_NAME_JSON_FIELD_NAME = "name";
  private static final String PLACE_VISIT_MODEL_USER_MARK_JSON_FIELD_NAME = "userMark";
  private static final String PLACE_VISIT_MODEL_LATITUDE_JSON_FIELD_NAME = "latitude";
  private static final String PLACE_VISIT_MODEL_LONGITUDE_JSON_FIELD_NAME = "longitude";

  @Override
  public PlaceVisitModel read(JsonReader reader) throws IOException {
    if (reader.peek() == JsonToken.NULL) {
      reader.nextNull();
      return null;
    }
    PlaceVisitModel.Builder placeBuilder = PlaceVisitModel.builder();
    reader.beginObject();
    while (reader.hasNext()) {
      JsonToken token = reader.peek();
      if (token.equals(JsonToken.NAME)) {
        String name = reader.nextName();
        switch (name) {
          case PLACE_VISIT_MODEL_PLACE_ID_JSON_FIELD_NAME:
            placeBuilder.setPlaceId(reader.nextString());
            break;
          case PLACE_VISIT_MODEL_NAME_JSON_FIELD_NAME:
            placeBuilder.setName(reader.nextString());
            break;
          case PLACE_VISIT_MODEL_TRIP_ID_JSON_FIELD_NAME:
            placeBuilder.setTripId(reader.nextString());
            break;
          case PLACE_VISIT_MODEL_USER_MARK_JSON_FIELD_NAME:
            placeBuilder.setUserMark(reader.nextString());
          case PLACE_VISIT_MODEL_LATITUDE_JSON_FIELD_NAME:
            placeBuilder.setLatitude(reader.nextDouble());
            break;
          case PLACE_VISIT_MODEL_LONGITUDE_JSON_FIELD_NAME:
            placeBuilder.setLongitude(reader.nextDouble());
            break;
          default:
            throw new JsonParseException(
                String.format("Unknown field name %s for type Trip Model", name));
        }
      }
    }
    reader.endObject();
    return placeBuilder.build();
  }

  @Override
  public void write(JsonWriter writer, PlaceVisitModel place) throws IOException {
    if (place == null) {
      writer.nullValue();
      return;
    }
    writer.beginObject();
    writeUnlessNullOrEmpty(writer, PLACE_VISIT_MODEL_PLACE_ID_JSON_FIELD_NAME, place.placeId());
    writeUnlessNullOrEmpty(writer, PLACE_VISIT_MODEL_NAME_JSON_FIELD_NAME, place.name());
    writeUnlessNullOrEmpty(writer, PLACE_VISIT_MODEL_TRIP_ID_JSON_FIELD_NAME, place.tripId());
    writeUnlessNullOrEmpty(writer, PLACE_VISIT_MODEL_USER_MARK_JSON_FIELD_NAME, place.userMark());
    writeUnlessNullOrEmpty(writer, PLACE_VISIT_MODEL_LATITUDE_JSON_FIELD_NAME, place.latitude());
    writeUnlessNullOrEmpty(writer, PLACE_VISIT_MODEL_LONGITUDE_JSON_FIELD_NAME, place.longitude());
    writer.endObject();
  }

  private static void writeUnlessNullOrEmpty(JsonWriter writer, String name, String value)
      throws IOException {
    if (Strings.isNullOrEmpty(value)) {
      writer.name(name);
      writer.value(value);
    }
  }

  private static void writeUnlessNullOrEmpty(JsonWriter writer, String name, double value)
      throws IOException {
    if (Strings.isNullOrEmpty(value)) {
      writer.name(name);
      writer.value(value);
    }
  }
}
