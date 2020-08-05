package com.google.tripmeout.frontend.storage;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.tripmeout.frontend.TripModel;
import com.google.tripmeout.frontend.error.TripAlreadyExistsException;
import com.google.tripmeout.frontend.error.TripNotFoundException;
import java.util.UUID;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PersistentTripStorageTest {
  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
  private PersistentTripStorage storage;

  @Before
  public void setUp() {
    helper.setUp();
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    storage = new PersistentTripStorage(datastore);
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test
  public void addTrip_getTrip() throws Exception {
    TripModel TOKYO_TRIP1 = createTripForUser1();
    TripModel LONDON_TRIP1 = createTripForUser1();
    storage.addTrip(TOKYO_TRIP1);
    storage.addTrip(LONDON_TRIP1);
    TripModel gottenTrip = storage.getTrip(LONDON_TRIP1.id());
    assertThat(gottenTrip).isEqualTo(LONDON_TRIP1);
  }

  @Test
  public void addTrip_returnTripAlreadyExistsException() throws Exception {
    TripModel TOKYO_TRIP1 = createTripForUser1();
    storage.addTrip(TOKYO_TRIP1);
    Assert.assertThrows(TripAlreadyExistsException.class, () -> storage.addTrip(TOKYO_TRIP1));
  }

  @Test
  public void getTrip_returnTripNotFoundException() throws Exception {
    TripModel TOKYO_TRIP1 = createTripForUser1();
    storage.addTrip(TOKYO_TRIP1);
    Assert.assertThrows(TripNotFoundException.class, () -> storage.getTrip(UUID.randomUUID().toString()));
  }

  @Test
  public void getAllUserTrips_withOneUser() throws Exception {
    TripModel TOKYO_TRIP1 = createTripForUser1();
    TripModel LONDON_TRIP1 = createTripForUser1();
    TripModel NEW_JERSEY_TRIP = createTripForUser1();
    storage.addTrip(TOKYO_TRIP1);
    storage.addTrip(LONDON_TRIP1);
    storage.addTrip(NEW_JERSEY_TRIP);
    assertThat(storage.getAllUserTrips("user1"))
        .containsExactly(TOKYO_TRIP1, LONDON_TRIP1, NEW_JERSEY_TRIP);
  }

  @Test
  public void getAllUserTrips_returnEmptyList() throws Exception {
    TripModel TOKYO_TRIP1 = createTripForUser1();
    TripModel LONDON_TRIP1 = createTripForUser1();
    TripModel NEW_JERSEY_TRIP = createTripForUser1();
    storage.addTrip(TOKYO_TRIP1);
    storage.addTrip(LONDON_TRIP1);
    storage.addTrip(NEW_JERSEY_TRIP);
    assertThat(storage.getAllUserTrips("user2")).isEmpty();
    ;
  }

  @Test
  public void getAllUserTrips_withMultipleUsers_returnUser1() throws Exception {
    TripModel TOKYO_TRIP1 = createTripForUser1();
    TripModel LONDON_TRIP1 = createTripForUser1();
    TripModel NEW_JERSEY_TRIP = createTripForUser1();
    TripModel CANCUN_TRIP = createTripForUser2();
    storage.addTrip(TOKYO_TRIP1);
    storage.addTrip(LONDON_TRIP1);
    storage.addTrip(CANCUN_TRIP);
    storage.addTrip(NEW_JERSEY_TRIP);
    assertThat(storage.getAllUserTrips("user1"))
        .containsExactly(TOKYO_TRIP1, LONDON_TRIP1, NEW_JERSEY_TRIP);
  }

  @Test
  public void getAllUserTrips_withMultipleUsers_returnUser2() throws Exception {
    TripModel TOKYO_TRIP1 = createTripForUser1();
    TripModel LONDON_TRIP1 = createTripForUser1();
    TripModel NEW_JERSEY_TRIP = createTripForUser1();
    TripModel CANCUN_TRIP = createTripForUser2();
    storage.addTrip(TOKYO_TRIP1);
    storage.addTrip(LONDON_TRIP1);
    storage.addTrip(CANCUN_TRIP);
    storage.addTrip(NEW_JERSEY_TRIP);
    assertThat(storage.getAllUserTrips("user2")).containsExactly(CANCUN_TRIP);
  }

  @Test
  public void removeTrip_returnTrips() throws Exception {
    TripModel TOKYO_TRIP1 = createTripForUser1();
    TripModel LONDON_TRIP1 = createTripForUser1();
    TripModel NEW_JERSEY_TRIP = createTripForUser1();
    storage.addTrip(TOKYO_TRIP1);
    storage.addTrip(LONDON_TRIP1);
    storage.addTrip(NEW_JERSEY_TRIP);
    storage.removeTrip(NEW_JERSEY_TRIP.id());
    assertThat(storage.getAllUserTrips("user1")).containsExactly(TOKYO_TRIP1, LONDON_TRIP1);
  }
  @Test
  public void removeTrip_returnTripNotFoundException() throws Exception {
    TripModel NEW_JERSEY_TRIP = createTripForUser1();
    storage.addTrip(NEW_JERSEY_TRIP);
    Assert.assertThrows(TripNotFoundException.class, () -> storage.removeTrip(UUID.randomUUID().toString()));
  }

  @Test
  public void updateTripName_returnTripWithNewName() throws Exception {
    TripModel NEW_JERSEY_TRIP = createTripForUser1();
    storage.addTrip(NEW_JERSEY_TRIP);
    storage.updateTripName(NEW_JERSEY_TRIP.id(), "JERSEY 2020");
    assertEquals("JERSEY 2020", storage.getTrip(NEW_JERSEY_TRIP.id()).name());
  }

  @Test
  public void updateTripName_returnTripNotFoundException() throws Exception {
    TripModel NEW_JERSEY_TRIP = createTripForUser1();
    storage.addTrip(NEW_JERSEY_TRIP);
    Assert.assertThrows(
        TripNotFoundException.class, () -> storage.updateTripName(UUID.randomUUID().toString(), "JERSEY 2020"));
  }

  private static TripModel createTripForUser2() {
    return TripModel.builder()
        .setId(UUID.randomUUID().toString())
        .setName("name")
        .setUserId("user2")
        .setPlacesApiPlaceId("place-api-place-id")
        .build();
  }

  private static TripModel createTripForUser1() {
    return TripModel.builder()
        .setId(UUID.randomUUID().toString())
        .setName("name")
        .setUserId("user1")
        .setPlacesApiPlaceId("place-api-place-id")
        .build();
  }
}
