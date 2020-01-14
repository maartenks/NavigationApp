package com.example.navigationapplication.logic;

import android.content.Context;
import android.content.SharedPreferences;


import com.example.navigationapplication.data.Waypoint;

public class SharedPreferenceManager
{
    public static final String SHARED_PREFS = "sharedpref";
    private static SharedPreferenceManager insance;
    public Context context;

    public SharedPreferenceManager(Context context)
    {
        this.context = context;
    }

    public static SharedPreferenceManager getInstance(Context context)
    {
        if (insance == null) {
            insance = new SharedPreferenceManager(context);
        }

        return insance;
    }

    public boolean getIsFavourite(Waypoint waypoint)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(waypoint.getName()+"Favourite", false);
    }

    public void setIsFavourite(Waypoint waypoint, boolean value)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(waypoint.getName() + "Favourite", value);
        editor.apply();
    }
}