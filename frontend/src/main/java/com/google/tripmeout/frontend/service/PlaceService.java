package com.google.tripmeout.frontend.service;

import com.google.maps.errors.ApiException;
import com.google.tripmeout.frontend.PlaceVisitModel;
import java.io.IOException;
import java.lang.InterruptedException;

public interface PlaceService {
  /**
   * check that a placeId is valid based on the Places API
   *
   * @param placeId the id of the place to vaildate
   */
  boolean validatePlaceId(String placesApiPlaceId)
      throws ApiException, InterruptedException, IOException;
}
