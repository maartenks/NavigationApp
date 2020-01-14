package com.example.navigationapplication.presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.example.navigationapplication.R;
import com.example.navigationapplication.data.DropdownItem;
import com.example.navigationapplication.data.Waypoint;
import com.example.navigationapplication.logic.DirectionsAPIListener;
import com.example.navigationapplication.logic.DirectionsAPIManager;
import com.example.navigationapplication.logic.ItemAdapter;
import com.example.navigationapplication.logic.JsonParser;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, DirectionsAPIListener {

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    Spinner dropDownSpinner;

    private ArrayList<DropdownItem> dropdownItems;
    private ArrayList<Waypoint> waypoints;
    private ArrayList<Marker> markers;
    private DirectionsAPIManager directionsAPIManager;

    private GoogleMap googleMap;
    private MapView mapView;

    Location currentLocation;
    private ArrayList<LatLng> locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initList();
        waypoints = new ArrayList<>();
        locations = new ArrayList<>();
        markers = new ArrayList<>();
        directionsAPIManager = new DirectionsAPIManager(getApplicationContext(), this);

        dropDownSpinner = findViewById(R.id.dropdown_Spinner);
        ItemAdapter adapter = new ItemAdapter(this, dropdownItems);
        dropDownSpinner.setAdapter(adapter);

        ImageButton addWaypoint = findViewById(R.id.add_waypoint_button);


        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mapView = findViewById(R.id.google_map);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        checkLocationPremissions();

        addWaypoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  Bundle bundle = new Bundle();
//                try {
                     bundle.putString("LatLng", currentLocation.toString());
//                    List<Address> addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
//                        bundle.putString("Streetname", addresses.get(0).getThoroughfare());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

                MenuFragment menuFragment = new MenuFragment();
                menuFragment.setArguments(bundle);
                menuFragment.show(getSupportFragmentManager(), "Menu Fragment");
            }
        });

        dropDownSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:

                        break;

                    case 1:
                        Intent intent = new Intent(MainActivity.this, WaypointsActivity.class);
                        view.getContext().startActivity(intent);
                        parent.setSelection(0);
                        break;

                    case 2:

                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                dropDownSpinner.getSelectedItem();
            }
        });
    }

    public void buildWaypoint () {
        JsonParser jsonParser = new JsonParser(this);

        waypoints = jsonParser.parseFile(jsonParser.loadJSONFromAsset());

        for (Waypoint waypoint : waypoints) {
            LatLng coords = waypoint.getLocation();
            Log.d("Location", coords.toString());
            Marker marker = googleMap.addMarker(new MarkerOptions().position(coords).title(waypoint.getName()));
            markers.add(marker);
        }
    }

    private boolean hasLocationAccess()
    {
        boolean courceLocationAccess = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean fineLocationAccess = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        return (courceLocationAccess && fineLocationAccess);
    }

    private void setupLocationServices()
    {
        FusedLocationProviderClient fushedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000).setFastestInterval(5000).setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        currentLocation = location;
                        Log.d("locationUpdate", location.getLatitude() + " : " + location.getLongitude());
                    }
                }
            }
        };
        fushedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    public void drawRoute(Waypoint waypoint) {
        this.directionsAPIManager.requestRoute(currentLocation, waypoint);
    }

    private void checkLocationPremissions()
    {
        if (!hasLocationAccess())
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        else
            setupLocationServices();
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
        this.googleMap = googleMap;
        buildWaypoint();
        if(hasLocationAccess()) {
            this.googleMap.setMyLocationEnabled(true);
        }
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                for (Waypoint waypoint: waypoints) {
                    if (marker.getTitle().equals(waypoint.getName())) {
                        marker.showInfoWindow();
                        drawRoute(waypoint);
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void onRouteAvailable(ArrayList<LatLng> locations) {
        this.googleMap.clear();
        buildWaypoint();
        this.locations.clear();
        this.locations.addAll(locations);
        Polyline polyline = this.googleMap.addPolyline(new PolylineOptions().clickable(false).addAll(locations));
    }
}
