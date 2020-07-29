package com.google.tripmeout.frontend.storage;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;

import com.google.tripmeout.frontend.PlaceVisitModel;
import com.google.tripmeout.frontend.error.PlaceVisitAlreadyExistsException;
import com.google.tripmeout.frontend.error.PlaceVisitNotFoundException;
import com.google.tripmeout.frontend.error.TripNotFoundException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import java.util.Optional;

import java.util.UUID;

import javax.swing.tree.ExpandVetoException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PersistentPlaceVisitStorageTest {
    private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    private PersistentPlaceVisitStorage storage;

    @Before
  public void setUp() {
    helper.setUp();
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    storage = new PersistentPlaceVisitStorage(datastore);
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test
  public void getTrip_returnTrip()throws Exception{
    PlaceVisitModel place1 = createPlaceWithTrip1();
    storage.addPlaceVisit(place1);
    Optional<PlaceVisitModel> optionalPlace = storage.getPlaceVisit(place1.tripId(), place1.id());
    assertEquals(optionalPlace.get(), place1);
  }
  
  @Test
  public void getTripPlaceVisits_returnPlacesFromOneTrip() throws Exception{
    PlaceVisitModel place1 = createPlaceWithTrip1();
    PlaceVisitModel place2 = createPlaceWithTrip1();
    PlaceVisitModel place3 = createPlaceWithTrip1();
    storage.addPlaceVisit(place1);
    storage.addPlaceVisit(place2);
    storage.addPlaceVisit(place3);
    assertThat(storage.getTripPlaceVisits("trip1")).containsExactly(place1,place2,place3);
  }

  @Test
  public void updateUserMark_returnTripWithNewUserMark()throws Exception{
    PlaceVisitModel place1 = createPlaceWithTrip1();
    storage.addPlaceVisit(place1);
    storage.updateUserMarkOrAddPlaceVisit(place1, PlaceVisitModel.UserMark.NO);
    assertEquals(PlaceVisitModel.UserMark.NO, storage.getPlaceVisit(place1.tripId(), place1.id()).get().userMark());
  }

  @Test
  public void removePlaceVisit_returnListofTrips()throws Exception{
    PlaceVisitModel place1 = createPlaceWithTrip1();
    PlaceVisitModel place2 = createPlaceWithTrip1();
    PlaceVisitModel place3 = createPlaceWithTrip1();
    PlaceVisitModel place4 = createPlaceWithTrip1();
    storage.addPlaceVisit(place1);
    storage.addPlaceVisit(place2);
    storage.addPlaceVisit(place3);
    storage.addPlaceVisit(place4);
    storage.removePlaceVisit(place4.tripId(), place4.id());
    assertThat(storage.getTripPlaceVisits("trip1")).containsExactly(place1,place2,place3);
  }
   @Test
  public void getTripPlaceVisits_returnListofTrip1()throws Exception{
    PlaceVisitModel place1 = createPlaceWithTrip1();
    PlaceVisitModel place2 = createPlaceWithTrip2();
    PlaceVisitModel place3 = createPlaceWithTrip1();
    PlaceVisitModel place4 = createPlaceWithTrip2();
    storage.addPlaceVisit(place1);
    storage.addPlaceVisit(place2);
    storage.addPlaceVisit(place3);
    storage.addPlaceVisit(place4);
    assertThat(storage.getTripPlaceVisits("trip1")).containsExactly(place1,place3);
    assertThat(storage.getTripPlaceVisits("trip2")).containsExactly(place2,place4);
  }

  @Test
  public void removeTripPlaceVisits_returnListofTrip1()throws Exception{
    PlaceVisitModel place1 = createPlaceWithTrip1();
    PlaceVisitModel place2 = createPlaceWithTrip2();
    PlaceVisitModel place3 = createPlaceWithTrip1();
    PlaceVisitModel place4 = createPlaceWithTrip2();
    storage.addPlaceVisit(place1);
    storage.addPlaceVisit(place2);
    storage.addPlaceVisit(place3);
    storage.addPlaceVisit(place4);
    storage.removeTripPlaceVisits("trip1");
    assertThat(storage.getTripPlaceVisits("trip1")).isEmpty();;
    assertThat(storage.getTripPlaceVisits("trip2")).containsExactly(place2,place4);
  }

  private PlaceVisitModel createPlaceWithTrip1(){
      String id = KeyFactory.keyToString(KeyFactory.createKey("trip1", UUID.randomUUID().toString()));
      return PlaceVisitModel.builder()
      .setId(id)
      .setPlaceName("placeName")
      .setPlacesApiPlaceId("placeId")
      .setTripId("trip1")
      .setUserMark(PlaceVisitModel.UserMark.YES)
      .build();
  }
   private PlaceVisitModel createPlaceWithTrip2(){
      String id = KeyFactory.keyToString(KeyFactory.createKey("trip2", UUID.randomUUID().toString()));
      return PlaceVisitModel.builder()
      .setId(id)
      .setPlaceName("placeName")
      .setPlacesApiPlaceId("placeId")
      .setTripId("trip2")
      .setUserMark(PlaceVisitModel.UserMark.YES)
      .build();
  }
    
}
