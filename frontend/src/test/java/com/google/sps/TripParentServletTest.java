package com.google.sps;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import com.google.tripmeout.frontend.storage.TripStorage;
import com.google.tripmeout.frontend.TripModel;
import com.google.tripmeout.frontend.servlet.TripParentServlet;
import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

@RunWith(JUnit4.class)
public class TripParentServletTest{
    @Mock TripStorage storage;
    @Mock HttpServletRequest request;
    @Mock HttpServletResponse response;

    Gson gson;
    TripParentServlet tripParentServlet;


    @Before
    public void setup(){
        initMocks(this);
        gson =
            new GsonBuilder().registerTypeAdapter(TripModel.class, new JsonSerlializer<TripModel>()).create();
        tripParentServlet = new TripParentServlet(storage, gson);
    }


    @Test
    public void doGet_noTrips() throws Exception{
        when(storage.getAllUserTrips("user1")).thenReturn(ImmutableList.of());
        StringWriter responseCapture = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseCapture));

        tripParentServlet.doGet(request, response);

        verify(response).setContentType("application/json;");
         assertThat(responseCapture.toString()).isEqualTo("[]");
    }

}
