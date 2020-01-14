package com.example.navigationapplication.presentation;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navigationapplication.R;
import com.example.navigationapplication.data.Waypoint;
import com.example.navigationapplication.logic.JsonParser;
import com.example.navigationapplication.logic.RecyclerAdapter;

import java.util.ArrayList;

public class WaypointsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Waypoint> waypoints;
    RecyclerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_waypoints);
        JsonParser jsonParser = new JsonParser(this);
        waypoints = jsonParser.parseFile(jsonParser.loadJSONFromAsset());

        adapter = new RecyclerAdapter(this, waypoints);
        recyclerView = findViewById(R.id.waypoints_list_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false));


    }
}
