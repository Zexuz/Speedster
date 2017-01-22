package com.example.robin.speedster.api.endpoints;


public class ErrorEndpoint implements IApiEndpoint {
    @Override
    public int getMethod() {
        return -1;
    }

    @Override
    public String getPath() {
        return "Error, invalid path!";
    }
}
