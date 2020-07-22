package com.google.tripmeout.frontend.places;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.maps.GeoApiContext;
import com.google.maps.PlaceDetailsRequest;
import com.google.maps.errors.InvalidRequestException;
import com.google.maps.model.PlaceDetails;
import com.google.tripmeout.frontend.PlaceVisitModel;
import java.lang.Exception;

public class PlacesApiPlaceService implements PlaceService {
  private GeoApiContext context;

  @Inject
  public PlacesApiPlaceService(GeoApiContext context) {
    this.context = context;
  }

  @Override
  public void validatePlaceId(String placesApiPlaceId) throws Exception {
    PlaceDetailsRequest request = new PlaceDetailsRequest(context).placeId(placesApiPlaceId);
    request.await();
  }
}
