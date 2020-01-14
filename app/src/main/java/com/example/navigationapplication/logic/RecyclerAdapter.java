package com.example.navigationapplication.logic;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.navigationapplication.R;
import com.example.navigationapplication.data.Waypoint;


import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {

    private ArrayList<Waypoint> dataset;
    private SharedPreferenceManager sharedPreferenceManager;

    public RecyclerAdapter(Context context, ArrayList dataset){
        this.sharedPreferenceManager = new SharedPreferenceManager(context);
        this.dataset = dataset;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_grid_item, viewGroup, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);

        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder viewHolder, int i){
        final Waypoint wp = dataset.get(i);
        viewHolder.name.setText(wp.getName());
        viewHolder.street.setText(wp.getStreetName());
        if (!sharedPreferenceManager.getIsFavourite(wp)) {
            viewHolder.favorite.setBackgroundResource(R.drawable.ic_star_border_black_24dp);
        } else {
            viewHolder.favorite.setBackgroundResource(R.drawable.ic_star_black_24dp);
        }
        viewHolder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPreferenceManager.getIsFavourite(wp)) {
                    sharedPreferenceManager.setIsFavourite(wp, false);
                    viewHolder.favorite.setBackgroundResource(R.drawable.ic_star_border_black_24dp);
                } else {
                    sharedPreferenceManager.setIsFavourite(wp, true);
                    viewHolder.favorite.setBackgroundResource(R.drawable.ic_star_black_24dp);
                }
            }
        });

    }

    @Override
    public int getItemCount(){
        return this.dataset.size();
    }

    public class  ItemViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView street;
        ImageView favorite;


        public ItemViewHolder(View itemview) {
            super(itemview);

            name = itemview.findViewById(R.id.waypoint_name);
            street = itemview.findViewById(R.id.waypoint_street);
            favorite = itemview.findViewById(R.id.waypoint_favorite);
        }
    }
}
