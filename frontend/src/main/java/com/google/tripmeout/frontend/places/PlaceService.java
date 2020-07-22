package com.google.tripmeout.frontend.places;

import java.lang.Exception;

public interface PlaceService {
  /**
   * check that a placeId is valid based on the Places API
   *
   * @param placeId the id of the place to vaildate
   */
  void validatePlaceId(String placesApiPlaceId) throws Exception;
}
