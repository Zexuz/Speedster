package com.example.robin.speedster.api.endpoints;

import com.android.volley.Request;

public class ArivalsEndpoint implements IApiEndpoint {
    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public String getPath() {
        return "arrivalBoard?format=json&id=9021014003980000&date=2017-01-22&time=22:15";
    }
}
