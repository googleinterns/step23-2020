package com.google.tripmeout.frontend.service;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.maps.GeoApiContext;

public class PlaceServiceModule extends AbstractModule {
  @Provides
  @Singleton
  public GeoApiContext provideGeoApiContext() {
    return new GeoApiContext.Builder().apiKey("APIKEY").build();
  }
}