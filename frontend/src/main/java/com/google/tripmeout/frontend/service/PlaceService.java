package com.google.tripmeout.frontend.service;

import com.google.tripmeout.frontend.PlaceVisitModel;

public interface PlaceService {
  /**
   * returns a PlaceVisitModel object with all of its fields filled out
   *
   * @param tripId the trip id associated with the PlaceVisitModel object
   * @param placeId the Places API place id of the location to get details for
   */
  PlaceVisitModel getDetailedPlaceVisit(String tripId, String placeId);
}