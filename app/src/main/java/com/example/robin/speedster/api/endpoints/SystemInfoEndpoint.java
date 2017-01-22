package com.example.robin.speedster.api.endpoints;


import com.android.volley.Request;

public class SystemInfoEndpoint implements IApiEndpoint {

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public String getPath() {
        return "systeminfo?format=json";
    }
}
