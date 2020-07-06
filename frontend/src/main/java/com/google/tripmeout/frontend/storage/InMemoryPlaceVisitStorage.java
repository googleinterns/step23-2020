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

public class InMemoryPlaceVisitStorage implements PlaceVisitStorage {
  // <placeId, tripId, PlaceVisitModel> 
  Table<String, String, PlaceVisitModel> storage = Tables.synchronizedTable(HashBasedTable.create());
 
  @Override
  public void addPlaceVisit(PlaceVisitModel placeVisit) throws PlaceVisitAlreadyExistsException {
    synchronized(storage) {
      PlaceVisitModel place = storage.get(placeVisit.placeId(), placeVisit.tripId());
      if (place == null) {
        storage.put(placeVisit.placeId(), placeVisit.tripId(), placeVisit);
      } else {
        throw new PlaceVisitAlreadyExistsException("PlaceVisit " + 
          place.name() + " already exists for trip " + place.tripId());
      }
    }
  }

  @Override
  public void removePlaceVisit(String tripId, String placeId) throws PlaceVisitNotFoundException {
    if (storage.remove(placeId, tripId) == null) {
      throw new PlaceVisitNotFoundException("PlaceVisit with id" + placeId + 
      " not found for trip " + tripId);
    }
  }

  @Override
  public PlaceVisitModel getPlaceVisit(String tripId, String placeId) throws PlaceVisitNotFoundException {
    PlaceVisitModel place = storage.get(placeId, tripId);
    if (place != null) {
      return place;
    }
    throw new PlaceVisitNotFoundException("PlaceVisit with id" + placeId + 
      " not found for trip " + tripId);
  }

  @Override
  public boolean changePlaceVisitStatus(String tripId, String placeId, String newStatus) {
    synchronized(storage) {
      PlaceVisitModel place = storage.get(placeId, tripId);
      if (place != null) {
        PlaceVisitModel updatedPlace = place.toBuilder().setUserMark(newStatus).build();
        storage.put(placeId, tripId, updatedPlace);
        return true;
      }
      return false;
    }
  }

  @Override
  public List<PlaceVisitModel> getTripPlaceVisits(String tripId) {
    Map<String, PlaceVisitModel> placeIdPlaceMap = storage.column(tripId);
    Collection<PlaceVisitModel> placeVisits = placeIdPlaceMap.values();
    List<PlaceVisitModel> tripPlaceVisits = new ArrayList<>();
    for (PlaceVisitModel place: placeVisits) {
      if (place.userMark().equals("must-see") || place.userMark().equals("if-time")) {
        tripPlaceVisits.add(place);
      }
    }
    return tripPlaceVisits;
  }
 
}
