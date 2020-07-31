package com.google.tripmeout.frontend.serialization;

import com.google.common.base.Strings;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.google.tripmeout.frontend.AuthenticationInfoModel;
import java.io.IOException;

public class GsonAuthenticationInfoModelTypeAdapter extends TypeAdapter<AuthenticationInfoModel> {
  private static final String RETURN_LINK_JSON_FIELD_NAME = "returnLink";
  private static final String LOGGED_IN_JSON_FIELD_NAME = "loggedIn";

  @Override
  public AuthenticationInfoModel read(JsonReader reader) throws IOException {
    if (reader.peek() == JsonToken.NULL) {
      reader.nextNull();
      return null;
    }
    AuthenticationInfoModel.Builder authenticationBuilder = AuthenticationInfoModel.builder();
    reader.beginObject();
    while (reader.hasNext()) {
      String name = reader.nextName();
      switch (name) {
        case RETURN_LINK_JSON_FIELD_NAME:
          authenticationBuilder.setreturnLink(reader.nextString());
          break;
        case LOGGED_IN_JSON_FIELD_NAME:
          authenticationBuilder.setloggedIn(reader.nextString());
          break;
        default:
          throw new JsonParseException(
              String.format("Unknown field name %s for type Authentication Model", name));
      }
    }
    reader.endObject();
    return authenticationBuilder.build();
  }

  @Override
  public void write(JsonWriter writer, AuthenticationInfoModel authenticator) throws IOException {
    if (authenticator == null) {
      writer.nullValue();
      return;
    }
    writer.beginObject();
    writeUnlessNullOrEmpty(writer, RETURN_LINK_JSON_FIELD_NAME, authenticator.returnLink());
    writeUnlessNullOrEmpty(writer, LOGGED_IN_JSON_FIELD_NAME, authenticator.loggedIn());
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