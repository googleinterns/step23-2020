package com.google.tripmeout.frontend;

import java.util.List;

public interface PlaceVisitStorage {
  public void addTrip(TripModel trip);
  public void removeTrip(String tripId);
  public void changeName(String tripId, String name);
  public TripModel getTrip(String tripId);
  public List<TripMode> getAllUserTrips(String userId);
  public List<PlaceVisitModel> getTripPlaceVisits(String tripId);
}