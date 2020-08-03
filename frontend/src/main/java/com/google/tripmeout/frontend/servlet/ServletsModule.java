package com.google.tripmeout.frontend.servlet;

import com.google.inject.servlet.ServletModule;

/**
 * A {@link ServletModule} configured to serve application paths.
 * See https://github.com/google/guice/wiki/ServletModule
 */
public class ServletsModule extends ServletModule {
  @Override
  protected void configureServlets() {
    serveRegex("/api/trips/[^/]+/placeVisits/[^/]+").with(PlaceVisitIndividualServlet.class);
    serveRegex("/api/trips/[^/]+/placeVisits").with(PlaceVisitParentServlet.class);
    serveRegex("/api/trips/[^/]+").with(TripServlet.class);
    serveRegex("/api/login").with(AuthenticationHandlerServlet.class);
    serveRegex("/api/trips").with(TripParentServlet.class);
    serveRegex("/api(?:/.*)?").with(InvalidPathServlet.class);
  }
}
