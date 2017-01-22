package com.example.robin.speedster.api.endpoints;


import com.android.volley.Request;

public class AllStopsEndpoint implements IApiEndpoint{
    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public String getPath() {
        return "location.allstops?format=json";
    }
}
