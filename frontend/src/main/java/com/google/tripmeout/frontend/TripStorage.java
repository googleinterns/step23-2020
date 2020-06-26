package com.google.tripmeout.frontend;

import com.google.tripmeout.frontend.PlaceVisitModel;
import com.google.tripmeout.frontend.TripModel;
import com.google.tripmeout.frontend.PlaceVisitModel;
import java.util.List;

public interface TripStorage {
  public void addTrip(TripModel trip);
  public void removeTrip(String tripId);
  public void changeName(String tripId, String name);
  public TripModel getTrip(String tripId);
  public List<TripModel> getAllUserTrips(String userId);
  public List<PlaceVisitModel> getTripPlaceVisits(String tripId);
}
