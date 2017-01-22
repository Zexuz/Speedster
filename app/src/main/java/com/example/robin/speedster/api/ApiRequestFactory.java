package com.example.robin.speedster.api;


import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.robin.speedster.AuthService;
import com.example.robin.speedster.api.endpoints.ApiEndpoints;
import com.example.robin.speedster.api.endpoints.EndpointsEnumHelper;
import com.example.robin.speedster.api.endpoints.IApiEndpoint;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ApiRequestFactory {

    private static String BASE_URL = "https://api.vasttrafik.se/bin/rest.exe/v2/";
    private String _accessToken;
    private Context _context;

    public ApiRequestFactory(Context context) {
        _context = context;
        getNewAccessToken();
    }


    public void sendApiEndpointRequest(ApiEndpoints apiEnum, Response.Listener<JSONObject> successLister, Response.ErrorListener errorListener) {
        JsonObjectRequest req = createRequest(apiEnum, successLister, errorListener);

        req.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 10,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        HttpQueueManager instance = HttpQueueManager.getInstance(_context);
        instance.getRequestQueue(_context).add(req);
    }

    private JsonObjectRequest createRequest(ApiEndpoints apiEnum, Response.Listener<JSONObject> successLister, Response.ErrorListener errorListener) {

        IApiEndpoint endpoint = EndpointsEnumHelper.getEndPointFromEnum(apiEnum);
        return new JsonObjectRequest(endpoint.getMethod(), BASE_URL + endpoint.getPath(), null, successLister, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + _accessToken);
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Accept-Language", "sv-SE,sv;q=0.8,en-US;q=0.6,en;q=0.4");

                return params;
            }
        };
    }

    public void getNewAccessToken() {
        new AuthService().createAccessToken(_context, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    _accessToken = response.get("access_token").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
    }
}


