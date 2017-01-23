package com.example.robin.speedster.api;

import com.android.volley.Response;
import com.example.robin.speedster.api.endpoints.ApiEndpoints;

import org.json.JSONObject;

import java.util.HashMap;

public class RequestParameterObject {
    private final ApiEndpoints _apiEnum;
    private final Response.Listener<JSONObject> _successLister;
    private final Response.ErrorListener _errorListener;
    private HashMap<String, String> _queryParameters;

    public RequestParameterObject(ApiEndpoints apiEnum, Response.Listener<JSONObject> successLister, Response.ErrorListener errorListener, HashMap<String, String> params) {
        this._apiEnum = apiEnum;
        this._successLister = successLister;
        this._errorListener = errorListener;
        this._queryParameters = params;
    }

    public ApiEndpoints getApiEnum() {
        return _apiEnum;
    }

    public Response.Listener<JSONObject> getSuccesslister() {
        return _successLister;
    }

    public Response.ErrorListener getErrorListener() {
        return _errorListener;
    }

    public HashMap<String, String> getQueryParameters() {
        return _queryParameters;
    }
}
