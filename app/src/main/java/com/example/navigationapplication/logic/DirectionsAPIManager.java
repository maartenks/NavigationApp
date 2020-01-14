package com.example.navigationapplication.logic;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.navigationapplication.R;
import com.example.navigationapplication.data.Waypoint;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DirectionsAPIManager implements Response.Listener, Response.ErrorListener
{
    private Context context;
    private RequestQueue requestQueue;

    private ArrayList<LatLng> locations;

    private DirectionsAPIListener directionsAPIListener;

    public DirectionsAPIManager(Context context, DirectionsAPIListener directionsAPIListener)
    {
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);

        this.locations = new ArrayList<LatLng>();
        this.directionsAPIListener = directionsAPIListener;
    }

    public void requestRoute(Location currentLocation, Waypoint waypoint)
    {
        this.locations.clear();
        sendRequest(Request.Method.GET, getUrlForWaypoints(currentLocation, waypoint));
    }

    private void sendRequest(int method, String url)
    {
        JsonRequest jsonRequest = new JsonObjectRequest(method, url, null, this, this);
        //jsonRequest.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        this.requestQueue.add(jsonRequest);
    }

    private String getUrlForWaypoints(Location myLocation, Waypoint waypoint)
    {
        String url = "http://145.48.6.80:3000/directions?"
                    + "origin=" + myLocation.getLatitude() + "," + myLocation.getLongitude()
                    + "&destination=" + waypoint.getLocation().latitude + "," + waypoint.getLocation().longitude
                    + "&mode=walking"
                    + "&key=" + this.context.getResources().getString(R.string.google_directions_key);

        return url;
    }

    @Override
    public void onResponse(Object response)
    {
        JSONObject jsonObject = (JSONObject)response;
        Log.d("Json", ((JSONObject) response).toString());
        try {
            if (jsonObject.getString("status").toLowerCase().equals("ok")) {
                JSONArray routes = jsonObject.getJSONArray("routes");

                JSONArray legs = routes.getJSONObject(0).getJSONArray("legs");
                JSONObject leg = legs.getJSONObject(0);
                JSONArray steps = leg.getJSONArray("steps");

                for (int i = 0; i < steps.length(); i++) {
                    JSONObject step = steps.getJSONObject(i);
                    JSONObject startLocation = step.getJSONObject("start_location");
                    JSONObject endLocation = step.getJSONObject("end_location");

                    LatLng lastLocation = (this.locations.size() != 0) ? (this.locations.get(this.locations.size() - 1)) : null;
                    if (lastLocation == null)
                        this.locations.add(new LatLng(startLocation.getDouble("lat"), startLocation.getDouble("lng")));
                    this.locations.add(new LatLng(endLocation.getDouble("lat"), endLocation.getDouble("lng")));
                }
                this.directionsAPIListener.onRouteAvailable(locations);

                Log.d("Route", locations.toString());
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error)
    {

    }
}
