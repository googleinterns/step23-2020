package com.google.tripmeout.frontend.storage;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;
import com.google.inject.Inject;
import com.google.tripmeout.frontend.TripModel;
import com.google.tripmeout.frontend.error.TripAlreadyExistsException;
import com.google.tripmeout.frontend.error.TripNotFoundException;
import java.util.ArrayList;
import java.util.List;

class PersistentTripStorage implements TripStorage {
  private static final String TRIP_ID_PROPERTY_NAME = "tripId";
  private static final String USER_ID_PROPERTY_NAME = "userId";
  private static final String NAME_PROPERTY_NAME = "name";
  private static final String PLACE_API_ID_PROPERTY_NAME = "placeId";

  private final DatastoreService datastore;
  @Inject
  PersistentTripStorage(DatastoreService datastore) {
    this.datastore = datastore;
  }

  public void addTrip(TripModel trip) throws TripAlreadyExistsException {
    Key tripKey = KeyFactory.stringToKey(trip.id());
    Transaction transaction = datastore.beginTransaction();
    try {
      datastore.get(tripKey);
      transaction.rollback();
      throw new TripAlreadyExistsException("Trip Already Exists");
    } catch (EntityNotFoundException e) {
      // Do nothing if trip doesn't exist
    }
    try {
      Entity tripEntity = new Entity(tripKey);
      tripEntity.setProperty(TRIP_ID_PROPERTY_NAME, trip.id());
      tripEntity.setProperty(USER_ID_PROPERTY_NAME, trip.userId());
      tripEntity.setProperty(NAME_PROPERTY_NAME, trip.name());
      tripEntity.setProperty(PLACE_API_ID_PROPERTY_NAME, trip.placesApiPlaceId());
      datastore.put(tripEntity);
    } catch (Exception e) {
      // TODO:Wrap in new storage exception
      transaction.rollback();
    }
    transaction.commit();
  }
  public void removeTrip(String tripId) throws TripNotFoundException {
    Key tripKey = KeyFactory.stringToKey(tripId);
    Transaction transaction = datastore.beginTransaction();
    try {
      datastore.get(tripKey);
      datastore.delete(tripKey);
    } catch (EntityNotFoundException e) {
      transaction.rollback();
      throw new TripNotFoundException("Trip with id: " + tripId + " not found");
    }
    transaction.commit();
  }

  public void updateTripName(String tripId, String tripName) throws TripNotFoundException {
    Key tripKey = KeyFactory.stringToKey(tripId);
    try {
      Entity tripEntity = datastore.get(tripKey);
      tripEntity.setProperty(NAME_PROPERTY_NAME, tripName);
      datastore.put(tripEntity);
    } catch (EntityNotFoundException e) {
      throw new TripNotFoundException("Trip with id: " + tripId + " not found");
    }
  }

  public TripModel getTrip(String tripId) throws TripNotFoundException {
    Key tripKey = KeyFactory.stringToKey(tripId);
    try {
      Entity tripEntity = datastore.get(tripKey);

      return TripModel.builder()
          .setId((String) tripEntity.getProperty(TRIP_ID_PROPERTY_NAME))
          .setName((String) tripEntity.getProperty(NAME_PROPERTY_NAME))
          .setUserId((String) tripEntity.getProperty(USER_ID_PROPERTY_NAME))
          .setPlacesApiPlaceId((String) tripEntity.getProperty(PLACE_API_ID_PROPERTY_NAME))
          .build();

    } catch (EntityNotFoundException e) {
      throw new TripNotFoundException("Trip with id: " + tripId + " not found");
    }
  }

  public List<TripModel> getAllUserTrips(String userId) {
    Query query = new Query(userId);
    PreparedQuery results = datastore.prepare(query);
    List<TripModel> trips = new ArrayList<>();
    for (Entity tripEntity : results.asIterable()) {
      trips.add(
          TripModel.builder()
              .setId((String) tripEntity.getProperty(TRIP_ID_PROPERTY_NAME))
              .setName((String) tripEntity.getProperty(NAME_PROPERTY_NAME))
              .setUserId((String) tripEntity.getProperty(USER_ID_PROPERTY_NAME))
              .setPlacesApiPlaceId((String) tripEntity.getProperty(PLACE_API_ID_PROPERTY_NAME))
              .build());
    }
    return trips;
  }
}
