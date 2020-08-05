package com.google.tripmeout.frontend.serialization;

import com.google.appengine.repackaged.com.google.api.client.util.Strings;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.google.tripmeout.frontend.LoginRequest;
import java.io.IOException;

public class GsonLoginRequestTypeAdapter extends TypeAdapter<LoginRequest> {
  private static final String REDIRECT_LINK_JSON_FIELD_NAME = "redirectLink";

  @Override
  public LoginRequest read(JsonReader reader) throws IOException {
    if (reader.peek() == JsonToken.NULL) {
      reader.nextNull();
      return null;
    }
    LoginRequest.Builder builder = LoginRequest.builder();
    reader.beginObject();
    if (reader.hasNext() && reader.nextName().equals(REDIRECT_LINK_JSON_FIELD_NAME)) {
      builder.setRedirectLink(reader.nextString());
    }
    reader.endObject();
    return builder.build();
  }

  @Override
  public void write(JsonWriter writer, LoginRequest loginRequest) throws IOException {
    if (loginRequest == null) {
      writer.nullValue();
      return;
    }
    writer.beginObject();
    if (!Strings.isNullOrEmpty(loginRequest.redirectLink())) {
      writer.name(REDIRECT_LINK_JSON_FIELD_NAME);
      writer.value(loginRequest.redirectLink());
    }
    writer.endObject();
  }
}