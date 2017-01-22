package com.example.robin.speedster.api.endpoints;


public class EndpointsEnumHelper {

    public static String getStringFromEnum(ApiEndpoints enu) {
        return getEndPointFromEnum(enu).getPath();
    }

    public static IApiEndpoint getEndPointFromEnum(ApiEndpoints enu) {
        switch (enu) {
            case ALLSTOPS:
                return new AllStopsEndpoint();
            case SYSTEMINFO:
                return new SystemInfoEndpoint();
            case SEARCHFORSTOP:
                return new SearchForStopsByNameEndpoint();
            case DEPATUREBOARD:
                return new DepaturesEndpoint();
            case ARIVALBORD:
                return new ArivalsEndpoint(); //TODO
            default:
                return new SystemInfoEndpoint();
        }
    }
}
