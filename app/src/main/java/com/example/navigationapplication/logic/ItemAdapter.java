package com.example.navigationapplication.logic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.navigationapplication.R;
import com.example.navigationapplication.data.DropdownItem;

import java.util.ArrayList;

public class ItemAdapter extends ArrayAdapter<DropdownItem> {
    public ItemAdapter(@NonNull Context context, ArrayList<DropdownItem> resource) {
        super(context, 0, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.spinner_items_layout, parent, false
            );
        }
        ImageView imageViewLogo = convertView.findViewById(R.id.img_inView);
        TextView textView = convertView.findViewById(R.id.item_text);

        DropdownItem currentItem = getItem(position);

        if (currentItem != null) {
            imageViewLogo.setImageResource(currentItem.getItemImage());
            textView.setText(currentItem.getItemName());
        }

        return convertView;
    }
}
