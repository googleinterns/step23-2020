package com.google.sps;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth8.assertThat;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.List;
import com.google.tripmeout.frontend.storage.InMemoryPlaceVisitStorage;
import com.google.tripmeout.frontend.PlaceVisitModel;
import com.google.tripmeout.frontend.error.PlaceVisitAlreadyExistsException;
import com.google.tripmeout.frontend.error.PlaceVisitNotFoundException;

@RunWith(JUnit4.class)
public final class InMemoryPlaceVisitStorageTest {

  // PlaceVisitModel objects to use in testing
  private static final PlaceVisitModel LONDON = PlaceVisitModel.builder()
    .setPlaceId("LCY")
    .setName("London, UK")
    .setTripId("a")
    .setUserMark("don't-care")
    .setLatitude(56)
    .setLongitude(23.64)
    .build();

  private static final PlaceVisitModel ROME = PlaceVisitModel.builder()
    .setPlaceId("FCO")
    .setName("Rome, Italy")
    .setTripId("a")
    .setUserMark("must-see")
    .setLatitude(44.32)
    .setLongitude(32.1244)
    .build();

  private static final PlaceVisitModel PARIS = PlaceVisitModel.builder()
    .setPlaceId("CDG")
    .setName("Paris, France")
    .setTripId("a")
    .setUserMark("if-time")
    .setLatitude(48.3288)
    .setLongitude(34)
    .build();

  private static final PlaceVisitModel TOKYO = PlaceVisitModel.builder()
    .setPlaceId("HND")
    .setName("Tokyo, Japan")
    .setTripId("a")
    .setUserMark("must-see")
    .setLatitude(35.32)
    .setLongitude(139.33)
    .build();

  private static final PlaceVisitModel BEIJING = PlaceVisitModel.builder()
    .setPlaceId("PEK")
    .setName("Beijing, China")
    .setTripId("b")
    .setUserMark("must-see")
    .setLatitude(35.32)
    .setLongitude(125.33)
    .build();

  private static final PlaceVisitModel SEOUL = PlaceVisitModel.builder()
    .setPlaceId("ICN")
    .setName("Seoul, South Korea")
    .setTripId("b")
    .setUserMark("if-time")
    .setLatitude(26.32)
    .setLongitude(135.33)
    .build();

  private static final PlaceVisitModel TOKYO_B = PlaceVisitModel.builder()
    .setPlaceId("HND")
    .setName("Tokyo, Japan")
    .setTripId("b")
    .setUserMark("must-see")
    .setLatitude(35.32)
    .setLongitude(139.33)
    .build();

  private static final PlaceVisitModel SEOUL_B = PlaceVisitModel.builder()
    .setPlaceId("ICN")
    .setName("some city in Korea")
    .setTripId("b")
    .setUserMark("if-time")
    .setLatitude(0)
    .setLongitude(0)
    .build();

  /** 
   * test that a PlaceVisitAlreadyExistsException is thrown when a place that 
   * has already been added to the storage is added again 
   */
  @Test
  public void addPlaceVisit_placeAlreadyAdded_throwsException()
      throws PlaceVisitAlreadyExistsException {
    InMemoryPlaceVisitStorage storage = new InMemoryPlaceVisitStorage();
    storage.addPlaceVisit(LONDON);
    Assert.assertThrows(PlaceVisitAlreadyExistsException.class, () -> storage.addPlaceVisit(LONDON));
  }

  /** 
   * test that a PlaceVisitAlreadyExistsException is thrown when a place is  
   * added but another place with the same placeId and tripId has already been 
   * added to the storage
   */
  @Test
  public void addPlaceVisit_samePlaceIdAndTripIdAlreadyAdded_throwsException()
      throws PlaceVisitAlreadyExistsException {
    InMemoryPlaceVisitStorage storage = new InMemoryPlaceVisitStorage();
    storage.addPlaceVisit(SEOUL);
    Assert.assertThrows(PlaceVisitAlreadyExistsException.class, () -> storage.addPlaceVisit(SEOUL_B));
  }

  /**
   * test that an exception is not thrown when two PlaceVisitModel objects
   * with different tripId and/or placeId are added to storage
   */
  @Test
  public void addPlaceVisit_sameLocationDiffTripAdded_noExceptionThrown()
      throws PlaceVisitAlreadyExistsException {
    InMemoryPlaceVisitStorage storage = new InMemoryPlaceVisitStorage();
    storage.addPlaceVisit(TOKYO);
    storage.addPlaceVisit(TOKYO_B);
  }

  /**
   * test that a PlaceNotFoundException is thrown when getting a place from
   * an empty storage
   */
  @Test
  public void getPlaceVisit_emptyStorage_returnsEmptyOptional() {
    InMemoryPlaceVisitStorage storage = new InMemoryPlaceVisitStorage();
    Assert.assertFalse(storage.getPlaceVisit(TOKYO.tripId(), TOKYO.placeId()).isPresent());
  }

