package com.google.tripmeout.frontend.storage;
 
import com.google.tripmeout.frontend.TripModel;
import com.google.tripmeout.frontend.error.TripAlreadyExistsException;
import com.google.tripmeout.frontend.error.TripNotFoundException;
import java.util.List;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
 
public class PersistentTripStorage implements TripStorage {
 
   private static final String TRIP_ENTITY_NAME = "trips";
   private static final String TRIP_ID_PROPERTY_NAME= "tripId";
   private static final String USER_ID_PROPERTY_NAME= "userId";
   private static final String NAME_PROPERTY_NAME= "name";
   private static final String PLACE_API_ID_PROPERTY_NAME= "placeId";
 
 public void addTrip(TripModel trip) throws TripAlreadyExistsException{
   DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Entity taskEntity = new Entity(TRIP_ENTITY_NAME);
    taskEntity.setProperty(TRIP_ID_PROPERTY_NAME, trip.id());
    taskEntity.setProperty(USER_ID_PROPERTY_NAME, trip.userId());
    taskEntity.setProperty(NAME_PROPERTY_NAME, trip.name());
    taskEntity.setProperty(PLACE_API_ID_PROPERTY_NAME, trip.placesApiPlaceId());
    datastore.put(taskEntity);
 }
 public  void removeTrip(String tripId) throws TripNotFoundException{
     
 }
 
 public void updateTripName(String tripId, String tripName) throws TripNotFoundException{

 }
 
 public TripModel getTrip(String tripId) throws TripNotFoundException{

 }
 
 public List<TripModel> getAllUserTrips(String userId){

 }
  
}
