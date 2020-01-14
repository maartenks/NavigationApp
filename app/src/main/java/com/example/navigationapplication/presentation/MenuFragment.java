package com.example.navigationapplication.presentation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.navigationapplication.R;
import com.example.navigationapplication.data.Waypoint;
import com.example.navigationapplication.logic.JsonParser;
import com.google.android.gms.maps.model.LatLng;

public class MenuFragment extends AppCompatDialogFragment {

    private MenuFragmentListener listener;
    private Button cancel;
    private Button save;
    private ImageButton favorite;
    private TextView name;
    private Boolean isFav = false;
    private JsonParser jsonParser;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        jsonParser = new JsonParser(this.getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_pop_up_fragment, null);
        final String streetname = getArguments().getString("Streetname");
        Log.d("Streetname", streetname + " is streetname");
        String[] getLocation = getArguments().getString("LatLng").split(" ");
        String[] latlng = getLocation[1].split(",");
        double latitude = Double.parseDouble(latlng[0]);
        double longitude = Double.parseDouble(latlng[1]);
        final LatLng location = new LatLng(latitude, longitude);

        name = view.findViewById(R.id.waypoint_setname);
        cancel = (Button) view.findViewById(R.id.button_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                getFragmentManager().beginTransaction().remove(MenuFragment.this).commit();
            }
        });
        save = (Button) view.findViewById(R.id.button_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (streetname == "null") {
                    Waypoint newWaypoint = new Waypoint(name.getText().toString(), location, streetname, 20, isFav);
                    jsonParser.AddWaypoint(newWaypoint);
                    ((MainActivity)getActivity()).buildWaypoint();
                } else {
                    Waypoint newWaypoint = new Waypoint(name.getText().toString(), location, "Not known", 20, isFav);
                    jsonParser.AddWaypoint(newWaypoint);
                    ((MainActivity)getActivity()).buildWaypoint();
                }
                getFragmentManager().beginTransaction().remove(MenuFragment.this).commit();
            }
        });
        favorite = (ImageButton) view.findViewById(R.id.button_setfav);
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFav) {
                    isFav = true;
                    favorite.setImageResource(R.drawable.ic_star_black_24dp);
                } else {
                    isFav = false;
                    favorite.setImageResource(R.drawable.ic_star_border_black_24dp);
                }
            }
        });
        builder.setView(view);
        return builder.create();
    }

    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (MenuFragmentListener) context;
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
        }
    }

    public interface MenuFragmentListener {
        void applyWaypoint(Waypoint waypoint);
    }
}
