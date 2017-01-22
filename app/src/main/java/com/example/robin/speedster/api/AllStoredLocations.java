package com.example.robin.speedster.api;

import java.util.HashMap;


public class AllStoredLocations {

    public HashMap<Locations, String> getMap() {
        HashMap<Locations, String> map = new HashMap<>();

        map.put(Locations.KORSVÄGEN, "9021014003980000");
        map.put(Locations.CHALMERS, "9021014001960000");
        map.put(Locations.DOKTOR_SYDOWS_GATA, "9021014002100000");
        map.put(Locations.VARBERGGATAN, "9021014007270000");

        return map;
    }

}


enum Locations {
    KORSVÄGEN,
    CHALMERS,
    DOKTOR_SYDOWS_GATA,
    VARBERGGATAN

}
