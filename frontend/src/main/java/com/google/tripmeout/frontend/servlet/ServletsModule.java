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
    // serve("/trips/*/placeVisits/*").with(PlaceIndividualServlet.class);
    // serve("/trips/*/placeVisits").with(PlaceParentServlet.class);
    // serve("/trips/*").with(TripIndividualServlet.class);
    serve("/trips").with(TripParentServlet.class);
  }
}