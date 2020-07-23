package com.google.tripmeout.frontend.places;

import com.google.inject.Inject;
import com.google.maps.GeoApiContext;
import com.google.maps.PlaceDetailsRequest;
import com.google.maps.errors.ApiException;
import com.google.maps.errors.InvalidRequestException;
import com.google.tripmeout.frontend.error.InvalidPlaceIdException;
import com.google.tripmeout.frontend.error.PlacesApiRequestException;
import java.io.IOException;

public class PlacesApiPlaceService implements PlaceService {
  private GeoApiContext context;

  @Inject
  public PlacesApiPlaceService(GeoApiContext context) {
    this.context = context;
  }

  @Override
  public void validatePlaceId(String placesApiPlaceId)
      throws IOException, InvalidPlaceIdException, PlacesApiRequestException {
    PlaceDetailsRequest request = new PlaceDetailsRequest(context).placeId(placesApiPlaceId);
    try {
      request.await();
    } catch (InvalidRequestException e) {
      throw new InvalidPlaceIdException(
          String.format("Places API id '%s' is not a valid id", placesApiPlaceId));
    } catch (ApiException e) {
      throw new PlacesApiRequestException(e.getMessage());
    } catch (InterruptedException e) {
      throw new PlacesApiRequestException(e.getMessage(), e.getCause());
    }
  }
}
