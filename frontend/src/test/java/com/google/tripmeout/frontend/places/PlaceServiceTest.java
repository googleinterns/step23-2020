package com.google.tripmeout.frontend.places;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth8.assertThat;
import static org.junit.Assert.assertThrows;

import org.junit.Assert;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.errors.InvalidRequestException;
import com.google.tripmeout.frontend.PlaceVisitModel;
import com.google.tripmeout.frontend.places.PlaceService;
import com.google.tripmeout.frontend.places.PlacesApiPlaceService;
import java.io.IOException;
import java.lang.InterruptedException;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class PlaceServiceTest {
  @Inject
  private PlaceService placeService;

  @Before
  public void setup() {
    Injector injector = Guice.createInjector(new PlacesApiPlaceServiceBindingModule());
    injector.injectMembers(this);
  }

  @Test
  @Ignore
  public void validatePlaceId_invalidPlaceId_throwsEcxeption() throws Exception {
    assertThrows(InvalidRequestException.class, () -> placeService.validatePlaceId("placeId"));
  }

  @Test
  @Ignore
  public void validatePlaceId_validPlaceId_noErrorThrown() throws Exception {
    placeService.validatePlaceId("ChIJ3S-JXmauEmsRUcIaWtf4MzE");
  }
}
