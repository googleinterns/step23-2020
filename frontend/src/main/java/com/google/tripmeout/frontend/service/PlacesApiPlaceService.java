package com.google.tripmeout.frontend.service;

import com.google.maps.GeoApiContext;
import com.google.maps.PlaceDetailsRequest;
import com.google.maps.model.PlaceDetails;
import com.google.tripmeout.frontend.PlaceVisitModel;

public class PlacesApiPlaceService implements PlaceService {
  private final GepApiContext CONTEXT = new GeoApiContext.Builder().apiKey("AIza...").build();

  @Override
  public PlaceVisitModel getDetailedPlaceVisit(String tripId, String placeId) {
    PlaceDetailsRequest request = new PlaceDetailsRequest(CONTEXT).placeId(placeId).fields(
        PlaceDetailsRequest.FieldMask.NAME, PlaceDetailsRequest.FieldMask.GEOMETRY_LOCATION);

    PlaceDetails details = request.await();

    return PlaceVisitModel.builder()
        .setTripId(tripId)
        .setPlaceId(placeId)
        .setName(details.name)
        .setLatitude(details.geometry.location.latitude)
        .setLongitude(details.geometry.location.longitude)
        .build();
  }
}