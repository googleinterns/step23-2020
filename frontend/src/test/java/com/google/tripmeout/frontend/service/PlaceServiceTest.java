package com.google.tripmeout.frontend.service;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth8.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.errors.InvalidRequestException;
import com.google.tripmeout.frontend.PlaceVisitModel;
import com.google.tripmeout.frontend.service.PlaceService;
import com.google.tripmeout.frontend.service.PlacesApiPlaceService;
import java.io.IOException;
import java.lang.InterruptedException;
import java.util.List;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

//@RunWith(JUnit4.class)
public class PlaceServiceTest {
  Injector injector = Guice.createInjector(new PlaceServiceModule());
  private GeoApiContext context = injector.getInstance(GeoApiContext.class);

  private final PlaceService placeService = new PlacesApiPlaceService(context);

  @Ignore
  public void validatePlaceId_invalidPlaceId_returnsFalse()
      throws ApiException, InterruptedException, IOException {
    assertThat(placeService.validatePlaceId("placeId")).isFalse();
  }

  @Ignore
  public void validatePlaceId_validPlaceId_returnsTrue()
      throws ApiException, InterruptedException, IOException {
    assertThat(placeService.validatePlaceId("ChIJ3S-JXmauEmsRUcIaWtf4MzE")).isTrue();
  }
}
