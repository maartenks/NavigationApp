package com.example.navigationapplication.logic;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.example.navigationapplication.data.Waypoint;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class JsonParser {

    private Context context;
    private File file;

    public JsonParser(Context context) {
        this.context = context;
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            CreateDirectory();
            FileInputStream is = new FileInputStream(new File(file.getAbsolutePath()));
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        Log.d("The data", json);
        return json;
    }

    public void CreateDirectory() {
        File file = new File(context.getFilesDir(), "JsonLoc");
        if (!file.exists()) {
            file.mkdir();
        }
        Log.d("Does it exist? ", String.valueOf(file.exists()));
        File mypath = new File(file.getAbsolutePath(), "JsonLocations.json");
        Log.d("Does it exist", String.valueOf(mypath.exists()));
        try {
            if (!mypath.exists()) {
                mypath.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.file = mypath;
    }

    public ArrayList<Waypoint> parseFile(String JSONstring) {
        ArrayList<Waypoint> waypoints = new ArrayList<>();
        SharedPreferenceManager sharedPreferenceManager = SharedPreferenceManager.getInstance(context);
        if (JSONstring != null) {
            try {
                JSONArray obj = new JSONArray(JSONstring);
                for (int i = 0; i < obj.length(); i++) {
                    String name = obj.getJSONObject(i).getString("name");
                    LatLng position = new LatLng(
                            obj.getJSONObject(i).getJSONObject("coordinates").getDouble("lat"),
                            obj.getJSONObject(i).getJSONObject("coordinates").getDouble("lng")
                    );
                    String streetname = obj.getJSONObject(i).getString("streetName");
                    String colorHex = obj.getJSONObject(i).getString("colorHex");
                    int color = Integer.parseInt(colorHex);
                    boolean isFavorite = obj.getJSONObject(i).getBoolean("isFavorite");
                    Waypoint waypoint = new Waypoint(name, position, streetname, color, isFavorite);
                    waypoint.setFavorite(sharedPreferenceManager.getIsFavourite(waypoint));
                    waypoints.add(waypoint);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return waypoints;
        }
        return waypoints;
    }

    public void AddWaypoint(Waypoint waypoint) {
        try {

            JSONArray array = new JSONArray();
            if (loadJSONFromAsset() != null && !loadJSONFromAsset().equals("")) {
                array = new JSONArray(loadJSONFromAsset());
            }
            JSONObject waypoints = new JSONObject();
            JSONObject latlng = new JSONObject();
            latlng.put("lat", waypoint.getLocation().latitude);
            latlng.put("lng", waypoint.getLocation().longitude);
            waypoints.put("name", waypoint.getName());
            waypoints.put("coordinates", latlng);
            waypoints.put("streetName", waypoint.getStreetName());
            waypoints.put("colorHex", waypoint.getColor());
            waypoints.put("isFavorite", waypoint.isFavorite());
            array.put(waypoints);
            ClearFile();
            FileOutputStream fs = new FileOutputStream(new File(file.getAbsolutePath()));

            byte[] arrayInBytes = array.toString().getBytes();
            fs.write(arrayInBytes);
            fs.flush();
            fs.close();
            loadJSONFromAsset();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ClearFile() {
        PrintWriter write = null;
        try {
            write = new PrintWriter(file.getAbsolutePath());
            write.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
