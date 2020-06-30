package com.google.tripmeout.frontend.storage;
 
import com.google.tripmeout.frontend.PlaceVisitModel;
import com.google.tripmeout.frontend.storage.PlaceVisitStorage;
import java.util.List;
 
@Singleton
public class InMemoryPlaceVisitStorage implements PlaceVisitStorage {
  List<PlaceVisitModel> storage = new ArrayList<>();
 
  @Override
  void addPlaceVisit(PlaceVisitModel placeVisit) {
    for (PlaceVisit place: storage) {
      if (place.tripId().equals(placeVisit.tripId()) && place.placeId().equals(placeVisit.placeId())) {
        throw Exception;
      }
    }
    storage.add(placeVisit);
  }

  @Override
  void removePlaceVisit(String tripId, String placeId) {
    for (PlaceVisit place: storage) {
      if (place.tripId().equals(tripId) && place.placeId().equals(placeId)) {
        storage.remove(place);
        return;
      }
    }
    throw Exception;
  }

  @Override
  PlaceVisit getPlaceVisit(String tripId, String placeId) {
    for (PlaceVisit place: storage) {
      if (place.tripId().equals(tripId) && place.placeId().equals(placeId)) {
        return place;
      }
    }
    throw Exception;
  }

  @Override
  void changePlaceVisitStatus(String tripId, String placeId, String newStatus) {
    for (PlaceVisit place: storage) {
      if (place.tripId().equals(tripId) && place.placeId().equals(placeId)) {
        Builder placeValueBuilder = PlaceVisitModel.builder();
        PlaceValueModel updatedPlace = placeValueBuilder
          .setTripId(tripId)
          .setPlaceId(placeId)
          .setName(place.name())
          .setLatitude(place.latitude())
          .setLongitude(place.longitude())
          .setUserMark(newStatus);
        storage.remove(place);
        storage.add(updatedPlace);
        return;
      }
    }
    throw Exception;
  }

  @Override
  List<PlaceVisitModel> getTripPlaceVisits(String tripId) {
    List<PlaceVisitModel> tripPlaceVisits = new ArrayList<>();
    for (PlaceVisit place: storage) {
      if (place.tripId().equals(tripId)) {
        tripPlaceVisits.add(place);
      }
    }
    return tripPlaceVisits;
  }
 
}
