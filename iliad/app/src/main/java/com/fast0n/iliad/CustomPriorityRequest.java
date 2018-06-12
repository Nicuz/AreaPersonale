package com.fast0n.iliad;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class CustomPriorityRequest extends JsonObjectRequest {

    Priority priority = Priority.HIGH;

    public CustomPriorityRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    public CustomPriorityRequest(String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
    }

    @Override
    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority p){
        priority = p;
    }
}