package com.google.sps;
import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth8.assertThat;

import com.google.tripmeout.frontend.TripModel;
import com.google.tripmeout.frontend.error.TripAlreadyExistsException;
import com.google.tripmeout.frontend.error.TripNotFoundException;
import com.google.tripmeout.frontend.storage.InMemoryTripModelStorage;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

public class InMemoryTripModelStorageTest {
  public static final TripModel LONDON_TRIP1 = TripModel.builder()
                                                   .setId("a")
                                                   .setName("London Trip")
                                                   .setUserId("person 1")
                                                   .setLocationLat(32.333)
                                                   .setLocationLong(52.1)
                                                   .build();

  public static final TripModel LONDON_TRIP2 = TripModel.builder()
                                                   .setId("b")
                                                   .setName("London Trip")
                                                   .setUserId("person 2")
                                                   .setLocationLat(32.333)
                                                   .setLocationLong(52.1)
                                                   .build();

  public static final TripModel LONDON_TRIP1_2022 = TripModel.builder()
                                                        .setId("c")
                                                        .setName("London Trip 2022")
                                                        .setUserId("person 1")
                                                        .setLocationLat(32.333)
                                                        .setLocationLong(52.1)
                                                        .build();

  public static final TripModel TOKYO_TRIP1 = TripModel.builder()
                                                  .setId("d")
                                                  .setName("Tokyo Trip")
                                                  .setUserId("person 1")
                                                  .setLocationLat(32.333)
                                                  .setLocationLong(147.1112)
                                                  .build();

  public static final TripModel LONDON_TRIP_DUPLICATE_ID = TripModel.builder()
                                                               .setId("a")
                                                               .setName("London Trip")
                                                               .setUserId("person 2")
                                                               .setLocationLat(32.333)
                                                               .setLocationLong(52.1)
                                                               .build();

  /**
   * test that an exception is thrown when adding a TripModel when there is
   * already a TripModel object in storage with the same tripId
   */
  @Test
  public void addTrip_tripWithSameIdAlreadyAdded_throwsException()
      throws TripAlreadyExistsException {
    InMemoryTripModelStorage storage = new InMemoryTripModelStorage();
    storage.addTrip(LONDON_TRIP1);
    Assert.assertThrows(
        TripAlreadyExistsException.class, () -> storage.addTrip(LONDON_TRIP_DUPLICATE_ID));
  }

  /**
   * test that removing a trip from empty storage causes an exception to be
   * thrown
   */
  @Test
  public void removeTrip_emptyStorage_thowsException() throws TripNotFoundException {
    InMemoryTripModelStorage storage = new InMemoryTripModelStorage();
    Assert.assertThrows(TripNotFoundException.class, () -> storage.removeTrip(LONDON_TRIP1.id()));
  }

  /**
   * test that removing a trip from storage when there is not a TripModel object
   * with the given tripId in storage causes an exception to be thrown
   */
  @Test
  public void removeTrip_tripNotInStorage_thowsException()
      throws TripAlreadyExistsException, TripNotFoundException {
    InMemoryTripModelStorage storage = new InMemoryTripModelStorage();
    storage.addTrip(LONDON_TRIP1);
    Assert.assertThrows(
        TripNotFoundException.class, () -> storage.removeTrip(LONDON_TRIP1_2022.id()));
  }

  /**
   * test that getting a trip from empty storage causes an exception to be
   * thrown
   */
  @Test
  public void getTrip_emptyStorage_thowsException() throws TripNotFoundException {
    InMemoryTripModelStorage storage = new InMemoryTripModelStorage();
    Assert.assertThrows(TripNotFoundException.class, () -> storage.getTrip(LONDON_TRIP1.id()));
  }

  /**
   * test that getting a trip from storage when there is not a TripModel object
   * with the given tripId in storage causes an exception to be thrown
   */
  @Test
  public void getTrip_tripNotInStorage_thowsException()
      throws TripAlreadyExistsException, TripNotFoundException {
    InMemoryTripModelStorage storage = new InMemoryTripModelStorage();
    storage.addTrip(LONDON_TRIP1);
    Assert.assertThrows(TripNotFoundException.class, () -> storage.getTrip(LONDON_TRIP1_2022.id()));
  }

  /**
   * test that after adding a TripModel object to storage, getting the TripModel
   * object with the same tripId returns the same TripModel object as the added
   * object
   */
  @Test
  public void addTrip_getTrip_addedTripEqualsGottenTrip()
      throws TripAlreadyExistsException, TripNotFoundException {
    InMemoryTripModelStorage storage = new InMemoryTripModelStorage();
    storage.addTrip(LONDON_TRIP1);
    storage.addTrip(LONDON_TRIP2);
    TripModel gottenTrip = storage.getTrip(LONDON_TRIP2.id());
    assertThat(gottenTrip).isEqualTo(LONDON_TRIP2);
  }

