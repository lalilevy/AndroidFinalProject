package com.colman.androidfinalproject.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.colman.androidfinalproject.Holders.PlaceViewHolder;
import com.colman.androidfinalproject.R;

import java.util.List;

public class PlaceListAdapter extends RecyclerView.Adapter<PlaceViewHolder> {

    private Context mContext;
    private PlaceBuffer mPlaces;
    private List<Place> lstPlaces;

    public PlaceListAdapter(Context context, List<Place> places) {
        this.mContext = context;
        this.lstPlaces = places;
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get the RecyclerView item layout
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_location, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder holder, int position) {
        holder.bindToPlace(lstPlaces.get(position));
    }

    public void swapPlaces(PlaceBuffer newPlaces){
        mPlaces = newPlaces;
        if (mPlaces != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {

        if(lstPlaces==null) return 0;
        return lstPlaces.size();
    }
}