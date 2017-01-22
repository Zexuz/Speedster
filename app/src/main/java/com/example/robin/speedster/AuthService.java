package com.example.robin.speedster;


import android.content.Context;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class AuthService {
    private static String TOKEN_URL = "https://api.vasttrafik.se/token?grant_type=client_credentials";

    public void createAccessToken(Context context, Response.Listener<JSONObject> success, Response.ErrorListener error) {
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest stringRequest = new JsonObjectRequest
                (JsonObjectRequest.Method.POST, TOKEN_URL, null, success, error) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Basic " + getAuthToken());
                params.put("Content-Type", "application/x-www-form-urlencoded");

                return params;
            }
        };
        queue.add(stringRequest);
    }

    private String getAuthToken() {
        String key = "dkKF1zpkmpItPSzo4rQzN0Nc7fQa";
        String secret = "otC53pAIxflNAkUr7RhAeMgFPMca";
        return Base64.encodeToString((key + ":" + secret).getBytes(), Base64.DEFAULT);
    }

}
