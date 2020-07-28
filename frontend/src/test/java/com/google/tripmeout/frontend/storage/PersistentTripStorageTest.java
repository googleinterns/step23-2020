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
import org.junit.Test;

public class PersistentTripStorageTest {
  public static final TripModel LONDON_TRIP1 = TripModel.builder()
                                                   .setId("a")
                                                   .setName("London Trip")
                                                   .setUserId("person 1")
                                                   .setPlacesApiPlaceId("place-id")
                                                   .build();

  public static final TripModel LONDON_TRIP2 = TripModel.builder()
                                                   .setId("b")
                                                   .setName("London Trip")
                                                   .setUserId("person 2")
                                                   .setPlacesApiPlaceId("place-id")
                                                   .build();

  public static final TripModel LONDON_TRIP1_2022 = TripModel.builder()
                                                        .setId("c")
                                                        .setName("London Trip 2022")
                                                        .setUserId("person 1")
                                                        .setPlacesApiPlaceId("place-id")
                                                        .build();

  public static final TripModel TOKYO_TRIP1 = TripModel.builder()
                                                  .setId("d")
                                                  .setName("Tokyo Trip")
                                                  .setUserId("person 1")
                                                  .setPlacesApiPlaceId("place-id")
                                                  .build();

  public static final TripModel LONDON_TRIP_DUPLICATE_ID = TripModel.builder()
                                                               .setId("a")
                                                               .setName("London Trip")
                                                               .setUserId("person 2")
                                                               .setPlacesApiPlaceId("place-id")
                                                               .build();

  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  @Before
  public void setUp() {
    helper.setUp();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test
  public void addTrip_getTrip() throws TripAlreadyExistsException, TripNotFoundException {
    PersistentTripStorage storage = new PersistentTripStorage();
    storage.addTrip(TOKYO_TRIP1);
    storage.addTrip(LONDON_TRIP1);
    TripModel gottenTrip = storage.getTrip(LONDON_TRIP1.id());
    assertThat(gottenTrip).isEqualTo(LONDON_TRIP1);
  }
}