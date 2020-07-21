package com.google.sps;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth8.assertThat;

import com.google.tripmeout.frontend.PlaceVisitModel;
import com.google.tripmeout.frontend.error.PlaceVisitAlreadyExistsException;
import com.google.tripmeout.frontend.error.PlaceVisitNotFoundException;
import com.google.tripmeout.frontend.error.TripNotFoundException;
import com.google.tripmeout.frontend.storage.InMemoryPlaceVisitStorage;
import java.util.List;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class InMemoryPlaceVisitStorageTest {
  // PlaceVisitModel objects to use in testing
  private static final PlaceVisitModel LONDON = PlaceVisitModel.builder()
                                                    .setPlacesApiPlaceId("LCY")
                                                    .setPlaceName("London, UK")
                                                    .setTripId("a")
                                                    .setUserMark(PlaceVisitModel.UserMark.NO)
                                                    .setLatitude(56.0)
                                                    .setLongitude(23.64)
                                                    .build();

  private static final PlaceVisitModel ROME = PlaceVisitModel.builder()
                                                  .setPlacesApiPlaceId("FCO")
                                                  .setPlaceName("Rome, Italy")
                                                  .setTripId("a")
                                                  .setUserMark(PlaceVisitModel.UserMark.YES)
                                                  .setLatitude(44.32)
                                                  .setLongitude(32.1244)
                                                  .build();

  private static final PlaceVisitModel PARIS = PlaceVisitModel.builder()
                                                   .setPlacesApiPlaceId("CDG")
                                                   .setPlaceName("Paris, France")
                                                   .setTripId("a")
                                                   .setUserMark(PlaceVisitModel.UserMark.MAYBE)
                                                   .setLatitude(48.3288)
                                                   .setLongitude(34.0)
                                                   .build();

  private static final PlaceVisitModel TOKYO = PlaceVisitModel.builder()
                                                   .setPlacesApiPlaceId("HND")
                                                   .setPlaceName("Tokyo, Japan")
                                                   .setTripId("a")
                                                   .setUserMark(PlaceVisitModel.UserMark.YES)
                                                   .setLatitude(35.32)
                                                   .setLongitude(139.33)
                                                   .build();

  private static final PlaceVisitModel BEIJING = PlaceVisitModel.builder()
                                                     .setPlacesApiPlaceId("PEK")
                                                     .setPlaceName("Beijing, China")
                                                     .setTripId("b")
                                                     .setUserMark(PlaceVisitModel.UserMark.YES)
                                                     .setLatitude(35.32)
                                                     .setLongitude(125.33)
                                                     .build();

  private static final PlaceVisitModel SEOUL = PlaceVisitModel.builder()
                                                   .setPlacesApiPlaceId("ICN")
                                                   .setPlaceName("Seoul, South Korea")
                                                   .setTripId("b")
                                                   .setUserMark(PlaceVisitModel.UserMark.MAYBE)
                                                   .setLatitude(26.32)
                                                   .setLongitude(135.33)
                                                   .build();

  private static final PlaceVisitModel TOKYO_B = PlaceVisitModel.builder()
                                                     .setPlacesApiPlaceId("HND")
                                                     .setPlaceName("Tokyo, Japan")
                                                     .setTripId("b")
                                                     .setUserMark(PlaceVisitModel.UserMark.YES)
                                                     .setLatitude(35.32)
                                                     .setLongitude(139.33)
                                                     .build();

  private static final PlaceVisitModel SEOUL_B = PlaceVisitModel.builder()
                                                     .setPlacesApiPlaceId("ICN")
                                                     .setPlaceName("some city in Korea")
                                                     .setTripId("b")
                                                     .setUserMark(PlaceVisitModel.UserMark.MAYBE)
                                                     .setLatitude(0.0)
                                                     .setLongitude(0.0)
                                                     .build();

  /**
   * test that a PlaceVisitAlreadyExistsException is thrown when a place is
   * added but another place with the same PlacesApiPlaceId and tripId has already 
   * been added to the storage
   */
  @Test
  public void addPlaceVisit_samePlaceIdAndTripIdAlreadyAdded_throwsException()
      throws PlaceVisitAlreadyExistsException {
    InMemoryPlaceVisitStorage storage = new InMemoryPlaceVisitStorage();
    storage.addPlaceVisit(SEOUL);
    Assert.assertThrows(
        PlaceVisitAlreadyExistsException.class, () -> storage.addPlaceVisit(SEOUL_B));
  }

  /**
   * test that a PlaceNotFoundException is thrown when getting a place from
   * an empty storage
   */
  @Test
  public void getPlaceVisit_emptyStorage_returnsEmptyOptional() {
    InMemoryPlaceVisitStorage storage = new InMemoryPlaceVisitStorage();
    assertThat(storage.getPlaceVisit(TOKYO.tripId(), TOKYO.placesApiPlaceId())).isEmpty();
  }

  /**
   * test that a PlaceNotFoundException is thrown when trying to get a place
   * from storage when there is no place in storage with the given tripId and
   * PlacesApiPlaceId
   */
  @Test
  public void getPlaceVisit_placeNotInStorage_returnsEmptyOptional()
      throws PlaceVisitAlreadyExistsException, PlaceVisitNotFoundException {
    InMemoryPlaceVisitStorage storage = new InMemoryPlaceVisitStorage();
    storage.addPlaceVisit(TOKYO);
    storage.addPlaceVisit(TOKYO_B);
    assertThat(storage.getPlaceVisit(SEOUL.tripId(), SEOUL.placesApiPlaceId())).isEmpty();
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

    Optional<PlaceVisitModel> tokyo = storage.getPlaceVisit(TOKYO.tripId(), TOKYO.placesApiPlaceId());
    assertThat(tokyo).hasValue(TOKYO);

    Optional<PlaceVisitModel> tokyoB = storage.getPlaceVisit(TOKYO_B.tripId(), TOKYO_B.placesApiPlaceId());
    assertThat(tokyoB).hasValue(TOKYO_B);
  }

  /**
   * test that a PlaceNotFoundException is thrown when removing a place from
   * an empty storage
   */
  @Test
  public void removePlaceVisit_emptyStorage_throwsException() throws PlaceVisitNotFoundException {
    InMemoryPlaceVisitStorage storage = new InMemoryPlaceVisitStorage();
    Assert.assertThrows(PlaceVisitNotFoundException.class,
        () -> storage.removePlaceVisit(TOKYO.tripId(), TOKYO.placesApiPlaceId()));
  }

  /**
   * test that a PlaceNotFoundException is thrown when trying to remove a place
   * from storage when there is no place in storage with the given tripId and
   * placesApiPlaceId
   */
  @Test
  public void removePlaceVisit_placeNotInStorage_throwsException()
      throws PlaceVisitAlreadyExistsException, PlaceVisitNotFoundException {
    InMemoryPlaceVisitStorage storage = new InMemoryPlaceVisitStorage();
    storage.addPlaceVisit(TOKYO);
    storage.addPlaceVisit(TOKYO_B);
    Assert.assertThrows(PlaceVisitNotFoundException.class,
        () -> storage.removePlaceVisit(SEOUL.tripId(), SEOUL.placesApiPlaceId()));
  }

  /**
   * test that no exception is thrown when removing a place from storage when
   * there is a place in storage with the given tripId and placesApiPlaceId
   */
  @Test
  public void removePlace_placeInStorage_noException()
      throws PlaceVisitAlreadyExistsException, PlaceVisitNotFoundException {
    InMemoryPlaceVisitStorage storage = new InMemoryPlaceVisitStorage();
    storage.addPlaceVisit(ROME);
    storage.addPlaceVisit(BEIJING);
    storage.removePlaceVisit(ROME.tripId(), ROME.placesApiPlaceId());
  }

  /**
   * test that place is actually removed when removing a place from storage when
   * there is a place in storage with the given tripId and placesApiPlaceId by checking
   * that calling getPlaceVisit on the removed PlaceVisit tripId and placesApiPlaceId
   * results in PlaceVisitNotFoundException being thrown
   */
  @Test
  public void getRemovedPlaceVisit_throwsExeption()
      throws PlaceVisitAlreadyExistsException, PlaceVisitNotFoundException {
    InMemoryPlaceVisitStorage storage = new InMemoryPlaceVisitStorage();
    storage.addPlaceVisit(ROME);
    storage.addPlaceVisit(BEIJING);
    storage.removePlaceVisit(ROME.tripId(), ROME.placesApiPlaceId());
    assertThat(storage.getPlaceVisit(ROME.tripId(), ROME.placesApiPlaceId())).isEmpty();
  }

  /**
   * test that changePlaceVisitStatus returns true and changes userMark field
   * when PlaceVisitModel object with the given tripId and placesApiPlaceId is in storage
   */
  @Test
  public void updateUserMarkOrAddPlaceVisit_placeInStorage_returnsTrueAndStatusChanged()
      throws PlaceVisitAlreadyExistsException, PlaceVisitNotFoundException {
    InMemoryPlaceVisitStorage storage = new InMemoryPlaceVisitStorage();
    storage.addPlaceVisit(LONDON);
    storage.addPlaceVisit(PARIS);
    storage.addPlaceVisit(ROME);
    storage.addPlaceVisit(BEIJING);

    boolean response = storage.updateUserMarkOrAddPlaceVisit(PARIS, PlaceVisitModel.UserMark.NO);
    PlaceVisitModel changedParis = storage.getPlaceVisit(PARIS.tripId(), PARIS.placesApiPlaceId()).get();
    assertThat(response).isTrue();
    assertThat(changedParis.userMark()).isEqualTo(PlaceVisitModel.UserMark.NO);
  }

  /**
   * test that changePlaceVisitStatus returns false when PlaceVisitModel object
   * with the given tripId and placesApiPlaceId is not in storage
   */
  @Test
  public void updateUserMarkOrAddPlaceVisit_placeNotInStorage_returnsFalseAndPlaceAdded()
      throws PlaceVisitAlreadyExistsException, PlaceVisitNotFoundException {
    InMemoryPlaceVisitStorage storage = new InMemoryPlaceVisitStorage();
    storage.addPlaceVisit(LONDON);
    storage.addPlaceVisit(PARIS);
    storage.addPlaceVisit(ROME);
    storage.addPlaceVisit(BEIJING);
    boolean response = storage.updateUserMarkOrAddPlaceVisit(SEOUL, PlaceVisitModel.UserMark.NO);
    PlaceVisitModel updatedSeoul =
        SEOUL.toBuilder().setUserMark(PlaceVisitModel.UserMark.NO).build();
    assertThat(response).isFalse();
    assertThat(storage.getPlaceVisit(SEOUL.tripId(), SEOUL.placesApiPlaceId())).hasValue(updatedSeoul);
  }

  /**
   * test that changePlaceVisitStatus returns false when PlaceVisitModel object
   * and no PlaceVistModel with same tripId in storage
   */
  @Test
  public void updateUserMarkOrAddPlaceVisit_placeAndTripNotInStorage_returnsFalseAndPlaceAdded()
      throws PlaceVisitAlreadyExistsException, PlaceVisitNotFoundException {
    InMemoryPlaceVisitStorage storage = new InMemoryPlaceVisitStorage();
    boolean response = storage.updateUserMarkOrAddPlaceVisit(SEOUL, PlaceVisitModel.UserMark.NO);
    PlaceVisitModel updatedSeoul =
        SEOUL.toBuilder().setUserMark(PlaceVisitModel.UserMark.NO).build();
    assertThat(response).isFalse();
    assertThat(storage.getPlaceVisit(SEOUL.tripId(), SEOUL.placesApiPlaceId())).hasValue(updatedSeoul);
  }

  /**
   * test that changePlaceVisitStatus returns true and userMark field stays same
   * when changing status to same status as original PlaceVisitModel
   * when PlaceVisitModel object with the given tripId and placesApiPlaceId is in storage
   */
  @Test
  public void updateUserMarkOrAddPlaceVisit_placeInStorage_returnsTrueAndStatusSame()
      throws PlaceVisitAlreadyExistsException, PlaceVisitNotFoundException {
    InMemoryPlaceVisitStorage storage = new InMemoryPlaceVisitStorage();
    storage.addPlaceVisit(LONDON);
    storage.addPlaceVisit(PARIS);
    storage.addPlaceVisit(ROME);
    storage.addPlaceVisit(BEIJING);

    boolean response = storage.updateUserMarkOrAddPlaceVisit(PARIS, PlaceVisitModel.UserMark.MAYBE);
    PlaceVisitModel changedParis = storage.getPlaceVisit(PARIS.tripId(), PARIS.placesApiPlaceId()).get();
    assertThat(response).isTrue();
    assertThat(changedParis.userMark()).isEqualTo(PlaceVisitModel.UserMark.MAYBE);
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
    assertThat(tripAPlaces).containsExactly(ROME, TOKYO, PARIS, LONDON);
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
    assertThat(tripAPlaces).containsExactly(ROME, TOKYO, PARIS, LONDON);
  }

  /**
   * test that no PlaceVisitModel objects are returned in list when there are no
   * PlaceVisitModel objects in storage with the given tripId
   */
  @Test
  public void listTripPlaceVisits_tripNotInStorage_returnsEmptyList()
      throws PlaceVisitAlreadyExistsException {
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

  /**
   * test that no PlaceVisitModel objects are returned in list after removing
   * PlaceVisitModel objects with the given tripId
   */
  @Test
  public void removeTripPlaceVisits_getTripPlaceVisits_tripInStorage_returnsEmptyList()
      throws PlaceVisitAlreadyExistsException, TripNotFoundException {
    InMemoryPlaceVisitStorage storage = new InMemoryPlaceVisitStorage();
    storage.addPlaceVisit(LONDON);
    storage.addPlaceVisit(PARIS);
    storage.addPlaceVisit(ROME);
    storage.addPlaceVisit(TOKYO);
    storage.removeTripPlaceVisits("a");
    List<PlaceVisitModel> tripAPlaces = storage.getTripPlaceVisits("a");
    assertThat(tripAPlaces).isEmpty();
  }

  /**
   * test that after deleting one trip's PlaceVisits, other trip's PlaceVisits
   * remain
   */
  @Test
  public void removeTripPlaceVisits_getOtherTripPlaceVisits_tripInStorage_returnsTripList()
      throws PlaceVisitAlreadyExistsException, TripNotFoundException {
    InMemoryPlaceVisitStorage storage = new InMemoryPlaceVisitStorage();
    storage.addPlaceVisit(LONDON);
    storage.addPlaceVisit(PARIS);
    storage.addPlaceVisit(ROME);
    storage.addPlaceVisit(TOKYO);
    storage.addPlaceVisit(BEIJING);
    storage.addPlaceVisit(TOKYO_B);
    storage.addPlaceVisit(SEOUL);
    storage.removeTripPlaceVisits("b");
    List<PlaceVisitModel> tripAPlaces = storage.getTripPlaceVisits("a");
    assertThat(tripAPlaces).containsExactly(ROME, TOKYO, PARIS, LONDON);
  }

  @Test
  public void removeTripPlaceVisits_tripNotInStorage_throwsException()
      throws PlaceVisitAlreadyExistsException, TripNotFoundException {
    InMemoryPlaceVisitStorage storage = new InMemoryPlaceVisitStorage();
    storage.addPlaceVisit(LONDON);
    storage.addPlaceVisit(PARIS);
    storage.addPlaceVisit(ROME);
    storage.addPlaceVisit(TOKYO);
    storage.addPlaceVisit(BEIJING);
    storage.addPlaceVisit(TOKYO_B);
    storage.addPlaceVisit(SEOUL);
    Assert.assertThrows(TripNotFoundException.class, () -> storage.removeTripPlaceVisits("c"));
  }
}
