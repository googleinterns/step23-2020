package com.google.tripmeout.frontend.storage;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class PersistentStorageBindingModule extends AbstractModule {
  @Override
  public void configure() {
    bind(TripStorage.class).to(PersistentTripStorage.class).in(Singleton.class);
    bind(PlaceVisitStorage.class).to(PersistentPlaceVisitStorage.class).in(Singleton.class);
  }

  @Provides
  DatastoreService provideDatastoreService() {
    return DatastoreServiceFactory.getDatastoreService();
  }
}
