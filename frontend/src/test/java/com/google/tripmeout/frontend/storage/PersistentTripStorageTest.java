package com.google.tripmeout.frontend.storage;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.tripmeout.frontend.TripModel;
import com.google.tripmeout.frontend.error.TripAlreadyExistsException;
import com.google.tripmeout.frontend.error.TripNotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class PersistentTripStorageTest {
  public static final TripModel LONDON_TRIP1 = TripModel.builder()
                                                   .setId("agg")
                                                   .setName("London Trip")
                                                   .setUserId("person 1")
                                                   .setPlacesApiPlaceId("place-id")
                                                   .build();

  public static final TripModel CANCUN_TRIP = TripModel.builder()
                                                  .setId("bhh")
                                                  .setName("Cancun Trip")
                                                  .setUserId("person 2")
                                                  .setPlacesApiPlaceId("place-id")
                                                  .build();

  public static final TripModel NEW_JERSEY_TRIP = TripModel.builder()
                                                      .setId("ctt")
                                                      .setName("New Jersey Trip")
                                                      .setUserId("person 1")
                                                      .setPlacesApiPlaceId("place-id")
                                                      .build();

  public static final TripModel TOKYO_TRIP1 = TripModel.builder()
                                                  .setId("dhh")
                                                  .setName("Tokyo Trip")
                                                  .setUserId("person 1")
                                                  .setPlacesApiPlaceId("place-id")
                                                  .build();

  public static final TripModel LONDON_TRIP_DUPLICATE_ID = TripModel.builder()
                                                               .setId("ahh")
                                                               .setName("London Trip")
                                                               .setUserId("person 2")
                                                               .setPlacesApiPlaceId("place-id")
                                                               .build();

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
  public void addTrip_getTrip() throws TripAlreadyExistsException, TripNotFoundException {
    storage.addTrip(TOKYO_TRIP1);
    storage.addTrip(LONDON_TRIP1);
    TripModel gottenTrip = storage.getTrip(LONDON_TRIP1.id());
    assertThat(gottenTrip).isEqualTo(LONDON_TRIP1);
  }

  @Test
  public void getAllUserTrips_withOneUser() throws TripAlreadyExistsException {
    storage.addTrip(TOKYO_TRIP1);
    storage.addTrip(LONDON_TRIP1);
    storage.addTrip(NEW_JERSEY_TRIP);
    assertThat(storage.getAllUserTrips("person 1"))
        .containsExactly(TOKYO_TRIP1, LONDON_TRIP1, NEW_JERSEY_TRIP);
  }

  @Test
  public void getAllUserTrips_withMultipleUsers() throws TripAlreadyExistsException {
    storage.addTrip(TOKYO_TRIP1);
    storage.addTrip(LONDON_TRIP1);
    storage.addTrip(CANCUN_TRIP);
    storage.addTrip(NEW_JERSEY_TRIP);
    assertThat(storage.getAllUserTrips("person 1"))
        .containsExactly(TOKYO_TRIP1, LONDON_TRIP1, NEW_JERSEY_TRIP);
  }

  @Test
  public void removeTrip_returnTrips() throws Exception {
    storage.addTrip(TOKYO_TRIP1);
    storage.addTrip(LONDON_TRIP1);
    storage.addTrip(NEW_JERSEY_TRIP);
    storage.removeTrip(NEW_JERSEY_TRIP.id());
    assertThat(storage.getAllUserTrips("person 1")).containsExactly(TOKYO_TRIP1, LONDON_TRIP1);
  }

  @Test
  public void updateTripName_returnTripWithNewName() throws Exception {
    storage.addTrip(NEW_JERSEY_TRIP);
    storage.updateTripName(NEW_JERSEY_TRIP.id(), "JERSEY 2020");
    assertEquals("JERSEY 2020", storage.getTrip(NEW_JERSEY_TRIP.id()).name());
  }
}
