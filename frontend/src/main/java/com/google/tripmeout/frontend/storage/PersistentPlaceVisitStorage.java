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
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.repackaged.com.google.datastore.v1.Datastore;
import com.google.inject.Inject;
import com.google.tripmeout.frontend.PlaceVisitModel;
import com.google.tripmeout.frontend.error.PlaceVisitAlreadyExistsException;
import com.google.tripmeout.frontend.error.PlaceVisitNotFoundException;
import com.google.tripmeout.frontend.error.TripMeOutException;
import com.google.tripmeout.frontend.error.TripNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class PersistentPlaceVisitStorage implements PlaceVisitStorage {
  private static final String TRIP_ID_PROPERTY_NAME = "tripId";
  private static final String USER_MARK_PROPERTY_NAME = "userMark";
  private static final String ID_PROPERTY_NAME = "id";
  private static final String PLACE_NAME_PROPERTY_NAME = "placeName";
  private static final String PLACE_API_ID_PROPERTY_NAME = "placeApiId";

  private final DatastoreService datastore;

  @Inject
  PersistentPlaceVisitStorage(DatastoreService datastore) {
    this.datastore = datastore;
  }

  public void addPlaceVisit(PlaceVisitModel placeVisit) throws PlaceVisitAlreadyExistsException {
    Key placeKey = KeyFactory.stringToKey(placeVisit.id());
    Transaction transaction = datastore.beginTransaction();
    try {
      datastore.get(placeKey);
      transaction.rollback();
      throw new PlaceVisitAlreadyExistsException("Place Already Exists");
    } catch (EntityNotFoundException e) {
        //Do nothing
    }
    try {
      Entity placeEntity = new Entity(placeKey);
      placeEntity.setProperty(ID_PROPERTY_NAME, placeVisit.id());
      placeEntity.setProperty(TRIP_ID_PROPERTY_NAME, placeVisit.tripId());
      placeEntity.setProperty(USER_MARK_PROPERTY_NAME, placeVisit.userMark().toString());
      placeEntity.setProperty(PLACE_NAME_PROPERTY_NAME, placeVisit.placeName());
      placeEntity.setProperty(PLACE_API_ID_PROPERTY_NAME, placeVisit.placesApiPlaceId());
      datastore.put(placeEntity);
    } catch (Exception e) {
      //TODO:Wrap this in new storage exception
      transaction.rollback();
    }
    transaction.commit();
  }

  public void removePlaceVisit(String tripId, String placeVisitId)
      throws PlaceVisitNotFoundException {
    Key placeKey = KeyFactory.stringToKey(placeVisitId);
    Transaction transaction = datastore.beginTransaction();
    try {
      datastore.get(placeKey);
    } catch (EntityNotFoundException e) {
      transaction.rollback();
      throw new PlaceVisitNotFoundException("Place with id: " + placeVisitId + " doesn't exist");
    }
    try{
         datastore.delete(placeKey);
    }finally{
        transaction.commit();
    } 
  }

  public Optional<PlaceVisitModel> getPlaceVisit(String tripId, String placeVisitId) {
    Key placeKey = KeyFactory.stringToKey(placeVisitId);
    Optional<PlaceVisitModel> optionalPlace = Optional.empty();
    try {
      Entity placeEntity = datastore.get(placeKey);
      String mark = (String) placeEntity.getProperty(USER_MARK_PROPERTY_NAME);
      PlaceVisitModel.UserMark userMark = PlaceVisitModel.UserMark.valueOf(mark);
      optionalPlace = Optional.of(
          PlaceVisitModel.builder()
              .setId((String) placeEntity.getProperty(ID_PROPERTY_NAME))
              .setPlaceName((String) placeEntity.getProperty(PLACE_NAME_PROPERTY_NAME))
              .setPlacesApiPlaceId((String) placeEntity.getProperty(PLACE_API_ID_PROPERTY_NAME))
              .setTripId((String) placeEntity.getProperty(TRIP_ID_PROPERTY_NAME))
              .setUserMark(userMark)
              .build());

    } catch (EntityNotFoundException e) {
         //Do nothing
    }
    return optionalPlace;
  }

  public PlaceVisitModel updateUserMarkOrAddPlaceVisit(
    PlaceVisitModel placeVisit, PlaceVisitModel.UserMark newStatus) {
    Key placeKey = KeyFactory.stringToKey(placeVisit.id());
    try {
      Entity placeEntity = datastore.get(placeKey);
      placeEntity.setProperty(USER_MARK_PROPERTY_NAME, newStatus.toString());
      datastore.put(placeEntity);
      placeVisit.toBuilder().setUserMark(newStatus);

    } catch (EntityNotFoundException e) {
    
    }
    return placeVisit;
  }

  public List<PlaceVisitModel> getTripPlaceVisits(String tripId) {
    Query query = new Query(tripId);
    PreparedQuery results = datastore.prepare(query);
    List<PlaceVisitModel> places = new ArrayList<>();
    for (Entity placeEntity : results.asIterable()) {
      String mark = (String) placeEntity.getProperty(USER_MARK_PROPERTY_NAME);
      PlaceVisitModel.UserMark userMark = PlaceVisitModel.UserMark.valueOf(mark);
      places.add(
          PlaceVisitModel.builder()
              .setId((String) placeEntity.getProperty(ID_PROPERTY_NAME))
              .setPlaceName((String) placeEntity.getProperty(PLACE_NAME_PROPERTY_NAME))
              .setPlacesApiPlaceId((String) placeEntity.getProperty(PLACE_API_ID_PROPERTY_NAME))
              .setTripId((String) placeEntity.getProperty(TRIP_ID_PROPERTY_NAME))
              .setUserMark(userMark)
              .build());
    }
    return places;
  }

  public void removeTripPlaceVisits(String tripId) throws TripNotFoundException {
    Transaction transaction = datastore.beginTransaction();
    Query query = new Query(tripId);
    PreparedQuery results = datastore.prepare(query);
    int count = 0;
    for (Entity placeEntity : results.asIterable()) {
        Key key = placeEntity.getKey();
      datastore.delete(key);
      count +=1;
    }
    if(count==0){
        transaction.rollback();
        throw new TripNotFoundException("Trip with id: " + tripId + " doesn't exist");
    }
    transaction.commit();
  }
}
