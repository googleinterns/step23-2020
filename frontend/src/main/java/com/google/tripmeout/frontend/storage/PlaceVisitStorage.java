package com.google.tripmeout.frontend.storage;

import com.google.tripmeout.frontend.PlaceVisitModel;
import com.google.tripmeout.frontend.error.PlaceVisitAlreadyExistsException;
import com.google.tripmeout.frontend.error.PlaceVisitNotFoundException;
import java.util.List;

/**
 * stores PlaceVisit objects
 */
public interface PlaceVisitStorage {
  /**
   * adds a PlaceVisitModel object to storage
   *
   * @param placeVisit the PlaceVisitModel object to add to storage
   *
   * @throws a PlaceVisitAlreadyExists exception if there is a PlaceVisitModel
   *     in storage with the same tripId and placeId as placeVisit
   */
  void addPlaceVisit(PlaceVisitModel placeVisit) throws PlaceVisitAlreadyExistsException;

  /**
   * removes from storage the PlaceVisitModel whose tripId and placeId match the
   * given tripId and placeId, respectively
   *
   * @param tripId the id to match PlaceVisitModel object's placeId field on
   * @param placeId the id to match PlaceVisitModel object's placeId field on
   *
   * @throws a PlaceVisitNotFound exception if there is no PlaceVisitModel
   *     object in storage with the given tripId and placeId
   */
  void removePlaceVisit(String tripId, String placeId) throws PlaceVisitNotFoundException;

  /**
   * returns the PlaceVisitModel whose tripId and placeId match the given tripId
   * and placeId, respectively
   *
   * @param tripId the id to match PlaceVisitModel object's placeId field on
   * @param placeId the id to match PlaceVisitModel object's placeId field on
   *
   * @throws a PlaceVisitNotFound exception if there is no PlaceVisitModel
   *     object in storage with the given tripId and placeId
   */
  PlaceVisitModel getPlaceVisit(String tripId, String placeId) throws PlaceVisitNotFoundException;

  /**
   * updates the userMark parameter of the PlaceVisitModel object whose tripId
   * and placeId match the given tripId and placeId, respectively
   * return true if PlaceVisitModel object with the given tripId and placeId
   * exist in storage
   *
   * @param tripId the id to match PlaceVisitModel object's tripId field on
   * @param placeId the id to match PlaceVisitModel object's placeId field on
   */
  boolean changePlaceVisitStatus(String tripId, String placeId, String newStatus);

  /**
   * gets a list of all of the PlaceVisitModel objects whose tripId matches the
   * given tripId
   *
   * @param tripId the id to match PlaceVisitModel object's placeId field on
   */
  List<PlaceVisitModel> getTripPlaceVisits(String tripId);
}
