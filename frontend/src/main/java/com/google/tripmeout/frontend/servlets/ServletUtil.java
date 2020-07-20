package com.google.tripmeout.frontend.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.tripmeout.frontend.error.WrongFormatUriException;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;

/**
 * Static utilities that provide functionality that may be shared between servlets.
 */
public final class ServletUtil {

  public static <T> Optional<T> extractFromRequestBody(HttpServletRequest request, Gson gson,
      Class<T> clazz) throws IOException, JsonParseException {
    return Optional.ofNullable(gson.fromJson(request.getReader(), clazz));
  }

  public static <T> Optional<T> extractFromRequestBody(
      HttpServletRequest request, Gson gson, Type t) throws IOException, JsonParseException {
    return Optional.ofNullable(gson.fromJson(request.getReader(), t));
  }

  public static Matcher matchUriOrThrowError(HttpServletRequest request, Pattern pattern)
      throws WrongFormatUriException {
    String uri = request.getRequestURI();
    Matcher matcher = pattern.matcher(uri);
    if (matcher.matches()) {
      return matcher;
    } else {
      throw new WrongFormatUriException(
          String.format("URI '%s' does not match expected pattern '%s'", uri, pattern.toString()));
    }
  }

  // Don't allow instantiation of the static util class.
  private ServletUtil() {}
}
