package com.google.sps;
import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth8.assertThat;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.List;
import com.google.tripmeout.frontend.storage.InMemoryTripModelStorage;
import com.google.tripmeout.frontend.TripModel;
import com.google.tripmeout.frontend.error.TripAlreadyExistsException;
import com.google.tripmeout.frontend.error.TripNotFoundException;


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


  @Test
  public void addTrip_tripAlreadyAdded_throwsException() 
      throws TripAlreadyExistsException {
    InMemoryTripModelStorage storage = new InMemoryTripModelStorage();
    storage.addTrip(LONDON_TRIP1);
    Assert.assertThrows(TripAlreadyExistsException.class, () -> 
        storage.addTrip(LONDON_TRIP1));
    
  }

  @Test
  public void addTrip_tripWithSameIdAlreadyAdded_throwsException() 
      throws TripAlreadyExistsException {
    InMemoryTripModelStorage storage = new InMemoryTripModelStorage();
    storage.addTrip(LONDON_TRIP1);
    Assert.assertThrows(TripAlreadyExistsException.class, () -> 
        storage.addTrip(LONDON_TRIP_DUPLICATE_ID));
    
  }

  @Test
  public void addTrip_tripNotInStorage_exceptionNotThrown() 
      throws TripAlreadyExistsException {
    InMemoryTripModelStorage storage = new InMemoryTripModelStorage();
    storage.addTrip(LONDON_TRIP1);
    storage.addTrip(LONDON_TRIP2);
  }

  @Test
  public void removeTrip_emptyStorage_thowsException() 
      throws TripNotFoundException {
    InMemoryTripModelStorage storage = new InMemoryTripModelStorage();
    Assert.assertThrows(TripNotFoundException.class, () -> 
        storage.removeTrip(LONDON_TRIP1.id()));
  }

  @Test
  public void removeTrip_tripNotInStorage_thowsException() 
      throws TripAlreadyExistsException, TripNotFoundException {
    InMemoryTripModelStorage storage = new InMemoryTripModelStorage();
    storage.addTrip(LONDON_TRIP1);
    Assert.assertThrows(TripNotFoundException.class, () -> 
        storage.removeTrip(LONDON_TRIP1_2022.id()));
  }

  @Test
  public void getTrip_emptyStorage_thowsException() throws TripNotFoundException {
    InMemoryTripModelStorage storage = new InMemoryTripModelStorage();
    Assert.assertThrows(TripNotFoundException.class, () -> 
        storage.getTrip(LONDON_TRIP1.id()));
  }

  @Test
  public void getTrip_tripNotInStorage_thowsException() 
      throws TripAlreadyExistsException, TripNotFoundException {
    InMemoryTripModelStorage storage = new InMemoryTripModelStorage();
    storage.addTrip(LONDON_TRIP1);
    Assert.assertThrows(TripNotFoundException.class, () -> 
        storage.getTrip(LONDON_TRIP1_2022.id()));
  }

  @Test
  public void addTrip_getTrip_addedTripEqualsGottenTrip() 
      throws TripAlreadyExistsException, TripNotFoundException {
    InMemoryTripModelStorage storage = new InMemoryTripModelStorage();
    storage.addTrip(LONDON_TRIP1);
    storage.addTrip(LONDON_TRIP2);
    TripModel gottenTrip = storage.getTrip(LONDON_TRIP2.id());
    Assert.assertEquals(LONDON_TRIP2, gottenTrip);
  }

  @Test
  public void updateTripLocation_tripNotInStorage_throwsException() 
      throws TripAlreadyExistsException, TripNotFoundException {
    InMemoryTripModelStorage storage = new InMemoryTripModelStorage();
    storage.addTrip(LONDON_TRIP1);
    storage.addTrip(LONDON_TRIP2);
    Assert.assertThrows(TripNotFoundException.class, () -> 
        storage.updateTripLocation(TOKYO_TRIP1.id(), 22, 19));
  }

  @Test
  public void updateTripLocation_tripInStorage_changesLocationLatAndLong() 
      throws TripAlreadyExistsException, TripNotFoundException {
    InMemoryTripModelStorage storage = new InMemoryTripModelStorage();
    storage.addTrip(LONDON_TRIP1);
    storage.addTrip(LONDON_TRIP2);
    storage.updateTripLocation(LONDON_TRIP1.id(), 22, 19);
    TripModel newLondonTrip1 = storage.getTrip(LONDON_TRIP1.id());
    Assert.assertEquals(22, newLondonTrip1.locationLat(), 1e-15);
    Assert.assertEquals(19, newLondonTrip1.locationLong(), 1e-15);
  }

  @Test
  public void updateTripName_tripNotInStorage_throwsException() 
      throws TripAlreadyExistsException, TripNotFoundException {
    InMemoryTripModelStorage storage = new InMemoryTripModelStorage();
    storage.addTrip(LONDON_TRIP1);
    storage.addTrip(LONDON_TRIP2);
    Assert.assertThrows(TripNotFoundException.class, () -> 
        storage.updateTripName(TOKYO_TRIP1.id(), "Yay Tokyo!!!"));
  }

  @Test
  public void updateTripName_tripInStorage_changesName() 
      throws TripAlreadyExistsException, TripNotFoundException {
    InMemoryTripModelStorage storage = new InMemoryTripModelStorage();
    storage.addTrip(LONDON_TRIP1);
    storage.addTrip(LONDON_TRIP2);
    storage.updateTripName(LONDON_TRIP1.id(), "London Trip 2020");
    TripModel newLondonTrip1 = storage.getTrip(LONDON_TRIP1.id());
    Assert.assertEquals(newLondonTrip1.name(), "London Trip 2020");
  }

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

  @Test
  public void getAllUserTrips_userNotInStorage_returnsEmptyList() 
      throws TripAlreadyExistsException{
    InMemoryTripModelStorage storage = new InMemoryTripModelStorage();
    storage.addTrip(LONDON_TRIP1);
    storage.addTrip(LONDON_TRIP2);
    storage.addTrip(LONDON_TRIP1_2022);
    storage.addTrip(TOKYO_TRIP1);
    List<TripModel> tripsList = storage.getAllUserTrips("person1");
    assertThat(tripsList).isEmpty();
  }
}
