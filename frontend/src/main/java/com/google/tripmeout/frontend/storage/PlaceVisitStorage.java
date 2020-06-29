package com.google.tripmeout.frontend.storage;

import com.google.tripmeout.frontend.PlaceVisitModel;

/**
 * stores PlaceVisit objects
 */
public interface PlaceVisitStorage {
  /**
   * adds a PlaceVisitModel object to storage
   * 
   * @param placeVisit the PlaceVisitModel object to add to storage
   *
   * @throws a PlaceAlreadyExists exception if there is a PlaceVisitModel in 
   *     storage with the same tripId and placeId as placeVisit
   */
  public void addPlaceVisit(PlaceVisitModel placeVisit);

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
  public void removePlaceVisit(String tripId, String placeId);

  /**
   * returns the PlaceVisitModel whose tripId and placeId match the given tripId
   * and placeId, respectively
   *
   * @param tripId the id to match PlaceVisitModel object's placeId field on
   * @param placeId the id to match PlaceVisitModel object's placeId field on
   *
   * @throws a PlaceVisitNotFound exception if there is no PlaceVisitModel 
   * object in storage with the given tripId and placeId 
   */
  public PlaceVisitModel getPlaceVisit(String tripId, String placeId);

  /**
   * updates the userMark parameter of the PlaceVisitModel object whose tripId 
   * and placeId match the given tripId and placeId, respectively
   *
   * @param tripId the id to match PlaceVisitModel object's tripId field on
   * @param placeId the id to match PlaceVisitModel object's placeId field on
   *
   * @throws a PlaceVisitNotFound exception if there is no PlaceVisitModel 
   *     object in storage with the given tripId and placeId 
   */
  public void changePlaceVisitStatus(String tripId, String placeId, String newStatus);

  /**
   * gets a list of all of the PlaceVisitModel objects whose tripId matches the 
   * given tripId
   *
   * @param tripId the id to match PlaceVisitModel object's placeId field on
   */
  public List<PlaceVisitModel> getTripPlaceVisits(String tripId);
}