  /**
   * test that a PlaceNotFoundException is thrown when trying to get a place
   * from storage when there is no place in storage with the given tripId and
   * placeId
   */
  @Test
  public void getPlaceVisit_placeNotInStorage_returnsEmptyOptional()
      throws PlaceVisitAlreadyExistsException, PlaceVisitNotFoundException {
    InMemoryPlaceVisitStorage storage = new InMemoryPlaceVisitStorage();
    storage.addPlaceVisit(TOKYO);
    storage.addPlaceVisit(TOKYO_B);
    Assert.assertFalse(storage.getPlaceVisit(SEOUL.tripId(), SEOUL.placeId()).isPresent());
  }

  /**
   * test getPlaceVisit when PlaceVisitModel object with given tripId and 
   * placeId exist in storage
   */
  @Test
  public void addPlaceVisitGetPlaceVisit_placeAddedEqualsPlaceGot()
      throws PlaceVisitAlreadyExistsException, PlaceVisitNotFoundException {
    InMemoryPlaceVisitStorage storage = new InMemoryPlaceVisitStorage();
    storage.addPlaceVisit(TOKYO);
    storage.addPlaceVisit(TOKYO_B);

    PlaceVisitModel tokyo = storage.getPlaceVisit(TOKYO.tripId(), TOKYO.placeId()).get();
    Assert.assertEquals(tokyo, TOKYO);

    PlaceVisitModel tokyoB = storage.getPlaceVisit(TOKYO_B.tripId(), TOKYO_B.placeId()).get();
    Assert.assertEquals(tokyoB, TOKYO_B);

  }

  /**
   * test that a PlaceNotFoundException is thrown when removing a place from
   * an empty storage
   */
  @Test
  public void removePlaceVisit_emptyStorage_throwsException()
      throws PlaceVisitNotFoundException {
    InMemoryPlaceVisitStorage storage = new InMemoryPlaceVisitStorage();
    Assert.assertThrows(PlaceVisitNotFoundException.class, () -> 
        storage.removePlaceVisit(TOKYO.tripId(), TOKYO.placeId()));
  }

  /**
   * test that a PlaceNotFoundException is thrown when trying to remove a place
   * from storage when there is no place in storage with the given tripId and
   * placeId
   */
  @Test
  public void removePlaceVisit_placeNotInStorage_throwsException()
      throws PlaceVisitAlreadyExistsException, PlaceVisitNotFoundException {
    InMemoryPlaceVisitStorage storage = new InMemoryPlaceVisitStorage();
    storage.addPlaceVisit(TOKYO);
    storage.addPlaceVisit(TOKYO_B);
    Assert.assertThrows(PlaceVisitNotFoundException.class, () -> 
        storage.removePlaceVisit(SEOUL.tripId(), SEOUL.placeId()));
    
  }

  /**
   * test that no exception is thrown when removing a place from storage when 
   * there is a place in storage with the given tripId and placeId
   */
  @Test
  public void removePlace_placeInStorage_noException()
      throws PlaceVisitAlreadyExistsException, PlaceVisitNotFoundException {
    InMemoryPlaceVisitStorage storage = new InMemoryPlaceVisitStorage();
    storage.addPlaceVisit(ROME);
    storage.addPlaceVisit(BEIJING);
    storage.removePlaceVisit(ROME.tripId(), ROME.placeId());
  }

  /**
   * test that place is actually removed when removing a place from storage when 
   * there is a place in storage with the given tripId and placeId by checking
   * that calling getPlaceVisit on the removed PlaceVisit tripId and placeId
   * results in PlaceVisitNotFoundException being thrown
   */
  @Test
  public void getRemovedPlaceVisit_throwsExeption()
      throws PlaceVisitAlreadyExistsException, PlaceVisitNotFoundException {
    InMemoryPlaceVisitStorage storage = new InMemoryPlaceVisitStorage();
    storage.addPlaceVisit(ROME);
    storage.addPlaceVisit(BEIJING);
    storage.removePlaceVisit(ROME.tripId(), ROME.placeId());
    Assert.assertFalse(storage.getPlaceVisit(ROME.tripId(), ROME.placeId()).isPresent());
  }

  /**
   * test that changePlaceVisitStatus returns true and changes userMark field
   * when PlaceVisitModel object with the given tripId and placeId is in storage
   */
  @Test
  public void changePlaceVisitStatus_placeInStorage_returnsTrueAndStatusChanged()
      throws PlaceVisitAlreadyExistsException, PlaceVisitNotFoundException {
    InMemoryPlaceVisitStorage storage = new InMemoryPlaceVisitStorage();
    storage.addPlaceVisit(LONDON);
    storage.addPlaceVisit(PARIS);
    storage.addPlaceVisit(ROME);
    storage.addPlaceVisit(BEIJING);
    boolean response = storage.changePlaceVisitStatus(PARIS.tripId(), PARIS.placeId(), "don't-care");
    PlaceVisitModel changedParis = storage.getPlaceVisit(PARIS.tripId(), PARIS.placeId()).get();
    Assert.assertTrue(response);
    Assert.assertEquals(changedParis.userMark(), "don't-care");
  }

