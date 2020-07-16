package com.google.tripmeout.frontend.servlet;

import static com.google.common.base.Preconditions.checkState;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import com.google.common.io.ByteSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import javax.annotation.Nullable;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet response to be used for testing. Not thread-safe.
 *
 * Example use:
 * <pre>
 * @Test
 * public void testMyServlet() throws Exception {
 *   MyServlet servlet = new MyServlet();
 *   HttpServletRequest request = ...;
 *   FakeHttpServletResponse response = new FakeHttpServletResponse();
 *   servlet.doGet(request, response);
 *   assertThat(response.getResponseString()).isEqualTo("expected-response");
 *   assertThat(response.getStatus()).isEqualTo(SC_OK);
 *   assertThat(response.getCookie("my-cookie")).isEqualTo("cookie-value");
 *   assertThat(response.getHeaders("repeated-header"))
 *      .containsExactlyElementsIn(ImmutableList.of("value-1", "value-2));
 * }
 * </pre>
 */
public class FakeHttpServletResponse implements HttpServletResponse {
  private final ByteArrayOutputStream output = new ByteArrayOutputStream();
  private final List<Cookie> cookies = new ArrayList<>();
  private final Multimap<String, String> headers = HashMultimap.create();

  private int statusCode;
  private String statusMessage;
  private long contentLength;
  private String contentType;

  private ServletOutputStream outputStream;
  private PrintWriter printWriter;

  public String getResponseString() throws IOException {
    return ByteSource.wrap(output.toByteArray()).asCharSource(StandardCharsets.UTF_8).read();
  }

  public List<Cookie> getCookies() {
    return cookies;
  }

  public Multimap<String, String> getHeaders() {
    return headers;
  }

  @Nullable
  public String getStatusMessage() {
    return statusMessage;
  }

  public long getContentLength() {
    return contentLength;
  }

  @Override
  public void addCookie(Cookie cookie) {
    cookies.add(cookie);
  }

  @Override
  public boolean containsHeader(String name) {
    return headers.containsKey(name);
  }

  @Override
  public String encodeURL(String s) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String encodeRedirectURL(String s) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String encodeUrl(String s) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String encodeRedirectUrl(String s) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void sendError(int statusCode, String statusMessage) throws IOException {
    this.statusCode = statusCode;
    this.statusMessage = statusMessage;
  }

  @Override
  public void sendError(int statusCode) throws IOException {
    this.statusCode = statusCode;
    output.reset();
  }

  @Override
  public void sendRedirect(String s) throws IOException {
    statusCode = SC_FOUND;
    output.reset();
  }

  @Override
  public void setDateHeader(String name, long timestampMs) {
    headers.replaceValues(name, ImmutableList.of(timestampToHeaderDate(timestampMs)));
  }

  @Override
  public void addDateHeader(String name, long timestampMs) {
    headers.put(name, timestampToHeaderDate(timestampMs));
  }

  @Override
  public void setHeader(String name, String value) {
    headers.replaceValues(name, ImmutableList.of(value));
  }

  @Override
  public void addHeader(String name, String value) {
    headers.put(name, value);
  }

  @Override
  public void setIntHeader(String name, int value) {
    headers.replaceValues(name, ImmutableList.of(String.valueOf(value)));
  }

  @Override
  public void addIntHeader(String name, int value) {
    headers.put(name, String.valueOf(value));
  }

  @Override
  public void setStatus(int statusCode) {
    this.statusCode = statusCode;
  }

  @Override
  public void setStatus(int statusCode, String message) {
    this.statusCode = statusCode;
    this.statusMessage = message;
  }

  @Override
  public int getStatus() {
    return statusCode;
  }

  @Override
  public String getHeader(String name) {
    if (!headers.containsKey(name)) {
      return null;
    }
    return Iterables.getFirst(headers.get(name), null);
  }

  @Override
  public Collection<String> getHeaders(String s) {
    return headers.get(s);
  }

  @Override
  public Collection<String> getHeaderNames() {
    return headers.keySet();
  }

  @Override
  public String getCharacterEncoding() {
    return StandardCharsets.UTF_8.toString();
  }

  @Override
  public String getContentType() {
    return contentType;
  }

  @Override
  public ServletOutputStream getOutputStream() throws IOException {
    checkState(printWriter == null, "getWriter() already called");
    if (outputStream == null) {
      outputStream = new ServletOutputStream() {
        @Override
        public boolean isReady() {
          return true;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {}

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
          output.write(b, off, len);
        }

        @Override
        public void write(int b) throws IOException {
          output.write(b);
        }
      };
    }
    return outputStream;
  }

  @Override
  public PrintWriter getWriter() throws IOException {
    checkState(outputStream == null, "getOutputStream() already called");
    if (printWriter == null) {
      printWriter = new PrintWriter(
          new OutputStreamWriter(output, StandardCharsets.UTF_8), /* autoFlush= */ true);
    }
    return printWriter;
  }

  @Override
  public void setCharacterEncoding(String s) {
    // Ignored. The output always uses UTF-8.
  }

  @Override
  public void setContentLength(int length) {
    contentLength = length;
  }

  @Override
  public void setContentLengthLong(long length) {
    contentLength = length;
  }

  @Override
  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  @Override
  public void setBufferSize(int i) {
    // Ignored.
  }

  @Override
  public int getBufferSize() {
    return 0;
  }

  @Override
  public void flushBuffer() throws IOException {
    // Do nothing. We're effectively flushing writes immediately anyway.
  }

  @Override
  public void resetBuffer() {
    output.reset();
  }

  @Override
  public boolean isCommitted() {
    return false;
  }

  @Override
  public void reset() {
    output.reset();
    cookies.clear();
    headers.clear();
    statusCode = 0;
    statusMessage = null;
    contentLength = 0;
    contentType = null;

    outputStream = null;
    printWriter = null;
  }

  @Override
  public void setLocale(Locale locale) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Locale getLocale() {
    throw new UnsupportedOperationException();
  }

  /**
   * Converts the given number of milliseconds from the Unix epoch to a header-formatted date
   * string.
   */
  private static String timestampToHeaderDate(long timestampMs) {
    // Turns out HTTP is gross.
    return DateTimeFormatter.RFC_1123_DATE_TIME.format(
        Instant.ofEpochMilli(timestampMs).atOffset(ZoneOffset.UTC));
  }
}
