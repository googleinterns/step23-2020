package com.google.tripmeout.frontend.storage;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import com.google.tripmeout.frontend.PlaceVisitModel;
import com.google.tripmeout.frontend.error.PlaceVisitAlreadyExistsException;
import com.google.tripmeout.frontend.error.PlaceVisitNotFoundException;
import com.google.tripmeout.frontend.error.TripNotFoundException;
import com.google.tripmeout.frontend.storage.PlaceVisitStorage;
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
    Map<String, PlaceVisitModel> newPlaceMap = new ConcurrentHashMap<>();
    newPlaceMap.put(placeVisit.placeId(), placeVisit);

    Map<String, PlaceVisitModel> alreadyThereMap =
        storage.putIfAbsent(placeVisit.tripId(), newPlaceMap);

    if (alreadyThereMap != null) {
      PlaceVisitModel alreadyTherePlace =
          alreadyThereMap.putIfAbsent(placeVisit.placeId(), placeVisit);
      if (alreadyTherePlace != null) {
        throw new PlaceVisitAlreadyExistsException(
            "PlaceVisit " + placeVisit.name() + " already exists for trip " + placeVisit.tripId());
      }
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
    Map<String, PlaceVisitModel> tripsMap = storage.get(placeId);
    if (tripsMap != null && tripsMap.get(tripId) != null) {
      return Optional.ofNullable(tripsMap.get(tripId));
    }
    return Optional.empty();
  }

  @Override
  public boolean changePlaceVisitStatus(String tripId, String placeId, String newStatus) {
    Map<String, PlaceVisitModel> placesMap = storage.get(tripId);
    if (placesMap == null) {
      return false;
    }

    PlaceVisitModel originalPlace = placesMap.computeIfPresent(
        placeId, (placeKey, place) -> place.toBuilder().setUserMark(newStatus).build());
    if (originalPlace == null) {
      return false;
    }

    return true;
  }

  @Override
  public List<PlaceVisitModel> getTripPlaceVisits(String tripId) {
    Map<String, PlaceVisitModel> placesMap = storage.get(tripId);
    List<PlaceVisitModel> tripPlaceVisits = new ArrayList<>();
    if (placesMap == null) {
      return tripPlaceVisits;
    }
    for (PlaceVisitModel place : placesMap.values()) {
      if (place != null
          && (place.userMark().equals("must-see") || place.userMark().equals("if-time"))) {
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
