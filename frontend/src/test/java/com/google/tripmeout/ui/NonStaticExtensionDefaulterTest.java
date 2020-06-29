package com.google.tripmeout.ui;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import com.google.common.collect.ImmutableMap;
import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class NonStaticExtensionDefaulterTest {

  @Mock FilterConfig config;
  @Mock ServletContext context;
  @Mock HttpServletRequest request;
  @Mock HttpServletResponse response;
  @Mock FilterChain chain;

  @Before
  public void before() {
    initMocks(this);
  }

  @Test
  public void doFilter_uriDoesNotEndWithExtension_servesDefaultContent() throws Exception {
    setupConfig(
        Optional.of("js,css,png"),
        Optional.of("random-file"),
        ImmutableMap.of("random-file", "random-contents"));
    NonStaticExtensionDefaulter nonStaticExtensionDefaulter = new NonStaticExtensionDefaulter();
    nonStaticExtensionDefaulter.init(config);
    StringWriter responseCapture = new StringWriter();
    when(response.getWriter()).thenReturn(new PrintWriter(responseCapture));
    when(request.getRequestURI()).thenReturn("/some/random/path.jpg");

    nonStaticExtensionDefaulter.doFilter(request, response, chain);

    assertThat(responseCapture.toString()).isEqualTo("random-contents");
    verify(response).setContentType("text/html; charset=utf-8");
  }

  @Test
  public void doFilter_uriDoesEndWithExtension_callsFilterChain() throws Exception {
    setupConfig(
        Optional.of("js,css,png"),
        Optional.of("random-file"),
        ImmutableMap.of("random-file", "random-contents"));
    NonStaticExtensionDefaulter nonStaticExtensionDefaulter = new NonStaticExtensionDefaulter();
    nonStaticExtensionDefaulter.init(config);
    StringWriter responseCapture = new StringWriter();
    when(response.getWriter()).thenReturn(new PrintWriter(responseCapture));
    when(request.getRequestURI()).thenReturn("random-file.js");

    nonStaticExtensionDefaulter.doFilter(request, response, chain);

    verify(chain).doFilter(request, response);
    assertThat(responseCapture.toString()).isEmpty();
  }

  @Test
  public void doFilter_contentsDefaultToRootIndex() throws Exception {
    setupConfig(
        Optional.of("js,css,png"),
        Optional.empty(),
        ImmutableMap.of("random-file", "random-contents", "/index.html", "index-contents"));
    NonStaticExtensionDefaulter nonStaticExtensionDefaulter = new NonStaticExtensionDefaulter();
    nonStaticExtensionDefaulter.init(config);
    StringWriter responseCapture = new StringWriter();
    when(response.getWriter()).thenReturn(new PrintWriter(responseCapture));
    when(request.getRequestURI()).thenReturn("/some/random/path.jpg");

    nonStaticExtensionDefaulter.doFilter(request, response, chain);

    assertThat(responseCapture.toString()).isEqualTo("index-contents");
    verify(response).setContentType("text/html; charset=utf-8");
  }

  @Test
  public void init_defaultContentReturnsNull_throwsServletException() throws Exception {
    setupConfig(Optional.of("js,css,png"), Optional.of("does-not-exist"), ImmutableMap.of());
    NonStaticExtensionDefaulter nonStaticExtensionDefaulter = new NonStaticExtensionDefaulter();

    assertThrows(ServletException.class, () -> nonStaticExtensionDefaulter.init(config));
  }

  // This is kinda bad because it REALLLLY depends on implementation...
  private void setupConfig(
      Optional<String> extensionParam,
      Optional<String> defaultContentParam,
      Map<String, String> resourcesToContents) {
    extensionParam.ifPresent(ep -> when(config.getInitParameter("extensions")).thenReturn(ep));
    defaultContentParam.ifPresent(
        dcp -> when(config.getInitParameter("default-content")).thenReturn(dcp));
    when(config.getServletContext()).thenReturn(context);
    for (Map.Entry<String, String> resourceToContents : resourcesToContents.entrySet()) {
      when(context.getResourceAsStream(resourceToContents.getKey()))
          .thenReturn(
              new ByteArrayInputStream(
                  resourceToContents.getValue().getBytes(StandardCharsets.UTF_8)));
    }
  }
}
