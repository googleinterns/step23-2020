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
        System.out.println("Property "+reader.peek());
        String name = reader.nextName();
        switch (name) {
          case PLACE_VISIT_MODEL_PLACE_ID_JSON_FIELD_NAME:
           System.out.println("Place ID "+reader.peek());
            placeBuilder.setPlaceId(reader.nextString());
            break;
          case PLACE_VISIT_MODEL_NAME_JSON_FIELD_NAME:
           System.out.println("Name "+reader.peek());
            placeBuilder.setName(reader.nextString());
            break;
          case PLACE_VISIT_MODEL_TRIP_ID_JSON_FIELD_NAME:
           System.out.println("Trip ID "+reader.peek());
            placeBuilder.setTripId(reader.nextString());
            break;
          case PLACE_VISIT_MODEL_USER_MARK_JSON_FIELD_NAME:
           System.out.println("User Mark "+reader.peek());
            placeBuilder.setUserMark(reader.nextString());
            break;
          case PLACE_VISIT_MODEL_LATITUDE_JSON_FIELD_NAME:
            System.out.println("Latitude "+reader.peek());
            placeBuilder.setLatitude(reader.nextDouble());
            break;
          case PLACE_VISIT_MODEL_LONGITUDE_JSON_FIELD_NAME:
            System.out.println("Longitude "+reader.peek());
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
    writer.name(PLACE_VISIT_MODEL_LATITUDE_JSON_FIELD_NAME);
    System.out.println("Ran Writer");
    writer.value(place.latitude());
    writer.name(PLACE_VISIT_MODEL_LONGITUDE_JSON_FIELD_NAME);
    writer.value(place.longitude());
    System.out.println("Ran Finished Writer");
    writer.endObject();
  }

  private static void writeUnlessNullOrEmpty(JsonWriter writer, String name, String value)
      throws IOException {
    if (Strings.isNullOrEmpty(value)) {
      writer.name(name);
      writer.value(value);
    }
  }

  
}
