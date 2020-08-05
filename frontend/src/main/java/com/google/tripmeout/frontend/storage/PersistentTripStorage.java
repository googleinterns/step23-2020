package com.google.tripmeout.frontend.storage;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.FilterOperator;
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
    Key tripKey = DatastoreUtil.tripKey(trip.id());
    Transaction transaction = datastore.beginTransaction();
    try {
      datastore.get(transaction, tripKey);
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
      datastore.put(transaction, tripEntity);
    } catch (RuntimeException e) {
      transaction.rollback();
      throw new RuntimeException();
    }
    transaction.commit();
  }
  public void removeTrip(String tripId) throws TripNotFoundException {
    Key tripKey = DatastoreUtil.tripKey(tripId);
    Transaction transaction = datastore.beginTransaction();
    try {
      datastore.get(transaction, tripKey);
      datastore.delete(transaction, tripKey);
    } catch (EntityNotFoundException e) {
      transaction.rollback();
      throw new TripNotFoundException("Trip with id: " + tripId + " not found");
    }
    transaction.commit();
  }

  public void updateTripName(String tripId, String tripName) throws TripNotFoundException {
    Key tripKey = DatastoreUtil.tripKey(tripId);
    try {
      Entity tripEntity = datastore.get(tripKey);
      tripEntity.setProperty(NAME_PROPERTY_NAME, tripName);
      datastore.put(tripEntity);
    } catch (EntityNotFoundException e) {
      throw new TripNotFoundException("Trip with id: " + tripId + " not found");
    }
  }

  public TripModel getTrip(String tripId) throws TripNotFoundException {
    Key tripKey = DatastoreUtil.tripKey(tripId);
    try {
      Entity tripEntity = datastore.get(tripKey);
      return entityToTrip(tripEntity);
    } catch (EntityNotFoundException e) {
      throw new TripNotFoundException("Trip with id: " + tripId + " not found");
    }
  }

  public List<TripModel> getAllUserTrips(String userId) {
    FilterPredicate filterPredicate = new FilterPredicate(USER_ID_PROPERTY_NAME, FilterOperator.EQUAL, userId);
    Query query = new Query(DatastoreUtil.TRIP_ENTITY_TYPE).setFilter(filterPredicate);
    PreparedQuery results = datastore.prepare(query);
    List<TripModel> trips = new ArrayList<>();
    for (Entity tripEntity : results.asIterable()) {
      trips.add(entityToTrip(tripEntity));
    }
    return trips;
  }

  private static TripModel entityToTrip(Entity entity) {
    return TripModel.builder()
              .setId((String) entity.getProperty(TRIP_ID_PROPERTY_NAME))
              .setName((String) entity.getProperty(NAME_PROPERTY_NAME))
              .setUserId((String) entity.getProperty(USER_ID_PROPERTY_NAME))
              .setPlacesApiPlaceId((String) entity.getProperty(PLACE_API_ID_PROPERTY_NAME))
              .build();
  }
}
