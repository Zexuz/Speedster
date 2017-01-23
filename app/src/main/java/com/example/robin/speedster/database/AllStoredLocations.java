package com.example.robin.speedster.database;

import java.util.HashMap;


public class AllStoredLocations {

    public static String getIdForLocationByEnum(Locations location) {
       return getMap().get(location);
    }

    private static HashMap<Locations, String> getMap() {
        HashMap<Locations, String> map = new HashMap<>();

        map.put(Locations.KORSVÄGEN, "9021014003980000");
        map.put(Locations.CHALMERS, "9021014001960000");
        map.put(Locations.DOKTOR_SYDOWS_GATA, "9021014002100000");
        map.put(Locations.VARBERGGATAN, "9021014007270000");
        map.put(Locations.DOKTOR_WESTRINGS_GATA, "9021014002120000");

        return map;
    }

}


