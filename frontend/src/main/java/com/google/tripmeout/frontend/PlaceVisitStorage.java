package com.google.tripmeout.frontend;

<<<<<<< HEAD
import com.google.tripmeout.frontend.PlaceVisitModel;

=======
>>>>>>> e878473... create PlaceVisit and Trip Storage interfaces
public interface PlaceVisitStorage {
  public void addPlaceVisit(PlaceVisitModel placeVisit);
  public void removePlaceVisit(String tripId, String placeId);
  public PlaceVisitModel getPlaceVisit(String tripId, String placeId);
  public void changePlaceVisitStatus(String tripId, String placeId, String newStatus);
<<<<<<< HEAD
}
=======
}
>>>>>>> e878473... create PlaceVisit and Trip Storage interfaces
