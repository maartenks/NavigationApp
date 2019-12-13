package com.example.navigationapplication.logic;

import android.content.Context;
import android.graphics.Color;

import com.example.navigationapplication.data.Waypoint;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.InputStream;
import java.util.ArrayList;

public class JsonParser {

    public String loadJSONFromAsset(Context context, String file) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    public ArrayList<Waypoint> parseFile(String JSONstring) {
        ArrayList<Waypoint> waypoints = new ArrayList<>();
        try {
            JSONArray obj = new JSONArray(JSONstring);
            for (int i = 0; i < obj.length(); i++) {
                String name = obj.getJSONObject(i).getString("name");
                LatLng position = new LatLng(
                        obj.getJSONObject(i).getJSONObject("coordinates").getInt("lat"),
                        obj.getJSONObject(i).getJSONObject("coordinates").getInt("lng")
                );
                String streetname = obj.getJSONObject(i).getString("streetName");
                String colorHex = obj.getJSONObject(i).getString("colorHex");
                int color = Color.parseColor(colorHex);
                boolean isFavorite = obj.getJSONObject(i).getBoolean("isFavorite");
                waypoints.add(new Waypoint(name, position, streetname, color, isFavorite));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return waypoints;
    }
}