  /**
   * test that changePlaceVisitStatus returns false when PlaceVisitModel object
   * with the given tripId and placeId is not in storage
   */
  @Test
  public void changePlaceVisitStatus_placeNotInStorage_returnsFalse()
      throws PlaceVisitAlreadyExistsException, PlaceVisitNotFoundException {
    InMemoryPlaceVisitStorage storage = new InMemoryPlaceVisitStorage();
    storage.addPlaceVisit(LONDON);
    storage.addPlaceVisit(PARIS);
    storage.addPlaceVisit(ROME);
    storage.addPlaceVisit(BEIJING);
    boolean response = storage.changePlaceVisitStatus(SEOUL.tripId(), SEOUL.placeId(), "don't-care");
    Assert.assertFalse(response);
  }

  /**
   * test that changePlaceVisitStatus returns true and userMark field stays same
   * when changing status to same status as original PlaceVisitModel
   * when PlaceVisitModel object with the given tripId and placeId is in storage
   */
  @Test
  public void changePlaceVisitStatus_placeInStorage_returnsTrueAndStatusSame()
      throws PlaceVisitAlreadyExistsException, PlaceVisitNotFoundException {
    InMemoryPlaceVisitStorage storage = new InMemoryPlaceVisitStorage();
    storage.addPlaceVisit(LONDON);
    storage.addPlaceVisit(PARIS);
    storage.addPlaceVisit(ROME);
    storage.addPlaceVisit(BEIJING);
    boolean response = storage.changePlaceVisitStatus(PARIS.tripId(), PARIS.placeId(), "if-time");
    PlaceVisitModel changedParis = storage.getPlaceVisit(PARIS.tripId(), PARIS.placeId()).get();
    Assert.assertTrue(response);
    Assert.assertEquals(changedParis.userMark(), "if-time");
  }

  /**
   * test that only the PlaceVisitModel objects with the given tripId and 
   * userMark of "must-see" or "if-time" are returned in list when there is only
   * one unique tripId in storage
   */
  @Test
  public void listTripPlaceVIsits_oneTripInStorage_returnsOnlyTripsWithCorrectMark()
      throws PlaceVisitAlreadyExistsException {
    InMemoryPlaceVisitStorage storage = new InMemoryPlaceVisitStorage();
    storage.addPlaceVisit(LONDON);
    storage.addPlaceVisit(PARIS);
    storage.addPlaceVisit(ROME);
    storage.addPlaceVisit(TOKYO);
    List<PlaceVisitModel> tripAPlaces = storage.getTripPlaceVisits("a");
    assertThat(tripAPlaces).containsExactly(ROME, TOKYO, PARIS);
  }

  /**
   * test that only the PlaceVisitModel objects with the given tripId and 
   * userMark of "must-see" or "if-time" are returned in list when there are
   * multiple unique tripIds in storage
   */
  @Test
  public void listTripPlaceVisits_multipleTripsInStorage_returnsOnlyTripsWithCorrectTripIdAndMark()
      throws PlaceVisitAlreadyExistsException {
    InMemoryPlaceVisitStorage storage = new InMemoryPlaceVisitStorage();
    storage.addPlaceVisit(LONDON);
    storage.addPlaceVisit(PARIS);
    storage.addPlaceVisit(ROME);
    storage.addPlaceVisit(TOKYO);
    storage.addPlaceVisit(BEIJING);
    storage.addPlaceVisit(TOKYO_B);
    storage.addPlaceVisit(SEOUL);
    List<PlaceVisitModel> tripAPlaces = storage.getTripPlaceVisits("a");
    assertThat(tripAPlaces).containsExactly(ROME, TOKYO, PARIS);
  }

  /**
   * test that no PlaceVisitModel objects are returned in list when there are no
   * PlaceVisitModel objects in storage with the given tripId
   */
  @Test
  public void listTripPlaceVisits_tripNotInStorage_returnsEmptyList() throws PlaceVisitAlreadyExistsException {
    InMemoryPlaceVisitStorage storage = new InMemoryPlaceVisitStorage();
    storage.addPlaceVisit(LONDON);
    storage.addPlaceVisit(PARIS);
    storage.addPlaceVisit(ROME);
    storage.addPlaceVisit(TOKYO);
    storage.addPlaceVisit(BEIJING);
    storage.addPlaceVisit(TOKYO_B);
    storage.addPlaceVisit(SEOUL);
    List<PlaceVisitModel> tripAPlaces = storage.getTripPlaceVisits("c");
    assertThat(tripAPlaces).isEmpty();
  }
}
