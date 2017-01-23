package com.example.robin.speedster.api.endpoints;


import com.android.volley.Request;

import java.util.Calendar;
import java.util.Locale;

public class DeparturesEndpoint implements IApiEndpoint {
    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public String getPath() {
//        Calendar c = Calendar.getInstance(Locale.forLanguageTag("sv"));
//        String hourAndMinute = c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE);
//        return "departureBoard?format=json&id=9021014003980000&date=2017-01-23&time="+hourAndMinute;
       return "departureBoard";
    }
}
