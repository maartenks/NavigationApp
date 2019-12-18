package com.example.navigationapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.navigationapplication.data.Waypoint;

public class MenuFragment extends AppCompatDialogFragment {

    private MenuFragmentListener listener;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_pop_up_fragment, null);

        builder.setView(view);

        return builder.create();
    }

    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (MenuFragmentListener) context;
        } catch (Exception e) {

        }
    }

    public interface MenuFragmentListener {
        void applyWaypoint(Waypoint waypoint);
    }
}
