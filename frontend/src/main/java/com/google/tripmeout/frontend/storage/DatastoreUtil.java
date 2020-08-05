package com.google.tripmeout.frontend.storage;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

class DatastoreUtil {
    public static final String TRIP_ENTITY_TYPE = "Trip";
    public static final String PLACE_VISIT_ENTITY_TYPE = "PlaceVisit";

    public static Key tripKey(String tripId) {
        return KeyFactory.createKey(TRIP_ENTITY_TYPE, tripId);
    }

    public static Key placeVisitKey(String tripId, String placeVisitId) {
        return tripKey(tripId).getChild(PLACE_VISIT_ENTITY_TYPE, placeVisitId);
    }

    private DatastoreUtil() {}
}