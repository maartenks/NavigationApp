package com.example.navigationapplication.logic;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

public class ParkingLotsAPIManager {

    Context context;
    RequestQueue requestQueue;
    ParkingLotsAPIListener listener;

    final String url = "https://npropendata.rdw.nl/parkingdata/v2/";

    public ParkingLotsAPIManager(Context context, ParkingLotsAPIListener listener) {
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
        this.listener = listener;
    }

    public void getParkingLots() {
        JsonArrayRequest request = new JsonArrayRequest(

                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int idx = 0; idx < response.length(); idx++) {

                            }
                        } catch (Exception e) {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }

        );
    }
}
