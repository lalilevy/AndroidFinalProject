package com.colman.androidfinalproject.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.colman.androidfinalproject.CallBacks.SaveEventCallback;
import com.colman.androidfinalproject.Enums.Status;
import com.colman.androidfinalproject.Fragments.AddEventDatesFragment;
import com.colman.androidfinalproject.Fragments.AddEventDetailsFragment;
import com.colman.androidfinalproject.Fragments.AddEventLocationsFragment;
import com.colman.androidfinalproject.Fragments.AddEventUsersFragment;
import com.colman.androidfinalproject.Models.Event;
import com.colman.androidfinalproject.R;
import com.colman.androidfinalproject.Data.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewEventActivity extends BaseActivity implements
        AddEventDatesFragment.AddDatesListener,
        AddEventDetailsFragment.AddEventDetailsListener,
        AddEventLocationsFragment.AddLocationsFragmentListener,
        AddEventUsersFragment.AddEventUsersListener{

    private static final String TAG = "NewEventActivity";

    private Date dtLastUpdate;
    private int nDuration;
    private String strTitle;
    private String strDescription;

    List<String> lstLocations = new ArrayList<>();
    List<String> lstDates = new ArrayList<>();
    List<String> lstUsers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        getSupportActionBar().setTitle("New Event");

        getFragmentManager()
                .beginTransaction()
                .add(R.id.nested_scroll_new_event, AddEventDetailsFragment.newInstance(), AddEventDetailsFragment.TAG)
                .commit();
    }

    @Override
    public void onGoToAddLocationClick(String Title, String Description, Date LastUpdateDate, int Duration) {
        this.strTitle = Title;
        this.strDescription = Description;
        this.dtLastUpdate = LastUpdateDate;
        this.nDuration = Duration;
        goToAddLocationsFragment();
    }

    private void goToAddLocationsFragment() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.nested_scroll_new_event, AddEventLocationsFragment.newInstance(), AddEventLocationsFragment.TAG)
                .addToBackStack(AddEventLocationsFragment.TAG)
                .commit();
    }

    @Override
    public void onGoToAddDatesClick(List<String> locations) {
        this.lstLocations = locations;
        goToAddDatesFragment();
    }

    private void goToAddDatesFragment() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.nested_scroll_new_event, AddEventDatesFragment.newInstance(), AddEventDatesFragment.TAG)
                .addToBackStack(AddEventDatesFragment.TAG)
                .commit();
    }

    @Override
    public void onGoToAddUserClick(List<String> dates) {
        this.lstDates = dates;
        goToAddUsersFragment();
    }

    private void goToAddUsersFragment() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.nested_scroll_new_event, AddEventUsersFragment.newInstance(userID), AddEventUsersFragment.TAG)
                .addToBackStack(AddEventUsersFragment.TAG)
                .commit();
    }

    @Override
    public void onCreateEventClick(List<String> users) {
        lstUsers = users;

        showProgressDialog();
        toastMessage("creating new event...");

        Event event = new Event(userID, userName, strTitle, strDescription, dtLastUpdate, nDuration,
                lstLocations, lstDates, lstUsers);

        event.usersStatus.put(userID, Status.Host.getName());

        for (String currUser: lstUsers) {
            if (!currUser.equals(userID)){
                event.usersStatus.put(currUser, Status.Invited.getName());
            }
        }

        model.instance.addEvent(event, new SaveEventCallback() {
            @Override
            public void onCompleted(String eventKey) {
                hideProgressDialog();
                goToEventDetailActivity(eventKey);
            }

            @Override
            public void onCanceled(Exception e) {
                hideProgressDialog();
            }
        });
    }

    private void goToEventDetailActivity(String strEventID) {
        Intent intent = new Intent(NewEventActivity.this, EventDetailActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        intent.putExtra(EventDetailActivity.EVENT_ID, strEventID);
        intent.putExtra(USER_ID, userID);
        intent.putExtra(USER_NAME, userName);
        startActivity(intent);
        finish();
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
