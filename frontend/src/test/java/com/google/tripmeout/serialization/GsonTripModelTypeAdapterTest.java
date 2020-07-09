package com.google.tripmeout.serialization;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.tripmeout.frontend.TripModel;
import com.google.tripmeout.frontend.serialization.GsonTripModelTypeAdapter;
import com.google.tripmeout.serialization.testdata.TestDataAccessUtil;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class GsonTripModelTypeAdapterTest {
    private Gson gsonWithTypeAdapter;

    @Before
    public void setup(){
        this.gsonWithTypeAdapter =
            new GsonBuilder().registerTypeAdapter(TripModel.class, new GsonTripModelTypeAdapter()).create();
    }

    @Test
    public void deserialize_wellFormed() throws Exception {
    TripModel trip =
        gsonWithTypeAdapter.fromJson(TestDataAccessUtil.getTripModelWellFormed(), TripModel.class);
        assertThat(trip.id()).isEqualTo("a");
        assertThat(trip.name()).isEqualTo("New Jersey");
        assertThat(trip.userId()).isEqualTo("idk");
        assertThat(trip.locationLat()).isEqualTo(12.66);
        assertThat(trip.locationLong()).isEqualTo(34.78);
    }
    
    @Test
    public void deserialize_unknownField_throwsJsonParseException() throws Exception {
    assertThrows(JsonParseException.class,
        ()
            -> gsonWithTypeAdapter.fromJson(
                TestDataAccessUtil.getTripModelUnknownField(), TripModel.class));
  }

   @Test
    public void deserialize_noId_throwsJsonParseException() throws Exception {
    assertThrows(JsonParseException.class,
        ()
            -> gsonWithTypeAdapter.fromJson(
                TestDataAccessUtil.getTripModelWithoutId(), TripModel.class));
  }

   @Test
    public void deserialize_noName_throwsJsonParseException() throws Exception {
    assertThrows(JsonParseException.class,
        ()
            -> gsonWithTypeAdapter.fromJson(
                TestDataAccessUtil.getTripModelWithoutName(), TripModel.class));
  }

     @Test
    public void deserialize_noUserId_throwsJsonParseException() throws Exception {
    assertThrows(JsonParseException.class,
        ()
            -> gsonWithTypeAdapter.fromJson(
                TestDataAccessUtil.getTripModelWithoutUserId(), TripModel.class));
  }

   @Test
    public void deserialize_noLatitude_throwsJsonParseException() throws Exception {
    
        
    assertThrows(JsonParseException.class,
        ()
            -> gsonWithTypeAdapter.fromJson(
                TestDataAccessUtil.getTripModelWithoutLatitude(), TripModel.class));
                
  }
   @Test
    public void deserialize_noLongitude_throwsJsonParseException() throws Exception {
    assertThrows(JsonParseException.class,
        ()
            -> gsonWithTypeAdapter.fromJson(
                TestDataAccessUtil.getTripModelWithoutLongitude(), TripModel.class));
  }
    
}