package com.google.tripmeout.frontend.service;

import com.google.maps.errors.ApiException;
import com.google.tripmeout.frontend.PlaceVisitModel;
import java.io.IOException;
import java.lang.InterruptedException;

public interface PlaceService {
  /**
   * returns a PlaceVisitModel object with all of its fields filled out
   *
   * @param tripId the trip id associated with the PlaceVisitModel object
   * @param placeId the Places API place id of the location to get details for
   */
  PlaceVisitModel getDetailedPlaceVisit(String tripId, String placeId)
      throws ApiException, InterruptedException, IOException;

  /**
   * check that a placeId is valid based on the Places API
   *
   * @param placeId the id of the place to vaildate
   */
  boolean validatePlaceId(String placeId) throws ApiException, InterruptedException, IOException;
}
