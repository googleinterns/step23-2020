package com.google.tripmeout.serialization;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.tripmeout.frontend.TripModel;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit.class)
public class GsonTripModelTypeAdapterTest {
    private Gson gsonWithTypeAdapter;

    @Before
    public void setup(){
        this.gsonWithTypeAdapter =
            new GsonBuilder().registerTypeAdapter(TripModel.class, new GsonTripModelTypeAdapter()).create();
    }

    
}