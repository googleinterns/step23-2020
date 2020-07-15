package com.google.tripmeout.serialization;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.tripmeout.frontend.PlaceVisitModel;
import com.google.tripmeout.frontend.serialization.GsonPlaceVisitTypeAdapter;
import com.google.tripmeout.serialization.testdata.TestDataAccessUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class GsonPlaceVisitTypeAdapterTest {
  private Gson gson;

  @Before
  public void setUp() {
    this.gson =
        new GsonBuilder()
            .registerTypeHierarchyAdapter(PlaceVisitModel.class, new GsonPlaceVisitTypeAdapter())
            .create();
  }

  @Test
  public void deserialize_wellFormed() throws Exception {
    PlaceVisitModel place =
        gson.fromJson(TestDataAccessUtil.getWellFormedPlaceVisit(), PlaceVisitModel.class);
    assertThat(place.placeId()).isEqualTo("ABC");
    assertThat(place.name()).isEqualTo("New York");
    assertThat(place.tripId()).isEqualTo("123");
    assertThat(place.userMark().toString()).isEqualTo("YES");
    assertThat(place.latitude()).isEqualTo(50.2);
    assertThat(place.longitude()).isEqualTo(39.1);
  }

  @Test
  public void deserialize_unknownField_throwsJsonParseException() throws Exception {
    assertThrows(JsonParseException.class,
        ()
            -> gson.fromJson(
                TestDataAccessUtil.getPlaceVisitWithUnkownField(), PlaceVisitModel.class));
  }

  @Test
  public void deserialize_noPlaceId_throwsJsonParseException() throws Exception {
    assertThrows(JsonParseException.class,
        ()
            -> gson.fromJson(
                TestDataAccessUtil.getPlaceVisitWithoutPlaceId(), PlaceVisitModel.class));
  }
  @Test
  public void deserialize_noName_throwsJsonParseException() throws Exception {
    assertThrows(JsonParseException.class,
        () -> gson.fromJson(TestDataAccessUtil.getPlaceVisitWithoutName(), PlaceVisitModel.class));
  }
  @Test
  public void deserialize_noTripId_throwsJsonParseException() throws Exception {
    assertThrows(JsonParseException.class,
        ()
            -> gson.fromJson(
                TestDataAccessUtil.getPlaceVisitWithoutTripId(), PlaceVisitModel.class));
  }
  @Test
  public void deserialize_noLatitude_throwsJsonParseException() throws Exception {
    assertThrows(JsonParseException.class,
        ()
            -> gson.fromJson(
                TestDataAccessUtil.getPlaceVisitWithoutLatitude(), PlaceVisitModel.class));
  }
  @Test
  public void deserialize_noLongitude_throwsJsonParseException() throws Exception {
    assertThrows(JsonParseException.class,
        ()
            -> gson.fromJson(
                TestDataAccessUtil.getPlaceVisitWithoutLongitude(), PlaceVisitModel.class));
  }
  @Test
  public void roundTrip_objectsAreEqual() throws Exception {
    PlaceVisitModel place = PlaceVisitModel.builder()
                                .setPlaceId("abc")
                                .setName("name")
                                .setTripId("tripId")
                                .setLongitude(12.3)
                                .setLatitude(34.9)
                                .build();
    assertThat(gson.fromJson(gson.toJson(place), PlaceVisitModel.class)).isEqualTo(place);
  }
}
