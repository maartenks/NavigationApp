package com.example.navigationapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.navigationapplication.data.DropdownItem;
import com.example.navigationapplication.data.Waypoint;
import com.example.navigationapplication.logic.ItemAdapter;
import com.example.navigationapplication.logic.JsonParser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    Spinner dropDownSpinner;
    private ArrayList<DropdownItem> dropdownItems;
    private ArrayList<Waypoint> waypoints;
    private MapView mapView;
    private ItemAdapter adapter;
    private ImageButton addWaypoint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initList();
        dropDownSpinner = findViewById(R.id.dropdown_Spinner);

        addWaypoint = findViewById(R.id.add_waypoint_button);

        addWaypoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuFragment menuFragment = new MenuFragment();
                menuFragment.show(getSupportFragmentManager(), "Menu Fragment");
            }
        });
        waypoints = new ArrayList<>();

        adapter = new ItemAdapter(this, dropdownItems);
        dropDownSpinner.setAdapter(adapter);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mapView = (MapView) findViewById(R.id.google_map);
        mapView.onCreate(mapViewBundle);

        mapView.getMapAsync(this);
    }

    public void buildWaypoint (GoogleMap googleMap){
        JsonParser jsonParser = new JsonParser();

        waypoints = jsonParser.parseFile(jsonParser.loadJSONFromAsset(this, "JsonLocations"));

        for(Waypoint waypoint : waypoints){
            LatLng coords = waypoint.getLocation();
            googleMap.addMarker(new MarkerOptions().position(coords).title(waypoint.getName()));
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }
        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void initList() {
        dropdownItems = new ArrayList<>();
        dropdownItems.add(new DropdownItem("", R.drawable.ic_keyboard_arrow_down_black_24dp));
        dropdownItems.add(new DropdownItem("List", R.drawable.ic_menu_black));
        dropdownItems.add(new DropdownItem("Info", R.drawable.ic_info_outline_black_24dp));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
