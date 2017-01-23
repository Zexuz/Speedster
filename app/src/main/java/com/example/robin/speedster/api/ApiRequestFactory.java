package com.example.robin.speedster.api;


import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.robin.speedster.AuthService;
import com.example.robin.speedster.api.endpoints.EndpointsEnumHelper;
import com.example.robin.speedster.api.endpoints.IApiEndpoint;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

    public void sendApiEndpointRequest(RequestParameterObject requestInfoDataHelper) {
        JsonObjectRequest req = createRequest(requestInfoDataHelper);

        req.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 10,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        HttpQueueManager instance = HttpQueueManager.getInstance(_context);
        instance.getRequestQueue(_context).add(req);
    }

    private JsonObjectRequest createRequest(RequestParameterObject requestInfoDataHelper) {

        IApiEndpoint endpoint = EndpointsEnumHelper.getEndPointFromEnum(requestInfoDataHelper.getApiEnum());

        return new JsonObjectRequest(endpoint.getMethod(), getFullUrl(requestInfoDataHelper, endpoint), null, requestInfoDataHelper.getSuccesslister(), requestInfoDataHelper.getErrorListener()) {
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

    private String getFullUrl(RequestParameterObject requestInfoDataHelper, IApiEndpoint endpoint) {
        return BASE_URL + endpoint.getPath() + getQueryString(requestInfoDataHelper.getQueryParameters());
    }

    private String getQueryString(HashMap<String, String> params) {
        StringBuilder sb = new StringBuilder();
        for (HashMap.Entry<String, String> e : params.entrySet()) {
            if (sb.length() > 0) {
                sb.append('&');
            }
            try {
                sb.append(URLEncoder.encode(e.getKey(), "UTF-8")).append('=').append(URLEncoder.encode(e.getValue(), "UTF-8"));
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
        }

        return "?"+sb.toString();
    }
}


