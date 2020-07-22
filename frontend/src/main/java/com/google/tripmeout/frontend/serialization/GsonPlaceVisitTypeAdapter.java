package com.google.tripmeout.frontend.serialization;

import com.google.common.base.Strings;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.google.tripmeout.frontend.PlaceVisitModel;
import com.google.tripmeout.frontend.PlaceVisitModel.UserMark;
import java.io.IOException;

public class GsonPlaceVisitTypeAdapter extends TypeAdapter<PlaceVisitModel> {
  private static final String PLACE_ID_JSON_FIELD_NAME = "placesApiPlaceId";
  private static final String UUID_JSON_FIELD_NAME = "id";
  private static final String TRIP_ID_JSON_FIELD_NAME = "tripId";
  private static final String NAME_JSON_FIELD_NAME = "placeName";
  private static final String USER_MARK_JSON_FIELD_NAME = "userMark";

  @Override
  public PlaceVisitModel read(JsonReader reader) throws IOException {
    if (reader.peek() == JsonToken.NULL) {
      reader.nextNull();
      return null;
    }
    PlaceVisitModel.Builder placeBuilder = PlaceVisitModel.builder();
    reader.beginObject();
    while (reader.hasNext()) {
      String name = reader.nextName();
      switch (name) {
        case UUID_JSON_FIELD_NAME:
          placeBuilder.setId(reader.nextString());
          break;
        case PLACE_ID_JSON_FIELD_NAME:
          placeBuilder.setPlacesApiPlaceId(reader.nextString());
          break;
        case NAME_JSON_FIELD_NAME:
          placeBuilder.setPlaceName(reader.nextString());
          break;
        case TRIP_ID_JSON_FIELD_NAME:
          placeBuilder.setTripId(reader.nextString());
          break;
        case USER_MARK_JSON_FIELD_NAME:
          UserMark userMark = UserMark.valueOf(reader.nextString());
          placeBuilder.setUserMark(userMark);
          break;
        default:
          throw new JsonParseException(
              String.format("Unknown field name %s for type PlaceVisitModel", name));
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
    writeUnlessNullOrEmpty(writer, UUID_JSON_FIELD_NAME, place.id());
    writeUnlessNullOrEmpty(writer, PLACE_ID_JSON_FIELD_NAME, place.placesApiPlaceId());
    writeUnlessNullOrEmpty(writer, NAME_JSON_FIELD_NAME, place.placeName());
    writeUnlessNullOrEmpty(writer, TRIP_ID_JSON_FIELD_NAME, place.tripId());
    writer.name(USER_MARK_JSON_FIELD_NAME);
    writer.value(place.userMark().toString());
    writer.endObject();
  }

  private static void writeUnlessNullOrEmpty(JsonWriter writer, String name, String value)
      throws IOException {
    if (!Strings.isNullOrEmpty(value)) {
      writer.name(name);
      writer.value(value);
    }
  }
}
