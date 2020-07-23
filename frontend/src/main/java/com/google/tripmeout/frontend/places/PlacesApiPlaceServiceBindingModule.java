package com.google.tripmeout.frontend.places;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.maps.GeoApiContext;

public class PlacesApiPlaceServiceBindingModule extends AbstractModule {
  @Override
  public void configure() {
    bind(PlaceService.class).to(PlacesApiPlaceService.class).in(Singleton.class);
  }

  @Provides
  @Singleton
  public GeoApiContext provideGeoApiContext() {
    return new GeoApiContext.Builder().apiKey("APIKEY").build();
  }
}
