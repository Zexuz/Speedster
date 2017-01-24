package com.example.robin.speedster.path;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.robin.speedster.api.ApiRequestFactory;
import com.example.robin.speedster.api.HttpQueueManager;
import com.example.robin.speedster.api.RequestParameterObject;
import com.example.robin.speedster.api.endpoints.ApiEndpoints;
import com.example.robin.speedster.database.AllStoredLocations;
import com.example.robin.speedster.database.Locations;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.EventListener;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class PathCalulator  {

    private List<Path> _paths;
    private ApiRequestFactory _apiRequestFactory;
    private Context _context;

    public PathCalulator(Context context) {
        _apiRequestFactory = new ApiRequestFactory(context);
        _paths = new ArrayList<>();
        _context = context;

        Path cableCarPath = new Path();
        cableCarPath.addStop(Locations.DOKTOR_SYDOWS_GATA);
        cableCarPath.addStop(Locations.CHALMERS);
        cableCarPath.addStop(Locations.KORSVÄGEN);

        Path bussPath = new Path();
        bussPath.addStop(Locations.DOKTOR_WESTRINGS_GATA);
        bussPath.addStop(Locations.KORSVÄGEN);

        _paths.add(cableCarPath);
        _paths.add(bussPath);
    }

    public void CalculateFastestPath() {

        for (int i = 0; i < _paths.size(); i++) {
            Path path = _paths.get(i);

            Locations loc = path.getLocationBasedByIndex(i);
            HashMap<String, String> queryString = new HashMap<>();
            queryString.put("id", AllStoredLocations.getIdForLocationByEnum(loc));
            queryString.put("format","json");

            RequestParameterObject parameterObject = new RequestParameterObject(ApiEndpoints.DEPATUREBOARD, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }, queryString);


            _apiRequestFactory.sendApiEndpointRequest(parameterObject);
            HttpQueueManager.getInstance(_context).getRequestQueue(_context).addRequestFinishedListener();
        }
    }

    public void doApiRequest(View view) {
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error response", "error, statscode:" + error.networkResponse.statusCode);
                if (error.networkResponse.statusCode == 401) {
                    factory.getNewAccessToken();
                }
            }
        };

        Response.Listener<JSONObject> successLister = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", response.toString());
                try {
                    handelDepartureJSONResponse(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        ApiEndpoints apiEndpoint = ApiEndpoints.DEPATUREBOARD;

        HashMap<String,String> queryString = new HashMap<>();
        queryString.put("id", AllStoredLocations.getIdForLocationByEnum(Locations.DOKTOR_WESTRINGS_GATA));
        queryString.put("format","json");

        RequestParameterObject parameterObject = new RequestParameterObject(apiEndpoint, successLister, errorListener, queryString);
        factory.sendApiEndpointRequest(parameterObject);
    }

    private void handelDepartureJSONResponse(JSONObject response) throws JSONException {
        if (isValidResponse(response)) return;

        JSONArray departures = response.getJSONObject("DepartureBoard").getJSONArray("Departure");
        loopOverResponse(departures);
    }

    private void loopOverResponse(JSONArray departures) throws JSONException {
        Calendar currentTime = Calendar.getInstance();
        for (int i = 0; i < departures.length(); i++) {
            JSONObject departure = departures.getJSONObject(i);
            String departyreTimeInHHMin = departure.get("time").toString();

            Log.d("time response", departure.get("time").toString());

            Calendar depTime = getCalendar(currentTime, departyreTimeInHHMin);

            long secondsUntilDeparture = getSecUntilDeparture(currentTime, depTime);

            if (alreadyDeparted(secondsUntilDeparture)) continue;
            Log.d("Line", departure.get());


            sayInfoToUser(secondsUntilDeparture);
            break;
        }
    }

    private void sayInfoToUser(long secondsUntilDeparture) {
        String timeLeft = getTextToSayAboutDeparture(secondsUntilDeparture);
        String tosay = "The next one you can take leaves in " + timeLeft;
        Log.d("Time left", timeLeft);
//        tts.setLanguage(Locale.ENGLISH);
//        int res = tts.speak(tosay, TextToSpeech.QUEUE_FLUSH, null, UUID.randomUUID().toString());
//        if (res == TextToSpeech.SUCCESS)
//            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT);
//        else
//            Toast.makeText(getApplicationContext(), "Fail!", Toast.LENGTH_SHORT).show();

    }

    private boolean isValidResponse(JSONObject response) throws JSONException {
        return response.getJSONObject("DepartureBoard") == null ||
                response.getJSONObject("DepartureBoard").getJSONArray("Departure") == null;
    }

    private boolean alreadyDeparted(long secondsUntilDeparture) {
        return secondsUntilDeparture < 0;
    }

    @Nullable
    private String getTextToSayAboutDeparture(long secondsUntilDeparture) {
        String timeLeft;

        if (secondsUntilDeparture < 60) {
            timeLeft = " less than one minute";
        } else {
            timeLeft = (secondsUntilDeparture / 60) + " minutes and " + (secondsUntilDeparture % 60) + " seconds";
        }
        return timeLeft;
    }

    private long getSecUntilDeparture(Calendar currentTime, Calendar depTime) {
        return (depTime.getTime().getTime() - currentTime.getTime().getTime()) / 1000;
    }

    @NonNull
    private Calendar getCalendar(Calendar currentTime, String departyreTimeInHHMin) {
        int hour = Integer.parseInt(departyreTimeInHHMin.substring(0, 2));
        int min = Integer.parseInt(departyreTimeInHHMin.substring(3));
        return new GregorianCalendar(
                currentTime.get(Calendar.YEAR),
                currentTime.get(Calendar.MONTH),
                currentTime.get(Calendar.DAY_OF_MONTH),
                hour,
                min
        );
    }


}

