package com.shir.androidfinalproject.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.shir.androidfinalproject.Enums.Status;
import com.shir.androidfinalproject.Models.Event;
import com.shir.androidfinalproject.Models.UserEvent;
import com.shir.androidfinalproject.R;
import com.shir.androidfinalproject.data.DataManager;
import com.shir.androidfinalproject.data.common;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class EventsListAdapter extends ArrayAdapter<Event>
        implements AdapterView.OnItemSelectedListener {

    private ArrayList<Event> lstEvents;
    private final Context context;

    public EventsListAdapter(Context context, ArrayList<Event> values) {
        super(context, R.layout.item_event, values);
        this.context = context;
        this.lstEvents = values;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_event, parent, false);

        ImageView ivEventImage = (ImageView)rowView.findViewById(R.id.event_image);
        TextView tvEventName = (TextView) rowView.findViewById(R.id.event_title);
        TextView tvEventDate = (TextView) rowView.findViewById(R.id.event_date);
        TextView tvEventAddress = (TextView) rowView.findViewById(R.id.event_address);
        TextView tvEventInvitationInfo = (TextView) rowView.findViewById(R.id.event_invitation_info);
        //Button btnEventPreference = (Button) rowView.findViewById(R.id.btn_event_preference);
        //Spinner spEventStatus = (Spinner) rowView.findViewById(R.id.event_status);

        // Spinner click listener
        //spEventStatus.setOnItemSelectedListener(this);

        // Creating adapter for spinner
        ArrayAdapter<Status> dataAdapter = new ArrayAdapter<Status>
                (this.context, android.R.layout.simple_spinner_item, Status.values());
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spEventStatus.setAdapter(dataAdapter);

        Event currEvent = getItem(position);

       // ivEventImage.setImageBitmap(currEvent.photosList.get(0));
        tvEventName.setText(currEvent.title);
        //tvEventDate.setText(currEvent.dateList.getMaximom().toString());
        //tvEventAddress.setText(currEvent.locationsList.getMaximom().toString());


        Status userStatus = Status.Going;

        if (userStatus == Status.Host)
        {
            tvEventInvitationInfo.setText("You are hosting this Event");
           // spEventStatus.setSelection(Status.Host.ordinal());
           // spEventStatus.setEnabled(false);
//            btnEventPreference.setVisibility(View.VISIBLE);
        }
        else if (userStatus == Status.Invited)
        {
//            tvEventInvitationInfo.setText( getDataManager().getUserByID(currEvent.usersList.getHost().userID).username
//                                       + " invited you to this event");
//            spEventStatus.setSelection(Status.Invited.ordinal());
//            spEventStatus.setEnabled(true);
//            btnEventPreference.setVisibility(View.INVISIBLE);
        }
        else if (userStatus == Status.Going)
        {
//            tvEventInvitationInfo.setText(currEvent.usersList.stream().filter(ue -> ue.status == Status.Going).count() + " guests are going");
//            spEventStatus.setSelection(Status.Going.ordinal());
//            spEventStatus.setEnabled(true);
//            btnEventPreference.setVisibility(View.VISIBLE);

            //TODO: create view that can change status and also have set preference btn
        }
        else if (userStatus== Status.NotGoing)
        {
            tvEventInvitationInfo.setText("You are not going to this event");
//            spEventStatus.setSelection(Status.NotGoing.ordinal());
//            spEventStatus.setEnabled(true);
//            btnEventPreference.setVisibility(View.INVISIBLE);

            //TODO: create view that can change status and also have set preference btn
        }

        return rowView;
    }

    public void preferenceClickHandler(View v)
    {
        Button btnChild = (Button)v;
        btnChild.setText("I've been clicked!");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    private Bitmap getImageBitmap(Image image)
    {
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.capacity()];
        buffer.get(bytes);
        Bitmap bitmapImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, null);

        return bitmapImage;
    }

    private DataManager getDataManager(){
        return DataManager.getInstance(this.context);
    }
}
