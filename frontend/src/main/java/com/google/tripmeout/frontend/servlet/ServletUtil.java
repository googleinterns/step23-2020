package com.google.tripmeout.frontend.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.tripmeout.frontend.error.BadUriException;
import com.google.tripmeout.frontend.error.EmptyRequestBodyException;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;

/**
 * Static utilities that provide functionality that may be shared between servlets.
 */
public final class ServletUtil {
  public static <T> T extractFromRequestBody(Reader reader, Gson gson, Class<T> clazz)
      throws IOException, JsonParseException, EmptyRequestBodyException {
    T object = gson.fromJson(reader, clazz);
    if (object != null) {
      return object;
    }
    throw new EmptyRequestBodyException("Received empty request");
  }

  public static <T> T extractFromRequestBody(Reader reader, Gson gson, Type t)
      throws IOException, JsonParseException, EmptyRequestBodyException {
    T object = gson.fromJson(reader, t);
    if (object != null) {
      return object;
    }
    throw new EmptyRequestBodyException("Received empty request");
  }

  public static Matcher matchUriOrThrowError(HttpServletRequest request, Pattern pattern)
      throws BadUriException {
    String uri = request.getRequestURI();
    Matcher matcher = pattern.matcher(uri);
    if (matcher.matches()) {
      return matcher;
    }
    throw new BadUriException(
        String.format("URI '%s' does not match expected pattern '%s'", uri, pattern.toString()));
  }

  // Don't allow instantiation of the static util class.
  private ServletUtil() {}
}
