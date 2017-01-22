package com.example.robin.speedster;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.robin.speedster.api.ApiRequestFactory;
import com.example.robin.speedster.api.endpoints.ApiEndpoints;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {

    private TextToSpeech tts;
    ApiRequestFactory factory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTextToSpeech();
        factory = new ApiRequestFactory(getApplicationContext());
    }

    private void initTextToSpeech() {
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
            }
        });
    }

    public void greatPersonByVoice(View view) {
        String toSpeak = "The next Cable car leaves in 5 minutes";
        Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
        tts.setLanguage(Locale.ENGLISH);
        tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, UUID.randomUUID().toString());
    }

    public void fetchAccessToken(View view) {
        factory.sendApiEndpointRequest(ApiEndpoints.DEPATUREBOARD, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", response.toString());
                try {
                    if(response.getJSONObject("DepartureBoard") == null || response.getJSONObject("DepartureBoard").getJSONArray("Departure") == null)
                        return;
                    JSONArray departures = response.getJSONObject("DepartureBoard").getJSONArray("Departure");

                    Calendar currentTime = Calendar.getInstance();
                    for (int i = 0; i < departures.length(); i++) {
                        JSONObject departure = departures.getJSONObject(i);
                        String departyreTimeInHHMin = departure.get("time").toString();


                        int hour = Integer.parseInt(departyreTimeInHHMin.substring(0,2));
                        int min = Integer.parseInt(departyreTimeInHHMin.substring(3));

                        SimpleDateFormat sdf = new SimpleDateFormat("dd-HH:mm",Locale.getDefault());

                        Calendar depTime = new GregorianCalendar(
                                currentTime.get(Calendar.YEAR),
                                currentTime.get(Calendar.MONTH),
                                currentTime.get(Calendar.DAY_OF_MONTH),
                                hour,
                                min
                        );


                        long secDiff = (depTime.getTime().getTime() - currentTime.getTime().getTime()) / 1000;
                        Log.d("Dep times", secDiff + "");

                        String timeLeft = "";

                        if(secDiff < 0 ) continue;
                        if (secDiff < 60) {
                            timeLeft = " less than one minute";
                        } else {
                            timeLeft = (secDiff % 60) +" minutes and " + (secDiff - (secDiff % 60)) + " seconds" ;
                        }

                        String tosay = "The next one you can take leaves in "+timeLeft;
                        Log.d("Time left", timeLeft);
                        tts.speak(tosay,TextToSpeech.QUEUE_FLUSH,null,UUID.randomUUID().toString());
                        break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error response", "error, statscode:" + error.networkResponse.statusCode);
                if (error.networkResponse.statusCode == 401) {
                    factory.getNewAccessToken();
                }
            }
        });

    }

}
