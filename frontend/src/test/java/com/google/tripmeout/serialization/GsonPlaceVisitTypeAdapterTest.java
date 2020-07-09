package com.google.tripmeout.serialization;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.tripmeout.frontend.PlaceVisitModel;
import com.google.tripmeout.frontend.serialization.GsonPlaceVisitTypeAdapter;
import com.google.tripmeout.serialization.testdata.TestDataAccessUtil;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class GsonPlaceVisitTypeAdapterTest {
  private Gson gsonWithTypeAdapter;

  @Before
  public void setUp() {
    this.gsonWithTypeAdapter =
        new GsonBuilder()
            .registerTypeAdapter(PlaceVisitModel.class, new GsonPlaceVisitTypeAdapter())
            .create();
  }

  @Test
  public void deserialize_wellFormed() throws Exception {
    PlaceVisitModel place = gsonWithTypeAdapter.fromJson(
        TestDataAccessUtil.getWellFormedPlaceVisit(), PlaceVisitModel.class);
    assertThat(place.placeId()).isEqualTo("ABC");
    assertThat(place.name()).isEqualTo("New York");
    assertThat(place.tripId()).isEqualTo("123");
    assertThat(place.userMark()).isEqualTo("must-see");
    assertThat(place.latitude()).isEqualTo(50.2);
    assertThat(place.longitude()).isEqualTo(39.1);
  }

  @Test
  public void deserialize_unknownField_throwsJsonParseException() throws Exception {
    assertThrows(JsonParseException.class,
        ()
            -> gsonWithTypeAdapter.fromJson(
                TestDataAccessUtil.getPlaceVisitWithUnkownField(), PlaceVisitModel.class));
  }

  @Test
  public void deserialize_noPlaceId_throwsJsonParseException() throws Exception {
    assertThrows(JsonParseException.class,
        ()
            -> gsonWithTypeAdapter.fromJson(
                TestDataAccessUtil.getPlaceVisitWithoutPlaceId(), PlaceVisitModel.class));
  }
  @Test
  public void deserialize_noName_throwsJsonParseException() throws Exception {
    assertThrows(JsonParseException.class,
        ()
            -> gsonWithTypeAdapter.fromJson(
                TestDataAccessUtil.getPlaceVisitWithoutName(), PlaceVisitModel.class));
  }
  @Test
  public void deserialize_noTripId_throwsJsonParseException() throws Exception {
    assertThrows(JsonParseException.class,
        ()
            -> gsonWithTypeAdapter.fromJson(
                TestDataAccessUtil.getPlaceVisitWithoutTripId(), PlaceVisitModel.class));
  }
  @Test
  public void deserialize_noLatitude_throwsJsonParseException() throws Exception {
    assertThrows(JsonParseException.class,
        ()
            -> gsonWithTypeAdapter.fromJson(
                TestDataAccessUtil.getPlaceVisitWithoutLatitude(), PlaceVisitModel.class));
  }
  @Test
  public void deserialize_noLongitude_throwsJsonParseException() throws Exception {
    assertThrows(JsonParseException.class,
        ()
            -> gsonWithTypeAdapter.fromJson(
                TestDataAccessUtil.getPlaceVisitWithoutLongitude(), PlaceVisitModel.class));
  }
}
