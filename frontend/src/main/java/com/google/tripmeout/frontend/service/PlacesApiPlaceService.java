package com.google.tripmeout.frontend.service;

import com.google.maps.GeoApiContext;
import com.google.maps.PlaceDetailsRequest;
import com.google.maps.errors.ApiException;
import com.google.maps.errors.InvalidRequestException;
import com.google.maps.model.PlaceDetails;
import com.google.tripmeout.frontend.PlaceVisitModel;
import java.io.IOException;
import java.lang.InterruptedException;

public class PlacesApiPlaceService implements PlaceService {
  private final GeoApiContext CONTEXT =
      new GeoApiContext.Builder().apiKey("APIKEY").build();

  @Override
  public boolean validatePlaceId(String placesApiPlaceId)
      throws ApiException, InterruptedException, IOException {
    PlaceDetailsRequest request = new PlaceDetailsRequest(CONTEXT).placeId(placesApiPlaceId);
    try {
      request.await();
      return true;
    } catch (InvalidRequestException e) {
      return false;
    }
  }
}
