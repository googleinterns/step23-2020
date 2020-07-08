package com.google.tripmeout.frontend.serialization;

import com.google.appengine.repackaged.com.google.api.client.json.Json;
import com.google.common.base.Strings;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import com.google.tripmeout.frontend.TripModel;


public class GsonTripModelTypeAdapter extends TypeAdapter<TripModel> {
    
    private static final String TRIP_MODEL_ID_JSON_FIELD_NAME = "id";
    private static final String TRIP_MODEL_NAME_JSON_FIELD_NAME = "name";
    private static final String TRIP_MODEL_USER_ID_JSON_FIELD_NAME = "userId";
    private static final String TRIP_MODEL_LATITUDE_JSON_FIELD_NAME = "locationLat";
    private static final String TRIP_MODEL_LONGITUDE_JSON_FIELD_NAME = "locationLong";

    @Override
    public TripModel read(JsonReader reader) throws IOException{

        if(reader.peek() == JsonToken.NULL){
            reader.nextNull();
            return null;
        }
        TripModel.Builder tripBuilder = TripModel.builder();
        reader.beginObject();
        while(reader.hasNext()){
            JsonToken token = reader.peek();
            if(token.equals(JsonToken.NAME)){
                String name = reader.nextName();
                switch(name){
                    case TRIP_MODEL_ID_JSON_FIELD_NAME:
                    tripBuilder.setId(reader.nextString());
                    break;
                    case TRIP_MODEL_NAME_JSON_FIELD_NAME:
                    tripBuilder.setName(reader.nextString());
                    break;
                    case TRIP_MODEL_USER_ID_JSON_FIELD_NAME:
                    tripBuilder.setUserId(reader.nextString());
                    break;
                    case TRIP_MODEL_LATITUDE_JSON_FIELD_NAME:
                    tripBuilder.setLocationLat(reader.nextDouble());
                    break;
                    case TRIP_MODEL_LONGITUDE_JSON_FIELD_NAME:
                    tripBuilder.setLocationLong(reader.nextDouble());
                    break;
                    default:
                    throw new JsonParseException(
                        String.format("Unknown field name %s for type Trip Model", name));
                }
            }
        }
        reader.endObject();
        return tripBuilder.build();

    }

    @Override
    public void write(JsonWriter writer, TripModel trip) throws IOException{
        if(trip == null){
            writer.nullValue();
            return;
        }
        writer.beginObject();
        writeUnlessNullOrEmpty(writer,TRIP_MODEL_ID_JSON_FIELD_NAME,trip.id());
        writeUnlessNullOrEmpty(writer, TRIP_MODEL_NAME_JSON_FIELD_NAME, trip.name());
        writeUnlessNullOrEmpty(writer, TRIP_MODEL_LATITUDE_JSON_FIELD_NAME, trip.locationLat());
        writer.endObject();
    }

     private static void writeUnlessNullOrEmpty(JsonWriter writer, String name, String value)
      throws IOException {
    if (Strings.isNullOrEmpty(value)) {
      writer.name(name);
      writer.value(value);
    }
  }

  private static void writeUnlessNullOrEmpty(JsonWriter writer, String name, Double value)
      throws IOException {
    if (Strings.isNullOrEmpty(value)) {
      writer.name(name);
      writer.value(value);
    }
  }

}