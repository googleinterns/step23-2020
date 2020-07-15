package com.google.tripmeout.frontend.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.tripmeout.frontend.PlaceVisitModel;
import com.google.tripmeout.frontend.TripModel;

/**
 * Provides a {@link Gson} instance configured to handle model objects.
 */
public class GsonModelSerializationModule extends AbstractModule {
  @Provides
  @Singleton
  public Gson provideConfiguredGson() {
    return new GsonBuilder()
        .registerTypeHierarchyAdapter(PlaceVisitModel.class, new GsonPlaceVisitTypeAdapter())
        .registerTypeHierarchyAdapter(TripModel.class, new GsonTripModelTypeAdapter())
        .create();
  }
}
