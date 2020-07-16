package com.google.tripmeout.serialization;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.tripmeout.frontend.TripModel;
import com.google.tripmeout.frontend.serialization.GsonTripModelTypeAdapter;
import com.google.tripmeout.serialization.testdata.TripTestDataAccessUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class GsonTripModelTypeAdapterTest {
  private Gson gson;

  private TripModel baseTrip = TripModel.builder().setUserId("idk").setId("a").build();

  @Before
  public void setup() {
    this.gson = new GsonBuilder()
                    .registerTypeHierarchyAdapter(TripModel.class, new GsonTripModelTypeAdapter())
                    .create();
  }

  @Test
  public void deserialize_wellFormed() throws Exception {
    TripModel trip =
        gson.fromJson(TripTestDataAccessUtil.getTripModelWellFormed(), TripModel.class);

    TripModel expected = baseTrip.toBuilder()
                             .setName("New Jersey")
                             .setLocationLat(12.66)
                             .setLocationLong(34.78)
                             .build();

    assertThat(trip).isEqualTo(expected);
  }

  @Test
  public void deserialize_unknownField_throwsJsonParseException() throws Exception {
    assertThrows(JsonParseException.class,
        ()
            -> gson.fromJson(

                TripTestDataAccessUtil.getTripModelUnknownField(), TripModel.class));
  }

  @Test
  public void deserialize_noId_throwsJsonParseException() throws Exception {
    assertThrows(JsonParseException.class,
        ()
            -> gson.fromJson(

                TripTestDataAccessUtil.getTripModelWithoutId(), TripModel.class));
  }

  @Test
  public void deserialize_noName_returnsTripModelWithNullName() throws Exception {
    TripModel trip =
        gson.fromJson(TripTestDataAccessUtil.getTripModelWithoutName(), TripModel.class);

    TripModel expected = baseTrip.toBuilder().setLocationLat(12.66).setLocationLong(34.78).build();

    assertThat(trip).isEqualTo(expected);
  }

  @Test
  public void deserialize_noUserId_throwsJsonParseException() throws Exception {
    assertThrows(JsonParseException.class,
        ()
            -> gson.fromJson(

                TripTestDataAccessUtil.getTripModelWithoutUserId(), TripModel.class));
  }

  @Test
  public void deserialize_noLatitude_throwsJsonParseException() throws Exception {
    TripModel trip =
        gson.fromJson(TripTestDataAccessUtil.getTripModelWithoutLatitude(), TripModel.class);

    TripModel expected = baseTrip.toBuilder().setName("New Jersey").setLocationLong(34.78).build();

    assertThat(trip).isEqualTo(expected);
  }

  @Test
  public void deserialize_noLongitude_throwsJsonParseException() throws Exception {
    TripModel trip =
        gson.fromJson(TripTestDataAccessUtil.getTripModelWithoutLongitude(), TripModel.class);

    TripModel expected = baseTrip.toBuilder().setName("New Jersey").setLocationLat(12.66).build();

    assertThat(trip).isEqualTo(expected);
  }

  @Test
  public void roundTrip_objectsAreEqual() throws Exception {
    TripModel trip = TripModel.builder()
                         .setId("id")
                         .setName("name")
                         .setUserId("userId")
                         .setLocationLat(23.9)
                         .setLocationLong(24.2)
                         .build();
    assertThat(gson.fromJson(gson.toJson(trip), TripModel.class)).isEqualTo(trip);
  }
}
