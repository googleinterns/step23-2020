package com.google.tripmeout.frontend.servlet;

import com.google.tripmeout.frontend.storage.TripStorage;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import javax.inject.Inject;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TripParentServlet extends HttpServlet{

    private final Gson gson;
    
    @Inject
    public TripParentServlet(Gson gson){
        this.gson = gson;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    response.setContentType("application/json") ; 
    response.getWriter().print(gson.toJson());
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        
    }

}