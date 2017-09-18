package com.colman.androidfinalproject.Adapters;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.colman.androidfinalproject.Activities.BaseActivity;
import com.colman.androidfinalproject.Activities.EventDetailActivity;
import com.colman.androidfinalproject.Activities.MainActivity;
import com.colman.androidfinalproject.CallBacks.GetSelectedItemCallback;
import com.colman.androidfinalproject.Fragments.SingleChoiceDialog;
import com.colman.androidfinalproject.Enums.Status;
import com.colman.androidfinalproject.Holders.EventViewHolder;
import com.colman.androidfinalproject.Models.Event;
import com.colman.androidfinalproject.R;
import com.colman.androidfinalproject.Data.model;

import java.util.ArrayList;
import java.util.List;

public class EventsListAdapter extends RecyclerView.Adapter<EventViewHolder>  {

    private List<Event> lstEvents;
    private MainActivity context;
    private String currUserID;
    private String userName;

    public EventsListAdapter(MainActivity context, List<Event> values, String currentUserID, String strUsername) {
        this.context = context;
        this.lstEvents = values;
        this.currUserID = currentUserID;
        this.userName = strUsername;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_event, viewGroup, false);

        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        Event currEvent = lstEvents.get(position);
        holder.bindToEvent(currEvent,
                currUserID,
                v -> onSelectDateClicked(currEvent),
                v -> onSelectLocationClicked(currEvent));

        holder.spUserStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (pos > 0){
                    String selectedStatus = parent.getItemAtPosition(pos).toString();

                    if (currEvent.setUserStatus(currUserID, Status.fromName(selectedStatus))){
                        model.instance.notifyEventChanged(currEvent);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        holder.itemView.setOnClickListener(v -> {
            // Launch EventDetailActivity
            Intent intent = new Intent(context, EventDetailActivity.class);
            Bundle bundle = new Bundle();
            intent.putExtras(bundle);
            intent.putExtra(EventDetailActivity.EVENT_ID, currEvent.eventID);
            intent.putExtra(BaseActivity.USER_ID, currUserID);
            intent.putExtra(BaseActivity.USER_NAME, userName);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if(lstEvents == null) return 0;
        return lstEvents.size();
    }

    private void onSelectDateClicked(Event event) {
        android.app.FragmentManager manager = context.getFragmentManager();
        SingleChoiceDialog dialog = new SingleChoiceDialog(new GetSelectedItemCallback() {
            @Override
            public void onApproved(String selectedItem) {
                event.setSelectedDate(currUserID, selectedItem);
                model.instance.notifyEventChanged(event);
            }

            @Override
            public void onCanceled() {
            }
        });

        Bundle bundle = new Bundle();
        bundle.putStringArrayList(SingleChoiceDialog.ITEMS, getDialogItems(event.datesList));
        bundle.putString(SingleChoiceDialog.EVENT_ID, event.eventID);
        bundle.putString(SingleChoiceDialog.TITLE, "SELECT DATE");
        bundle.putInt(SingleChoiceDialog.SELECTED, event.getSelectedDateIndex(currUserID));
        dialog.setArguments(bundle);
        dialog.show(manager, "Dialog");
    }

    private void onSelectLocationClicked(Event event) {
        android.app.FragmentManager manager = context.getFragmentManager();
        SingleChoiceDialog dialog = new SingleChoiceDialog(new GetSelectedItemCallback() {
            @Override
            public void onApproved(String selectedItem) {

                event.setSelectedLocation(currUserID, selectedItem);
                    model.instance.notifyEventChanged(event);

            }

            @Override
            public void onCanceled() {
            }
        });

        Bundle bundle = new Bundle();
        bundle.putStringArrayList(SingleChoiceDialog.ITEMS, getDialogItems(event.locationsList));
        bundle.putString(SingleChoiceDialog.EVENT_ID, event.eventID);
        bundle.putString(SingleChoiceDialog.TITLE, "SELECT location");
        bundle.putInt(SingleChoiceDialog.SELECTED, event.getSelectedLocationIndex(currUserID));
        dialog.setArguments(bundle);
        dialog.show(manager, "Dialog");
    }

    private ArrayList<String> getDialogItems(List<String> ls) {
        ArrayList<String> ret_val = new ArrayList<>();
        ret_val.addAll(ls);

        return ret_val;
    }
}
