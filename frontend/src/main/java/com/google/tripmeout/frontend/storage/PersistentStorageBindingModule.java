package com.google.tripmeout.frontend.storage;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class PersistentStorageBindingModule extends AbstractModule {
  @Override
  public void configure() {
    bind(TripStorage.class).to(PersistentTripStorage.class).in(Singleton.class);
  }
}