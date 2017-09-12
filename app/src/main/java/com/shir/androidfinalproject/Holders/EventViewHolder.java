package com.shir.androidfinalproject.Holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.shir.androidfinalproject.Enums.Status;
import com.shir.androidfinalproject.Models.Event;
import com.shir.androidfinalproject.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.Date;
/**
 * Created by shir on 09-Sep-17.
 */

public class EventViewHolder extends RecyclerView.ViewHolder {

    public TextView titleView;
    public TextView dateView;
    public TextView addressView;
    public TextView invitationInfoView;
    public MaterialBetterSpinner statusSpinner;
//    public Button btnEventPreference;
    public Button btnEventDate;
    public Button btnEventLocation;

    public EventViewHolder(View itemView) {
        super(itemView);

        titleView = (TextView)itemView.findViewById(R.id.event_title);
        dateView = (TextView)itemView.findViewById(R.id.event_date);
        addressView = (TextView) itemView.findViewById(R.id.event_address);
        invitationInfoView  = (TextView) itemView.findViewById(R.id.event_invitation_info);
        statusSpinner = (MaterialBetterSpinner)itemView.findViewById(R.id.sp_event_status);
        //btnEventPreference = (Button)itemView.findViewById(R.id.btn_event_preference);
        btnEventDate = (Button)itemView.findViewById(R.id.btn_event_date);
        btnEventLocation = (Button)itemView.findViewById(R.id.btn_event_location);
    }

    public void bindToEvent(Event event, String userID, View.OnClickListener preferenceClickListener) {
        titleView.setText(event.title);
        dateView.setText(event.selectedDate.toString());
        addressView.setText(event.selectedLocation);
        //btnEventPreference.setOnClickListener(preferenceClickListener);
        btnEventDate.setOnClickListener(preferenceClickListener);
        btnEventLocation.setOnClickListener(preferenceClickListener);

        Status userStatus = event.usersStatus.get(userID);
        setPreferenceButton(userStatus, event.lastUpdateDate);

        if (event.uid == userID)
        {
            invitationInfoView.setText("You are hosting this Event");
            statusSpinner.setSelection(Status.Host.ordinal());
            statusSpinner.setEnabled(false);
        }
        else {
            statusSpinner.setEnabled(true);
            if (userStatus == Status.Invited)
            {
                invitationInfoView.setText( event.hostName + " invited you to this event");
                statusSpinner.setSelection(Status.Invited.ordinal());

            }
            else if (userStatus == Status.Going)
            {
                invitationInfoView.setText(String.valueOf(event.goingCount) + " guests are going");
                statusSpinner.setSelection(Status.Going.ordinal());
            }
            else if (userStatus == Status.NotGoing)
            {
                invitationInfoView.setText("You are not going to this event");
                statusSpinner.setSelection(Status.NotGoing.ordinal());
            }
        }
    }

    private void setPreferenceButton(Status userStatus, Date lastUpdateDate){
        Date currDate = new Date();

        if (lastUpdateDate.compareTo(currDate) == -1) {
            //btnEventPreference.setVisibility(View.INVISIBLE);
            btnEventDate.setVisibility(View.INVISIBLE);
            btnEventLocation.setVisibility(View.INVISIBLE);
        } else {
            if ((userStatus == Status.Host) ||
                (userStatus == Status.Going))
            {
                //btnEventPreference.setVisibility(View.VISIBLE);
                btnEventDate.setVisibility(View.VISIBLE);
                btnEventLocation.setVisibility(View.VISIBLE);
            }
            else if ((userStatus == Status.Invited) ||
                     (userStatus == Status.NotGoing))
            {
                //btnEventPreference.setVisibility(View.INVISIBLE);
                btnEventDate.setVisibility(View.INVISIBLE);
                btnEventLocation.setVisibility(View.INVISIBLE);
            }
        }
    }
}
