package com.google.tripmeout.frontend.service;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth8.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.maps.errors.ApiException;
import com.google.maps.errors.InvalidRequestException;
import com.google.tripmeout.frontend.PlaceVisitModel;
import com.google.tripmeout.frontend.service.PlaceService;
import com.google.tripmeout.frontend.service.PlacesApiPlaceService;
import java.io.IOException;
import java.lang.InterruptedException;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class PlaceServiceTest {
  private final PlaceService placeService = new PlacesApiPlaceService();

  @Test
  public void getDetailedPlaceVisit_invalidPlaceId_throwsError()
      throws ApiException, InterruptedException, IOException {
    assertThrows(InvalidRequestException.class,
        () -> placeService.getDetailedPlaceVisit("tripid", "placeid"));
  }

  @Test
  public void getDetailedPlaceVisit_validPlaceId_returnPlaceVisitModel()
      throws ApiException, InterruptedException, IOException {
    PlaceVisitModel expected = PlaceVisitModel.builder()
                                   .setTripId("tripid")
                                   .setPlaceId("ChIJ3S-JXmauEmsRUcIaWtf4MzE")
                                   .setName("Sydney Opera House")
                                   .build();

    assertThat(placeService.getDetailedPlaceVisit("tripid", "ChIJ3S-JXmauEmsRUcIaWtf4MzE").tripId())
        .isEqualTo("tripid");
    assertThat(
        placeService.getDetailedPlaceVisit("tripid", "ChIJ3S-JXmauEmsRUcIaWtf4MzE").placeId())
        .isEqualTo("ChIJ3S-JXmauEmsRUcIaWtf4MzE");
    assertThat(placeService.getDetailedPlaceVisit("tripid", "ChIJ3S-JXmauEmsRUcIaWtf4MzE").name())
        .isEqualTo("Sydney Opera House");
  }

  @Test
  public void validatePlaceId_invalidPlaceId_returnsFalse()
      throws ApiException, InterruptedException, IOException {
    assertThat(placeService.validatePlaceId("placeId")).isFalse();
  }

  @Test
  public void validatePlaceId_validPlaceId_returnsTrue()
      throws ApiException, InterruptedException, IOException {
    assertThat(placeService.validatePlaceId("ChIJ3S-JXmauEmsRUcIaWtf4MzE")).isTrue();
  }
}