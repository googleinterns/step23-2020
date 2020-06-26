package com.google.tripmeout.frontend;

import com.google.tripmeout.frontend.PlaceVisitModel;

public interface PlaceVisitStorage {
  public void addPlaceVisit(PlaceVisitModel placeVisit);
  public void removePlaceVisit(String tripId, String placeId);
  public PlaceVisitModel getPlaceVisit(String tripId, String placeId);
  public void changePlaceVisitStatus(String tripId, String placeId, String newStatus);
}
