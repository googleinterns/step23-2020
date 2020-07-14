package com.google.tripmeout.frontend.servlets;

import com.google.common.base.Splitter;
import com.google.common.collect.Streams;
import com.google.common.flogger.FluentLogger;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import java.io.IOException;
import java.io.Reader;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;

/**
 * Static utilities that provide functionality that may be shared between servlets.
 */
public final class ServletUtil {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  public static <T> Optional<T> extractFromRequestBody(
      HttpServletRequest request, Gson gson, Class<T> clazz) throws IOException {
    try (Reader reader = request.getReader()) {
      return Optional.ofNullable(gson.fromJson(reader, clazz));
    } catch (JsonParseException e) {
      logger.atWarning().withCause(e).log(
          "received exception while attempting to parse json from reader!");
      return Optional.empty();
    }
  }

  public static Matcher parseUri(HttpServletRequest request, Pattern pattern) {
    String uri = request.getRequestURI();
    Matcher matcher = pattern.matcher(uri);
    if (matcher.matches()) {
      return matcher;
    } else {
      throw new IllegalArgumentException(
          String.format("URI '%s' does not match expected pattern", uri));
    }
  }

  // Don't allow instantiation of the static util class.
  private ServletUtil() {}
}
