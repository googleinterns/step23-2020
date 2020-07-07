package com.google.tripmeout.frontend.storage;
 
import com.google.tripmeout.frontend.PlaceVisitModel;
import com.google.tripmeout.frontend.storage.PlaceVisitStorage;
import com.google.tripmeout.frontend.error.PlaceVisitAlreadyExistsException;
import com.google.tripmeout.frontend.error.PlaceVisitNotFoundException;
import com.google.common.collect.Tables;
import com.google.common.collect.Table;
import com.google.common.collect.HashBasedTable;
import java.util.Map;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class InMemoryPlaceVisitStorage implements PlaceVisitStorage {
  // <tripId, placeId, PlaceVisitModel> 
  Map<String, Map<String, PlaceVisitModel>> storage = new ConcurrentHashMap<>();
 
  @Override
  public void addPlaceVisit(PlaceVisitModel placeVisit) throws PlaceVisitAlreadyExistsException {
    synchronized (storage) {
      Map<String, PlaceVisitModel> placesMap = storage.get(placeVisit.tripId());

      if (placesMap == null) {
        Map<String, PlaceVisitModel> newPlaceMap = new ConcurrentHashMap<>();
        newPlaceMap.put(placeVisit.placeId(), placeVisit);
        storage.put(placeVisit.tripId(), newPlaceMap);

      } else if (placesMap.get(placeVisit.placeId()) == null) {
        placesMap.put(placeVisit.placeId(), placeVisit);

      } else {
        throw new PlaceVisitAlreadyExistsException("PlaceVisit " + 
          placeVisit.name() + " already exists for trip " + placeVisit.tripId());
      }
    }
  }

  @Override
  public void removePlaceVisit(String tripId, String placeId) throws PlaceVisitNotFoundException {
    synchronized (storage) {
      Map<String, PlaceVisitModel> placesMap = storage.get(tripId);
      if (placesMap == null || placesMap.remove(placeId) == null) {
        throw new PlaceVisitNotFoundException("PlaceVisit with id" + placeId + 
          " not found for trip " + tripId);
      }
    }
  }

  @Override
  public PlaceVisitModel getPlaceVisit(String tripId, String placeId) throws PlaceVisitNotFoundException {
    Map<String, PlaceVisitModel> placesMap = storage.get(tripId);
    if (placesMap == null || placesMap.get(placeId) == null) {
      throw new PlaceVisitNotFoundException("PlaceVisit with id" + placeId + 
          " not found for trip " + tripId);
    } else {
      return placesMap.get(placeId);
    }
    
  }

  @Override
  public boolean changePlaceVisitStatus(String tripId, String placeId, String newStatus) {
    synchronized (storage) {
      Map<String, PlaceVisitModel> placesMap = storage.get(tripId);
      if (placesMap == null || placesMap.get(placeId) == null) {
        return false;
      } else {
        PlaceVisitModel place = placesMap.get(placeId);
        PlaceVisitModel updatedPlace = place.toBuilder().setUserMark(newStatus).build();
        placesMap.put(placeId, updatedPlace);
        return true;
      }
    }
  }

  @Override
  public List<PlaceVisitModel> getTripPlaceVisits(String tripId) {
    Map<String, PlaceVisitModel> placesMap = storage.get(tripId);
    List<PlaceVisitModel> tripPlaceVisits = new ArrayList<>();
    if (placesMap == null) {
      return tripPlaceVisits;
    }
    for (PlaceVisitModel place: placesMap.values()) {
      if (place != null && (place.userMark().equals("must-see") || place.userMark().equals("if-time"))) {
        tripPlaceVisits.add(place);
      }
    }

    return tripPlaceVisits;
  }
}
