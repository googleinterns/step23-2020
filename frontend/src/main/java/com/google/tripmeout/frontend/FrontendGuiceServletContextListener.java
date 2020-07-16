package com.google.tripmeout.frontend;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.tripmeout.frontend.serialization.GsonModelSerializationModule;

/**
 * Provides an {@link Injector} configured to serve the frontend application routes.
 *
 * @see https://github.com/google/guice/wiki/ServletModule
 */
public class FrontendGuiceServletContextListener extends GuiceServletContextListener {
  @Override
  protected Injector getInjector() {
    return Guice.createInjector(new GsonModelSerializationModule());
  }
}
