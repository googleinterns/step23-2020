package com.google.tripmeout.frontend.servlet;

import com.google.common.flogger.FluentLogger;
import javax.inject.Singleton;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Singleton
public class InvalidPathServlet extends HttpServlet {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    logger.atWarning().log("Invalid request path: %s %s", req.getMethod(), req.getRequestURI());
    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
  }
}
