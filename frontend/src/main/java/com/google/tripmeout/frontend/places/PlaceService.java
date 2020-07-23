package com.google.tripmeout.frontend.places;

import com.google.tripmeout.frontend.error.InvalidPlaceIdException;
import com.google.tripmeout.frontend.error.PlacesApiRequestException;
import java.io.IOException;

public interface PlaceService {
  /**
   * check that a placeId is valid based on the Places API
   *
   * @param placeId the id of the place to vaildate
   */
  void validatePlaceId(String placesApiPlaceId)
      throws IOException, InvalidPlaceIdException, PlacesApiRequestException;
}
