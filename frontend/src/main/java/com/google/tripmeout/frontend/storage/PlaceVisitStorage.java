package com.google.tripmeout.frontend.storage;

import com.google.tripmeout.frontend.PlaceVisitModel;
import com.google.tripmeout.frontend.error.PlaceVisitAlreadyExistsException;
import com.google.tripmeout.frontend.error.PlaceVisitNotFoundException;
import com.google.tripmeout.frontend.error.TripNotFoundException;
import java.util.List;
import java.util.Optional;

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
   *     in storage with the same tripId and placeUuid as placeVisit
   */
  void addPlaceVisit(PlaceVisitModel placeVisit) throws PlaceVisitAlreadyExistsException;

  /**
   * removes from storage the PlaceVisitModel whose tripId and placeUuid match the
   * given tripId and placeUuid, respectively
   *
   * @param tripId the id to match PlaceVisitModel object's tripId field on
   * @param placeUuid the id to match PlaceVisitModel object's uuid field on
   *
   * @throws a PlaceVisitNotFound exception if there is no PlaceVisitModel
   *     object in storage with the given tripId and placeUuid
   */
  void removePlaceVisit(String tripId, String placeUuid) throws PlaceVisitNotFoundException;

  /**
   * returns the PlaceVisitModel whose tripId and placeUuid match the given tripId
   * and placeUuid, respectively
   *
   * @param tripId the id to match PlaceVisitModel object's tripId field on
   * @param placeUuid the id to match PlaceVisitModel object's uuid field on
   */
  Optional<PlaceVisitModel> getPlaceVisit(String tripId, String placeUuid);

  /**
   * updates the userMark parameter of the PlaceVisitModel object whose tripId
   * and placeUuid match the given tripId and placeUuid, respectively,
   * return the updated PlaceVisitModel
   *
   * @param tripId the id to match PlaceVisitModel object's tripId field on
   * @param placeUuid the id to match PlaceVisitModel object's uuid field on
   * @param newStatus the new status to update placeVisit's userMark field to
   *
   * @throws a PlaceVisitNotFound exception if there is no PlaceVisitModel
   *     object in storage with the given tripId and placeUuid
   */
  PlaceVisitModel updateUserMark(String tripId, String placeUuid,
      PlaceVisitModel.UserMark newStatus) throws PlaceVisitNotFoundException;

  /**
   * updates the userMark parameter of the PlaceVisitModel object whose tripId
   * and placeId match the given tripId and placeId, respectively,
   * if PlaceVisitModel obbject in storage
   * adds PlaceVisitModel object otherwise
   * return true if PlaceVisitModel object with the given tripId and placeId
   * previously exist in storage
   *
   * @param placeVisit the PlaceVisitModel object to update or add
   * @param newStatus the new status to update placeVisit's userMark field to
   */
  boolean updateUserMarkOrAddPlaceVisit(
      PlaceVisitModel placeVisit, PlaceVisitModel.UserMark newStatus);

  /**
   * gets a list of all of the PlaceVisitModel objects whose tripId matches the
   * given tripId
   *
   * @param tripId the id to match PlaceVisitModel object's tripId field on
   */
  List<PlaceVisitModel> getTripPlaceVisits(String tripId);

  /**
   * removes all PlaceVisitModel objects with the given tripId
   *
   * @param tripId the id of the trip to remove all PlaceVisitModel objects for
   *
   * @throws TripNotFoundException if there are no PlaceVisitModel objects with
   *     the given tripId
   */
  void removeTripPlaceVisits(String tripId) throws TripNotFoundException;
}