  /**
   * test that error is thrown when updating location fields when there does
   * not exist a TripModel object in storage with the given tripId
   */
  @Test
  public void updateTripLocation_tripNotInStorage_throwsException()
      throws TripAlreadyExistsException, TripNotFoundException {
    InMemoryTripModelStorage storage = new InMemoryTripModelStorage();
    storage.addTrip(LONDON_TRIP1);
    storage.addTrip(LONDON_TRIP2);
    Assert.assertThrows(
        TripNotFoundException.class, () -> storage.updateTripLocation(TOKYO_TRIP1.id(), 22, 19));
  }

  /**
   * test that location fields are updated for the TripModel object in storage
   * with the given tripId after calling updateTripLocation
   */
  @Test
  public void updateTripLocation_tripInStorage_changesLocationLatAndLong()
      throws TripAlreadyExistsException, TripNotFoundException {
    InMemoryTripModelStorage storage = new InMemoryTripModelStorage();
    storage.addTrip(LONDON_TRIP1);
    storage.addTrip(LONDON_TRIP2);
    storage.updateTripLocation(LONDON_TRIP1.id(), 22, 19);
    TripModel newLondonTrip1 = storage.getTrip(LONDON_TRIP1.id());
    assertThat(newLondonTrip1.locationLat()).isWithin(1e-15).of(22);
    assertThat(newLondonTrip1.locationLong()).isWithin(1e-15).of(19);
  }

  /**
   * test that error is thrown when updating name field when there does
   * not exist a TripModel object in storage with the given tripId
   */
  @Test
  public void updateTripName_tripNotInStorage_throwsException()
      throws TripAlreadyExistsException, TripNotFoundException {
    InMemoryTripModelStorage storage = new InMemoryTripModelStorage();
    storage.addTrip(LONDON_TRIP1);
    storage.addTrip(LONDON_TRIP2);
    Assert.assertThrows(TripNotFoundException.class,
        () -> storage.updateTripName(TOKYO_TRIP1.id(), "Yay Tokyo!!!"));
  }

  /**
   * test that name field are updated for the TripModel object in storage
   * with the given tripId after calling updateTripLocation
   */
  @Test
  public void updateTripName_tripInStorage_changesName()
      throws TripAlreadyExistsException, TripNotFoundException {
    InMemoryTripModelStorage storage = new InMemoryTripModelStorage();
    storage.addTrip(LONDON_TRIP1);
    storage.addTrip(LONDON_TRIP2);
    storage.updateTripName(LONDON_TRIP1.id(), "London Trip 2020");
    TripModel newLondonTrip1 = storage.getTrip(LONDON_TRIP1.id());
    assertThat(newLondonTrip1.name()).isEqualTo("London Trip 2020");
  }

  /**
   * test that all TripModel objects in storage with given userId are returned
   * when calling getAllUserTrips when storage only contains one userId
   */
  @Test
  public void getAllUserTrips_onlyOneUserInStorage_returnsAllTripsInStorage()
      throws TripAlreadyExistsException {
    InMemoryTripModelStorage storage = new InMemoryTripModelStorage();
    storage.addTrip(LONDON_TRIP1);
    storage.addTrip(LONDON_TRIP1_2022);
    storage.addTrip(TOKYO_TRIP1);
    List<TripModel> tripsList = storage.getAllUserTrips("person 1");
    assertThat(tripsList).containsExactly(LONDON_TRIP1, LONDON_TRIP1_2022, TOKYO_TRIP1);
  }

  /**
   * test that only the TripModel objects in storage with given userId are
   * returned when calling getAllUserTrips when storage contains multiple userId
   */
  @Test
  public void getAllUserTrips_multipleUsersInStorage_returnsOnlyTripsOfUserInStorage()
      throws TripAlreadyExistsException {
    InMemoryTripModelStorage storage = new InMemoryTripModelStorage();
    storage.addTrip(LONDON_TRIP1);
    storage.addTrip(LONDON_TRIP2);
    storage.addTrip(LONDON_TRIP1_2022);
    storage.addTrip(TOKYO_TRIP1);
    List<TripModel> tripsList = storage.getAllUserTrips("person 1");
    assertThat(tripsList).containsExactly(LONDON_TRIP1, LONDON_TRIP1_2022, TOKYO_TRIP1);
  }

  /**
   * test that empty list is returned when getAllUserTrips is called when
   * storage does not contain any TripModel objects with the given userId
   */
  @Test
  public void getAllUserTrips_userNotInStorage_returnsEmptyList()
      throws TripAlreadyExistsException {
    InMemoryTripModelStorage storage = new InMemoryTripModelStorage();
    storage.addTrip(LONDON_TRIP1);
    storage.addTrip(LONDON_TRIP2);
    storage.addTrip(LONDON_TRIP1_2022);
    storage.addTrip(TOKYO_TRIP1);
    List<TripModel> tripsList = storage.getAllUserTrips("person1");
    assertThat(tripsList).isEmpty();
  }
}
