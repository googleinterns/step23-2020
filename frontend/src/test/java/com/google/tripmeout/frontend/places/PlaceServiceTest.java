package com.google.tripmeout.frontend.places;

import static org.junit.Assert.assertThrows;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.tripmeout.frontend.error.InvalidPlaceIdException;
import com.google.tripmeout.frontend.error.PlacesApiRequestException;
import java.io.IOException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class PlaceServiceTest {
  @Inject private PlaceService placeService;

  @Before
  public void setup() {
    Injector injector = Guice.createInjector(new PlacesApiPlaceServiceBindingModule());
    injector.injectMembers(this);
  }

  @Test
  @Ignore
  public void validatePlaceId_invalidPlaceId_throwsEcxeption()
      throws IOException, InvalidPlaceIdException, PlacesApiRequestException {
    assertThrows(InvalidPlaceIdException.class, () -> placeService.validatePlaceId("placeId"));
  }

  @Test
  @Ignore
  public void validatePlaceId_validPlaceId_noErrorThrown()
      throws IOException, InvalidPlaceIdException, PlacesApiRequestException {
    placeService.validatePlaceId("ChIJ3S-JXmauEmsRUcIaWtf4MzE");
  }
}
