package com.colman.androidfinalproject.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.colman.androidfinalproject.Holders.DateViewHolder;
import com.colman.androidfinalproject.R;

import java.util.List;

public class DateListAdapter extends RecyclerView.Adapter<DateViewHolder> {

    private List<String> mDates;

    public DateListAdapter(List<String> lstDates) {
        this.mDates = lstDates;
    }

    @Override
    public DateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_date, parent, false);

        return new DateViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DateViewHolder dateViewHolder, int position) {
        dateViewHolder.bindToPlace(mDates.get(position));
    }

    @Override
    public int getItemCount() {
        if(mDates==null) return 0;
        return mDates.size();
    }
}