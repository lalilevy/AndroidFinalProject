package com.shir.androidfinalproject.Holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;
import com.shir.androidfinalproject.R;

 /**
 * PlaceViewHolder class for the recycler view item
 */
public class PlaceViewHolder extends RecyclerView.ViewHolder {

    public TextView txtName;
    public TextView txtAddress;

     public PlaceViewHolder(View itemView) {
         super(itemView);

         txtName = (TextView) itemView.findViewById(R.id.txtName);
         txtAddress = (TextView) itemView.findViewById(R.id.txtAddress);
     }

     public void bindToPlace(Place place) {
         txtName.setText(place.getName());
         txtAddress.setText(place.getAddress());
     }
}