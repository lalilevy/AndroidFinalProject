package com.shir.androidfinalproject.Holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.shir.androidfinalproject.Enums.Status;
import com.shir.androidfinalproject.Models.Event;
import com.shir.androidfinalproject.R;

import java.util.Date;

public class EventViewHolder extends RecyclerView.ViewHolder {

    public TextView titleView;
    public TextView dateView;
    public TextView addressView;
    public TextView invitationInfoView;
    public Spinner spUserStatus;
    public Button btnEventDate;
    public Button btnEventLocation;

    public EventViewHolder(View itemView) {
        super(itemView);

        titleView = (TextView)itemView.findViewById(R.id.event_title);
        dateView = (TextView)itemView.findViewById(R.id.event_date);
        addressView = (TextView) itemView.findViewById(R.id.event_address);
        invitationInfoView  = (TextView) itemView.findViewById(R.id.event_invitation_info);
        spUserStatus = (Spinner)itemView.findViewById(R.id.spUserStatus);
        btnEventDate = (Button)itemView.findViewById(R.id.btn_event_date);
        btnEventLocation = (Button)itemView.findViewById(R.id.btn_event_location);
    }

    public void bindToEvent(Event event,
                            String userID,
                            View.OnClickListener dateListener,
                            View.OnClickListener locationListener) {

        titleView.setText(event.title);
        dateView.setText(event.getChosenDate());
        addressView.setText(event.getChosenLocation());

        Status userStatus = Status.fromName(event.usersStatus.get(userID));
        setPreferenceButton(userStatus, event.lastUpdateDate);

        if (btnEventDate.getVisibility() == View.VISIBLE){
            btnEventDate.setOnClickListener(dateListener);
        }

        if (btnEventLocation.getVisibility() == View.VISIBLE){
            btnEventLocation.setOnClickListener(locationListener);
        }

        if (event.hostID.equals(userID))
        {
            invitationInfoView.setText("You are hosting this Event");
            spUserStatus.setVisibility(View.INVISIBLE);
        }
        else {
            spUserStatus.setVisibility(View.VISIBLE);
            if (userStatus == Status.Invited)
            {
                invitationInfoView.setText( event.hostName + " invited you to this event");
                spUserStatus.setSelection(0);
            }
            else if (userStatus == Status.Going)
            {
                invitationInfoView.setText(String.valueOf(event.getGoingUsers()) + " guests are going");
                spUserStatus.setSelection(getSpinnerItemIndex(Status.Going.getName()));
            }
            else if (userStatus == Status.Ignore)
            {
                invitationInfoView.setText("You are not going to this event");
                spUserStatus.setSelection(getSpinnerItemIndex(Status.Ignore.getName()));
            }
        }
    }

    private void setPreferenceButton(Status userStatus, Date lastUpdateDate){
        Date currDate = new Date();
        boolean bSetVisible = false;

        if ((lastUpdateDate.compareTo(currDate) != -1) &&
            ((userStatus == Status.Host) || (userStatus == Status.Going))){
            bSetVisible = true;
        }

        if (bSetVisible){
            btnEventDate.setVisibility(View.VISIBLE);
            btnEventLocation.setVisibility(View.VISIBLE);
        } else{
            btnEventDate.setVisibility(View.INVISIBLE);
            btnEventLocation.setVisibility(View.INVISIBLE);
        }
    }

    private int getSpinnerItemIndex(String item){
        return ((ArrayAdapter<String>)spUserStatus.getAdapter()).getPosition(item);

    }
}
