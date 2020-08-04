package com.google.tripmeout.frontend.servlet;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.servlet.RequestScoped;
import com.google.inject.servlet.ServletModule;
import com.google.tripmeout.frontend.servlet.Annotations.UserId;

/**
 * A {@link ServletModule} configured to serve application paths.
 * See https://github.com/google/guice/wiki/ServletModule
 */
public class ServletsModule extends ServletModule {
  @Override
  protected void configureServlets() {
    filter("/api/trips*").through(RequestAuthenticatorFilter.class);
    serveRegex("/api/trips/[^/]+/placeVisits/[^/]+").with(PlaceVisitIndividualServlet.class);
    serveRegex("/api/trips/[^/]+/placeVisits").with(PlaceVisitParentServlet.class);
    serveRegex("/api/trips/[^/]+").with(TripServlet.class);
    serveRegex("/api/login").with(AuthenticationHandlerServlet.class);
    serveRegex("/api/trips").with(TripParentServlet.class);
    serveRegex("/api(?:/.*)?").with(InvalidPathServlet.class);
  }

  @Provides
  @Singleton
  public UserService provideUserService() {
    return UserServiceFactory.getUserService();
  }

  @Provides
  @UserId
  @RequestScoped
  public String provideUserId() {
    throw new IllegalStateException("Servlet must use Authorization Filter.");
  }
}
