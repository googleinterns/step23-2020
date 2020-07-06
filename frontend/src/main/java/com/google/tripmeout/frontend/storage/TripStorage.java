package com.google.tripmeout.frontend.storage;

import com.google.tripmeout.frontend.TripModel;
import java.util.List;

/**
 * stores TripModel objects
 */
public interface TripStorage {
  /**
   * adds a TripModel object to storage
   *
   * @param trip the TripModel object to add to storage
   *
   * @throws a TripAlreadyExists exception if there is a TripModel object
   *     already in storage with the same tripId as the trip
   */
  void addTrip(TripModel trip);

  /**
   * removes from storage the TripModel object whose tripId matches the given tripId
   *
   * @param tripId the id to match TripModel object's tripId field on
   *
   * @throws a TripNotFound exception if there is no TripModel object with
   *     the given tripId in storage
   */
  void removeTrip(String tripId);

  /**
   * updates the locationLat and locationLong fields of the TripModel object
   * in storage whose tripId matches the given tripId
   *
   * @param tripId the id to match TripModel object's tripId field on
   * @param latitude the new latitude of the TripModel
   * @param longitude the new longitude of the TripModel
   *
   * @throws a TripNotFound exception if there is no TripModel object with
   *     the given tripId in storage
   */
  void updateTripLocation(String tripId, double latitude, double longitude);

  /**
   * updates the name field of the TripModel object in storage whose tripId
   * matches the given tripId
   *
   * @param tripId the id to match TripModel object's tripId field on
   * @param name the new name of the TripModel
   *
   * @throws a TripNotFound exception if there is no TripModel object with
   *     the given tripId in storage
   */
  void updateTripName(String tripId, String tripName);

  /**
   * returns the TripModel object in storage whose tripId matched the given tripId
   *
   * @param tripId the id to match TripModel object's tripId field on
   *
   * @throws a TripNotFound exception if there is no TripModel object with
   *     the given tripId in storage
   */
  TripModel getTrip(String tripId);

  /**
   * returns all TripModel objects in storage whose userId matches the given userId
   *
   * @param userId the id to match TripModel object's userId field on
   */
  List<TripModel> getAllUserTrips(String userId);
}
