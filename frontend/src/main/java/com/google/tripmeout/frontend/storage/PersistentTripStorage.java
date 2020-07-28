package com.google.tripmeout.frontend.storage;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.KeyFactory.Builder;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.common.base.Optional;
import com.google.tripmeout.frontend.TripModel;
import com.google.tripmeout.frontend.error.TripAlreadyExistsException;
import com.google.tripmeout.frontend.error.TripNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class PersistentTripStorage implements TripStorage {
  private static final String TRIP_KIND_NAME = "trips";
  private static final String TRIP_ID_PROPERTY_NAME = "tripId";
  private static final String USER_ID_PROPERTY_NAME = "userId";
  private static final String NAME_PROPERTY_NAME = "name";
  private static final String PLACE_API_ID_PROPERTY_NAME = "placeId";

  private final DatastoreService datastore;

  PersistentTripStorage(DatastoreService datastore) {
    this.datastore = datastore;
  }

  public void addTrip(TripModel trip) throws TripAlreadyExistsException {
    Key tripKey = KeyFactory.createKey(TRIP_KIND_NAME, trip.id());
    try {
      datastore.get(tripKey);
      throw new TripAlreadyExistsException("Trip Already Exits");
    } catch (EntityNotFoundException e) {
      trip.toBuilder().setId(KeyFactory.keyToString(tripKey));
      Entity taskEntity = new Entity(trip.userId(), tripKey);
      taskEntity.setProperty(TRIP_ID_PROPERTY_NAME, trip.id());
      taskEntity.setProperty(USER_ID_PROPERTY_NAME, trip.userId());
      taskEntity.setProperty(NAME_PROPERTY_NAME, trip.name());
      taskEntity.setProperty(PLACE_API_ID_PROPERTY_NAME, trip.placesApiPlaceId());
      datastore.put(taskEntity);
    }
  }
  public void removeTrip(String tripId) throws TripNotFoundException {
    System.out.println("TRIP ID:ReMOVE - " + tripId);
    Key tripKey = KeyFactory.stringToKey(tripId);
    try {
      datastore.get(tripKey);
      datastore.delete(tripKey);
    } catch (EntityNotFoundException e) {
      throw new TripNotFoundException("Trip with id: " + tripId + " not found");
    }
  }

  public void updateTripName(String tripId, String tripName) throws TripNotFoundException {
    System.out.println("TRIP ID:UPDATE -" + tripId);
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
    System.out.println("TRIP ID:GET -" + tripId);
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