package com.example.navigationapplication.logic;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public interface DirectionsAPIListener
{
    void onRouteAvailable(ArrayList<LatLng> locations);
}
