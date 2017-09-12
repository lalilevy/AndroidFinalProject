package com.shir.androidfinalproject.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.shir.androidfinalproject.R;

import java.util.ArrayList;

/**
 * Created by shir on 9/2/2017.
 */

public class LocationsAdapter extends ArrayAdapter<String> {
    public LocationsAdapter(Context context, ArrayList<String> locations) {
        super(context, 0, locations);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        String location = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_location, parent, false);
        }
        // Lookup view for data population
        EditText locationText = (EditText) convertView.findViewById(R.id.tv_location);
        //TextView tvHome = (TextView) convertView.findViewById(R.id.tvHome);
        // Populate the data into the template view using the data object
        locationText.setText(location);
        //tvHome.setText(user.hometown);
        // Return the completed view to render on screen
        return convertView;
    }
}