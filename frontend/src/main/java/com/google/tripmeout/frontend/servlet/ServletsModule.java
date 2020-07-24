package com.google.tripmeout.frontend.servlet;

import com.google.inject.servlet.ServletModule;

/**
 * A {@link ServletModule} configured to serve application paths.
 * @see https://github.com/google/guice/wiki/ServletModule
 */
public class ServletsModule extends ServletModule {
  @Override
  protected void configureServlets() {
    // TO-DO: uncomment once servlet is implemented
    serve("/api/trips/*/placeVisits/*").with(PlaceVisitIndividualServlet.class);
    serve("/api/trips/*/placeVisits").with(PlaceVisitParentServlet.class);
    serve("/api/trips/*").with(TripServlet.class);
    serve("/api/trips").with(TripParentServlet.class);
  }
}
