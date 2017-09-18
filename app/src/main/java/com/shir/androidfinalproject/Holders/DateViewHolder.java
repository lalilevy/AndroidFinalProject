package com.colman.androidfinalproject.Holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.colman.androidfinalproject.R;

public class DateViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "DateViewHolder";
    public TextView tvItemDate;

    public DateViewHolder(View itemView) {
        super(itemView);

        tvItemDate = (TextView) itemView.findViewById(R.id.tvItemDate);
    }

    public void bindToPlace(String strDate) {
        tvItemDate.setText(strDate);
    }
}
