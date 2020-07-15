package com.google.tripmeout.frontend.storage;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import com.google.tripmeout.frontend.PlaceVisitModel;
import com.google.tripmeout.frontend.error.PlaceVisitAlreadyExistsException;
import com.google.tripmeout.frontend.error.PlaceVisitNotFoundException;
import com.google.tripmeout.frontend.error.TripNotFoundException;
import com.google.tripmeout.frontend.storage.PlaceVisitStorage;
import java.lang.RuntimeException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryPlaceVisitStorage implements PlaceVisitStorage {
  // <tripId, placeId, PlaceVisitModel>
  Map<String, Map<String, PlaceVisitModel>> storage = new ConcurrentHashMap<>();

  @Override
  public void addPlaceVisit(PlaceVisitModel placeVisit) throws PlaceVisitAlreadyExistsException {
    try {
      storage.compute(placeVisit.tripId(), (tripKey, placesMap) -> {
        if (placesMap == null) {
          Map<String, PlaceVisitModel> newPlaceMap = new ConcurrentHashMap<>();
          newPlaceMap.put(placeVisit.placeId(), placeVisit);
          return newPlaceMap;
        }
        if (placesMap.get(placeVisit.placeId()) != null) {
          throw new RuntimeException(new PlaceVisitAlreadyExistsException("PlaceVisit "
              + placeVisit.name() + " already exists for trip " + placeVisit.tripId()));
        }
        placesMap.put(placeVisit.placeId(), placeVisit);
        return placesMap;
      });
    } catch (RuntimeException e) {
      if (e.getCause() instanceof PlaceVisitAlreadyExistsException) {
        throw(PlaceVisitAlreadyExistsException) e.getCause();
      }
      throw e;
    }
  }

  @Override
  public void removePlaceVisit(String tripId, String placeId) throws PlaceVisitNotFoundException {
    Map<String, PlaceVisitModel> placesMap = storage.get(tripId);
    if (placesMap == null) {
      throw new PlaceVisitNotFoundException(
          "PlaceVisit with id" + placeId + " not found for trip " + tripId);
    }

    if (placesMap.remove(placeId) == null) {
      throw new PlaceVisitNotFoundException(
          "PlaceVisit with id" + placeId + " not found for trip " + tripId);
    }
  }

  @Override
  public Optional<PlaceVisitModel> getPlaceVisit(String tripId, String placeId) {
    return Optional.ofNullable(storage.get(tripId)).map(placesMap -> placesMap.get(placeId));
  }

  @Override
  public boolean updateUserMarkOrAddPlaceVisit(
      PlaceVisitModel placeVisit, PlaceVisitModel.UserMark newStatus) {
    synchronized (storage) {
      Map<String, PlaceVisitModel> placesMap = storage.get(placeVisit.tripId());
      if (placesMap == null) {
        Map<String, PlaceVisitModel> newPlaceMap = new ConcurrentHashMap<>();
        newPlaceMap.put(placeVisit.placeId(), placeVisit);
        storage.put(placeVisit.tripId(), newPlaceMap);
        return false;
      }
      if (placesMap.get(placeVisit.placeId()) != null) {
        placesMap.put(placeVisit.placeId(),
            placesMap.get(placeVisit.placeId()).toBuilder().setUserMark(newStatus).build());
        return true;
      } else {
        placesMap.put(placeVisit.placeId(), placeVisit);
        return false;
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
    for (PlaceVisitModel place : placesMap.values()) {
      if (place != null) {
        tripPlaceVisits.add(place);
      }
    }

    return tripPlaceVisits;
  }

  @Override
  public void removeTripPlaceVisits(String tripId) throws TripNotFoundException {
    if (storage.remove(tripId) == null) {
      throw new TripNotFoundException("No PlaceVisitModel objects found with tripId: " + tripId);
    }
  }
}
