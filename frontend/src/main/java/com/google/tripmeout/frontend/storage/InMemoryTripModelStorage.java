package com.google.tripmeout.frontend.storage;

import com.google.tripmeout.frontend.TripModel;
import com.google.tripmeout.frontend.error.TripAlreadyExistsException;
import com.google.tripmeout.frontend.error.TripNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryTripModelStorage {
  Map<String, TripModel> storage = new ConcurrentHashMap<>();

  public void addTrip(TripModel trip) throws TripAlreadyExistsException {
    synchronized(storage) {
      if (storage.get(trip.id()) == null) {
        storage.put(trip.id(), trip);
      } else {
        throw new TripAlreadyExistsException("Trip with id: " + trip.id() + 
            " already exisits");
      }
    }
  }

  public void removeTrip(String tripId) throws TripNotFoundException {
    TripModel trip = storage.remove(tripId);
    if (trip == null) {
      throw new TripNotFoundException("Trip with id: " + tripId + 
          " not found in storage");
    }
  }

  public void updateTripLocation(String tripId, double latitude, double longitude) 
      throws TripNotFoundException {
    synchronized(storage) {
      TripModel trip = storage.get(tripId);
      if (trip == null) {
        throw new TripNotFoundException("Trip with id: " + tripId + 
            " not found in storage");
      }
      TripModel updatedTrip = trip.toBuilder()
          .setLocationLat(latitude)
          .setLocationLong(longitude)
          .build();

      storage.put(tripId, updatedTrip);
    }
  }

  public void updateTripName(String tripId, String name) throws TripNotFoundException {
    synchronized(storage) {
      TripModel trip = storage.get(tripId);
      if (trip == null) {
        throw new TripNotFoundException("Trip with id: " + tripId + 
            " not found in storage");
      }
      TripModel updatedTrip = trip.toBuilder()
          .setName(name)
          .build();

      storage.put(tripId, updatedTrip);
    }
  }

  public TripModel getTrip(String tripId) throws TripNotFoundException {
    synchronized(storage) {
      TripModel trip = storage.get(tripId);
      if (trip != null) {
        return trip;
      }
      throw new TripNotFoundException("Trip with id: " + tripId + 
          " not found in storage");
    } 
  }

  public List<TripModel> getAllUserTrips(String userId) {
    List<TripModel> trips = new ArrayList<>();
    storage.forEach((tripId, trip) -> {
      if (userId.equals(trip.userId())) {
        trips.add(trip);
      }
    });
    return trips;
  }
}
