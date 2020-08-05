package com.google.tripmeout.frontend.servlet;

import com.google.appengine.api.users.UserService;
import com.google.common.flogger.FluentLogger;
import com.google.inject.Key;
import com.google.tripmeout.frontend.servlet.Annotations.UserId;
import java.io.IOException;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Singleton
public class RequestAuthenticatorFilter extends HttpFilter {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private static final String GOOGLE_DOMAIN = "google.com";
  private final UserService userService;

  @Inject
  RequestAuthenticatorFilter(UserService userService) {
    this.userService = userService;
  }

  @Override
  protected void doFilter(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain) throws IOException, ServletException {
    logger.atInfo().log("We are in the doFilter");
    if (userService.isUserLoggedIn()) {
      logger.atInfo().log("User %s is logged in with email %s of domain %s",
          userService.getCurrentUser().getUserId(), userService.getCurrentUser().getEmail(),
          userService.getCurrentUser().getAuthDomain());
      if (userService.getCurrentUser().getEmail().endsWith(GOOGLE_DOMAIN)) {
        String userId = userService.getCurrentUser().getUserId();
        request.setAttribute(Key.get(String.class, UserId.class).toString(), userId);
        chain.doFilter(request, response);
      } else {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      }
    } else {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
  }
}
