package com.example.navigationapplication.data;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;

public class Waypoint {
    private String name;
    private LatLng location;
    private int color;
    private boolean isFavorite;
    private String streetName;

    public Waypoint(String name, LatLng location, String streetName, int color, boolean isFavorite) {
        this.name = name;
        this.location = location;
        this.streetName = streetName;
        this.color = color;
        this.isFavorite = isFavorite;
    }

    public String getName() {
        return name;
    }

    public String getStreetName() {
        return streetName;
    }

    public LatLng getLocation() {
        return location;
    }

    public int getColor() {
        return color;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
