package com.google.tripmeout.frontend.service;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.maps.GeoApiContext;
import com.google.maps.PlaceDetailsRequest;
import com.google.maps.errors.ApiException;
import com.google.maps.errors.InvalidRequestException;
import com.google.maps.model.PlaceDetails;
import com.google.tripmeout.frontend.PlaceVisitModel;
import java.io.IOException;
import java.lang.InterruptedException;

@Singleton
public class PlacesApiPlaceService implements PlaceService {
  private GeoApiContext context;

  @Inject
  public PlacesApiPlaceService(GeoApiContext context) {
    this.context = context;
  }

  @Override
  public boolean validatePlaceId(String placesApiPlaceId)
      throws ApiException, InterruptedException, IOException {
    PlaceDetailsRequest request = new PlaceDetailsRequest(context).placeId(placesApiPlaceId);
    try {
      request.await();
      return true;
    } catch (InvalidRequestException e) {
      return false;
    }
  }
}
