package com.google.tripmeout.frontend.storage;

public class PersistentPlaceVisitStorage implements PlaceVisitStorage {
    void addPlaceVisit(PlaceVisitModel placeVisit) throws PlaceVisitAlreadyExistsException{}

 
  void removePlaceVisit(String tripId, String placeVisitId) throws PlaceVisitNotFoundException{}

  
  Optional<PlaceVisitModel> getPlaceVisit(String tripId, String placeVisitId){}

  
  PlaceVisitModel updateUserMarkOrAddPlaceVisit(
      PlaceVisitModel placeVisit, PlaceVisitModel.UserMark newStatus){}

  
  List<PlaceVisitModel> getTripPlaceVisits(String tripId){}

 
  void removeTripPlaceVisits(String tripId) throws TripNotFoundException{}
    
}