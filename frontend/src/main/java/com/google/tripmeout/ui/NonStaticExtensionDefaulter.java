package com.google.tripmeout.ui;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.io.CharStreams;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A servlet that always serves index.html regardless of the requested path. This allows the Flutter
 * app to always be served, which allows the internal UI page routing logic to take care of the
 * rest.
 */
public class NonStaticExtensionDefaulter extends HttpFilter {
  private static final long serialVersionUID = 0L;

  private static final String EXTENSIONS_INIT_PARAM_KEY = "extensions";
  private static final String IGNORE_PREFIX_REGEX_PARAM_KEY = "ignore-prefix-regex";
  private static final String DEFAULT_CONTENT_INIT_PARAM_KEY = "default-content";
  private static final String DEFAULT_DEFAULT_CONTENT = "/index.html";
  private static final Splitter EXTENSIONS_PARAM_SPLITTER = Splitter.on(',').omitEmptyStrings();

  private List<String> extensions = ImmutableList.of();
  private Optional<Pattern> ignorePrefixPattern = Optional.empty();

  private String defaultContent;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    String extensionsParam = filterConfig.getInitParameter(EXTENSIONS_INIT_PARAM_KEY);
    if (!Strings.isNullOrEmpty(extensionsParam)) {
      ImmutableList.Builder<String> extensionsBuilder = ImmutableList.builder();
      for (String extension : EXTENSIONS_PARAM_SPLITTER.split(extensionsParam)) {
        extensionsBuilder.add(extension);
      }
      extensions = extensionsBuilder.build();
    }
    String ignorePrefixRegex = filterConfig.getInitParameter(IGNORE_PREFIX_REGEX_PARAM_KEY);
    if (!Strings.isNullOrEmpty(ignorePrefixRegex)) {
      ignorePrefixPattern = Optional.of(Pattern.compile(ignorePrefixRegex));
    }
    String defaultContentPath = filterConfig.getInitParameter(DEFAULT_CONTENT_INIT_PARAM_KEY);
    if (Strings.isNullOrEmpty(defaultContentPath)) {
      defaultContentPath = DEFAULT_DEFAULT_CONTENT;
    }
    try (InputStream inputStream =
             filterConfig.getServletContext().getResourceAsStream(defaultContentPath)) {
      // getResourceAsStream returns a null stream if not found...
      if (inputStream == null) {
        throw new ServletException(
            String.format("Unable to read default contents file %s", defaultContentPath));
      }
      try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
        defaultContent = CharStreams.toString(reader);
      }
    } catch (IOException e) {
      throw new ServletException(String.format("Error closing resource %s", defaultContentPath), e);
    }
  }

  @Override
  public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    if (extensions.stream().anyMatch(request.getRequestURI()::endsWith)
        || ignorePrefixPattern.map(p -> p.matcher(request.getRequestURI()).matches())
               .orElse(false)) {
      chain.doFilter(request, response);
    } else {
      response.setContentType("text/html; charset=utf-8");
      response.getWriter().print(defaultContent);
    }
  }
}
