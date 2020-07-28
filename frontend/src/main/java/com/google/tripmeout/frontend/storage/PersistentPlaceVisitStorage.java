package com.google.tripmeout.frontend.storage;

import com.google.tripmeout.frontend.PlaceVisitModel;
import com.google.tripmeout.frontend.error.PlaceVisitAlreadyExistsException;
import com.google.tripmeout.frontend.error.PlaceVisitNotFoundException;
import com.google.tripmeout.frontend.error.TripNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.KeyFactory.Builder;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.PreparedQuery;

public class PersistentPlaceVisitStorage implements PlaceVisitStorage {

  private static final String PLACE_KIND_NAME = "Place";
  private static final String TRIP_ID_PROPERTY_NAME= "tripId";
  private static final String USER_MARK_PROPERTY_NAME= "userMark";
  private static final String ID_PROPERTY_NAME= "id";
  private static final String PLACE_NAME_PROPERTY_NAME= "placeName";
  private static final String PLACE_API_ID_PROPERTY_NAME= "placeApiId";

 public void addPlaceVisit(PlaceVisitModel placeVisit) throws PlaceVisitAlreadyExistsException{
     DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
     Key placeKey = KeyFactory.createKey(PLACE_KIND_NAME,placeVisit.id());
     placeVisit.toBuilder().setId(KeyFactory.keyToString(placeKey));
     Entity placeEntity = new Entity(placeVisit.tripId(), placeKey);
    placeEntity.setProperty(ID_PROPERTY_NAME, placeVisit.id());
    placeEntity.setProperty(TRIP_ID_PROPERTY_NAME, placeVisit.tripId());
    placeEntity.setProperty(USER_MARK_PROPERTY_NAME, placeVisit.userMark());
    placeEntity.setProperty(PLACE_NAME_PROPERTY_NAME, placeVisit.placeName());
    placeEntity.setProperty(PLACE_API_ID_PROPERTY_NAME, placeVisit.placesApiPlaceId());
    datastore.put(placeEntity);

    }

 
  public void removePlaceVisit(String tripId, String placeVisitId) throws PlaceVisitNotFoundException{
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Key placeKey = KeyFactory.stringToKey(placeVisitId);
    datastore.delete(placeKey);
  }

  public Optional<PlaceVisitModel> getPlaceVisit(String tripId, String placeVisitId){

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Key placeKey = KeyFactory.stringToKey(placeVisitId);
    Optional<PlaceVisitModel> optionalPlace = Optional.empty();
    try {
         Entity placeEntity = datastore.get(placeKey);
          optionalPlace = Optional.of( 
        PlaceVisitModel.builder()
        .setId((String) placeEntity.getProperty(ID_PROPERTY_NAME))
        .setPlaceName((String) placeEntity.getProperty(PLACE_NAME_PROPERTY_NAME))
        .setPlacesApiPlaceId((String) placeEntity.getProperty(PLACE_API_ID_PROPERTY_NAME))
        .setTripId((String) placeEntity.getProperty(TRIP_ID_PROPERTY_NAME))
        .setUserMark((PlaceVisitModel.UserMark) placeEntity.getProperty(USER_MARK_PROPERTY_NAME))
        .build());
         
    } catch (EntityNotFoundException e) {    
    }
    return optionalPlace;
  }

  
  public PlaceVisitModel updateUserMarkOrAddPlaceVisit(
      PlaceVisitModel placeVisit, PlaceVisitModel.UserMark newStatus){
           DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
   Key placeKey = KeyFactory.stringToKey(placeVisit.id());
   try{
   Entity placeEntity = datastore.get(placeKey);
   placeEntity.setProperty(USER_MARK_PROPERTY_NAME, newStatus);
   datastore.put(placeEntity);
   placeVisit.toBuilder().setUserMark(newStatus);
   
   }catch(EntityNotFoundException e){

   }
   return placeVisit;
   }

  
  public List<PlaceVisitModel> getTripPlaceVisits(String tripId){
       Query query =
       new Query(tripId);
       DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
       PreparedQuery results = datastore.prepare(query);
       List<PlaceVisitModel>places = new ArrayList<>();
       for(Entity placeEntity: results.asIterable()){
          
           places.add(PlaceVisitModel.builder()
        .setId((String) placeEntity.getProperty(ID_PROPERTY_NAME))
        .setPlaceName((String) placeEntity.getProperty(PLACE_NAME_PROPERTY_NAME))
        .setPlacesApiPlaceId((String) placeEntity.getProperty(PLACE_API_ID_PROPERTY_NAME))
        .setTripId((String) placeEntity.getProperty(TRIP_ID_PROPERTY_NAME))
        .setUserMark((PlaceVisitModel.UserMark) placeEntity.getProperty(USER_MARK_PROPERTY_NAME))
        .build());
       }
       return places;

  }

 
  public void removeTripPlaceVisits(String tripId) throws TripNotFoundException{
       Query query =
       new Query(tripId);
       DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
       PreparedQuery results = datastore.prepare(query);
       for(Entity placeEntity: results.asIterable()){
           datastore.delete(placeEntity.getKey());
       }

  }
    
}