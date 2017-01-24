package com.example.robin.speedster.path;

import com.example.robin.speedster.database.Locations;

import java.util.HashMap;
import java.util.Map;

public class Path {

    private Map<Integer,Locations> _stopAndTheirOrder;

    public Path() {
        _stopAndTheirOrder = new HashMap<>();
    }

    public Map<Integer, Locations> getStopAndTheirOrder() {
        return _stopAndTheirOrder;
    }

    public void addStop(Locations loc,Integer order) {
        _stopAndTheirOrder.put(order,loc);
    }

    public void addStop(Locations loc) {
        _stopAndTheirOrder.put(_stopAndTheirOrder.size(),loc);
    }

    public Locations getLocationBasedByIndex(int index) {
        return _stopAndTheirOrder.get(index);
    }
}
