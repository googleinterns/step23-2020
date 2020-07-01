package com.google.tripmeout.frontend.storage;
 
import com.google.tripmeout.frontend.PlaceVisitModel;
import com.google.tripmeout.frontend.storage.PlaceVisitStorage;
import com.google.common.collect.Tables;
import com.google.common.collect.Table;
import com.google.common.collect.HashBasedTable;
import java.util.Map;
import java.util.List;
import java.util.Collection;
 
@Singleton
public class InMemoryPlaceVisitStorage implements PlaceVisitStorage {
  // <placeId, tripId, PlaceVisitModel> 
  Table<String, String, PlaceVisitModel> storage = Tables.synchronizedTable(HashBasedTable.create());
 
  @Override
  void addPlaceVisit(PlaceVisitModel placeVisit) {
    for (PlaceVisitModel place: storage) {
      if (place.tripId().equals(placeVisit.tripId()) && place.placeId().equals(placeVisit.placeId())) {
        throw Exception;
      }
    }
    storage.put(placeVisit.placeId(), placeVisit.tripId(), placeVisit);
  }

  @Override
  void removePlaceVisit(String tripId, String placeId) {
    if (!storage.remove(placeId, tripId)) {
      throw Exception;
    } 
  }

  @Override
  PlaceVisitModel getPlaceVisit(String tripId, String placeId) {
    PlaceVisitModel place = storage.get(placeId, tripId);
    if (place) {
      return place;
    }
    throw Exception;
  }

  @Override
  void changePlaceVisitStatus(String tripId, String placeId, String newStatus) {
    for (PlaceVisitModel place: storage) {
      if (place.tripId().equals(tripId) && place.placeId().equals(placeId)) {
        PlaceVisitModel updatedPlace = PlaceVisitModel.buildFromStatus(place);
        storage.remove(place);
        storage.add(updatedPlace);
        return;
      }
    }
    throw Exception;
  }

  @Override
  List<PlaceVisitModel> getTripPlaceVisits(String tripId) {
    Map<String, PlaceVisitModel> placeIdPlaceMap = storage.column(tripId);
    Collection<PlaceVisitModel> placeVisits = placeIdPlaceMap.values();

    List<PlaceVisitModel> tripPlaceVisits = new ArrayList<>();
    
    for (PlaceVisitModel place: placeVisits) {
      if (place.userMark().equals("must-see")) {
        tripPlaceVisits.add(place);
      }
    }

    return tripPlaceVisits;
  }
 
}
