package com.example.robin.speedster.api;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class HttpQueueManager {

    private static HttpQueueManager _ourInstance;

    private RequestQueue _requestQueue;


    public static HttpQueueManager getInstance(Context context) {
        if (_ourInstance == null) _ourInstance = new HttpQueueManager(context);
        return _ourInstance;
    }

    private HttpQueueManager(Context context) {
        _requestQueue = getRequestQueue(context);
    }

    public RequestQueue getRequestQueue(Context context) {
        if (_requestQueue == null)
            _requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        return _requestQueue;
    }


}
