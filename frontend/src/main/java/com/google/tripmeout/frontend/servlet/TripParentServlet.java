package com.google.tripmeout.frontend.servlet;

import com.google.tripmeout.frontend.TripModel;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import javax.inject.Inject;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TripParentServlet extends HttpServlet{

    private static final String TRIP_PARENT_PARAM_KEY="tripName";
    private static final String LONG_PARAM_KEY="long";
    private static final String LAT_PARAM_KEY="lat";
    private static final String USER_ID_PARAM_KEY="userId";
    private final Gson gson;
    
    @Inject
    public TripParentServlet(Gson gson){
        this.gson = gson;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
   // response.setContentType("application/json") ; 
    //response.getWriter().print(gson.toJson());
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {  
        String tripName = request.getParameter(TRIP_PARENT_PARAM_KEY);
        String longitude = request.getParameter(LONG_PARAM_KEY);
        String latitude = request.getParameter(LAT_PARAM_KEY);
        String userID = request.getParameter(USER_ID_PARAM_KEY);
       
        TripModel trip = TripModel.builder()
        .setId(UUID.randomUUID().toString())
        .setName(tripName)
        .setUserId(userID)
        .setLocationLat(Double.parseDouble(latitude))
        .setLocationLong(Double.parseDouble(longitude))
        .build();

        response.getWriter().print(gson.toJson(trip));




    }

}