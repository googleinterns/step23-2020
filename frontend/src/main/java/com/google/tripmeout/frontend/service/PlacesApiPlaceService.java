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
  public PlaceVisitModel getDetailedPlaceVisit(String tripId, String placeId)
      throws ApiException, InterruptedException, IOException {
    PlaceDetailsRequest request = new PlaceDetailsRequest(CONTEXT).placeId(placeId).fields(
        PlaceDetailsRequest.FieldMask.NAME, PlaceDetailsRequest.FieldMask.GEOMETRY_LOCATION);

    PlaceDetails details = request.await();

    return PlaceVisitModel.builder()
        .setTripId(tripId)
        .setPlaceId(placeId)
        .setName(details.name)
        .setLatitude(details.geometry.location.lat)
        .setLongitude(details.geometry.location.lng)
        .build();
  }

  @Override
  public boolean validatePlaceId(String placeId)
      throws ApiException, InterruptedException, IOException {
    PlaceDetailsRequest request = new PlaceDetailsRequest(CONTEXT).placeId(placeId);
    try {
      request.await();
      return true;
    } catch (InvalidRequestException e) {
      return false;
    }
  }
}