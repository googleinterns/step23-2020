package com.google.tripmeout.frontend;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.tripmeout.frontend.places.PlacesApiPlaceServiceBindingModule;
import com.google.tripmeout.frontend.serialization.GsonModelSerializationModule;
import com.google.tripmeout.frontend.servlet.ServletsModule;
import com.google.tripmeout.frontend.storage.InMemoryStorageBindingModule;

/**
 * Provides an {@link Injector} configured to serve the frontend application routes.
 *
 * @see https://github.com/google/guice/wiki/ServletModule
 */
public class FrontendGuiceServletContextListener extends GuiceServletContextListener {
  @Override
  protected Injector getInjector() {
    return Guice.createInjector(new GsonModelSerializationModule(), new ServletsModule(),
        new InMemoryStorageBindingModule(), new PlacesApiPlaceServiceBindingModule());
  }
}
