package com.google.tripmeout.frontend.storage;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.tripmeout.frontend.storage.InMemoryPlaceVisitStorage;
import com.google.tripmeout.frontend.storage.InMemoryTripModelStorage;

public class InMemoryStorageBindingModule extends AbstractModule {
  @Override
  public void configure() {
    bind(PlaceVisitStorage.class).to(InMemoryPlaceVisitStorage.class).in(Singleton.class);
    bind(TripStorage.class).to(InMemoryTripModelStorage.class).in(Singleton.class);
  }
}
