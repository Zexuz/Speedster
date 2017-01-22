package com.example.robin.speedster.api.endpoints;


import com.android.volley.Request;

import java.util.Calendar;

public class DepaturesEndpoint implements IApiEndpoint {
    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public String getPath() {
        Calendar c = Calendar.getInstance();
        String hourAndMinute = c.get(Calendar.HOUR)+":"+c.get(Calendar.MINUTE);
        return "departureBoard?format=json&id=9021014003980000&date=2017-01-22&time="+hourAndMinute;
    }
}
