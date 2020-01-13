package com.example.navigationapplication.logic;

import com.example.navigationapplication.data.Waypoint;

public interface ParkingLotsAPIListener {
    void onParkingLotAvailable(Waypoint waypoint);
    void onParkingLotError(Error error);
}
