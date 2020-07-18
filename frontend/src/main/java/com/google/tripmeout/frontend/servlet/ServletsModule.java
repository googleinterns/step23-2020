package com.google.tripmeout.frontend.servlet;

import com.google.inject.servlet.ServletModule;

/**
 * A {@link ServletModule} configured to serve SPS application paths.
 * @see https://github.com/google/guice/wiki/ServletModule
 */
public class ServletsModule extends ServletModule {
  @Override
  protected void configureServlets() {
    // TO-DO: uncomment once servlet is implemented
    // serve("/api/trips/*/placeVisits/*").with(PlaceIndividualServlet.class);
    // serve("/api/trips/*/placeVisits").with(PlaceParentServlet.class);
    // serve("/api/trips/*").with(TripIndividualServlet.class);
    serve("/api/trips").with(TripParentServlet.class);
  }
}
