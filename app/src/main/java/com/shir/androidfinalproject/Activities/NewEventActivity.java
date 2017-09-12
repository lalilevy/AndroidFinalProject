package com.shir.androidfinalproject.Activities;

import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.Toast;

import com.shir.androidfinalproject.Fragments.AddEventDatesFragment;
import com.shir.androidfinalproject.Fragments.AddEventDetailsFragment;
import com.shir.androidfinalproject.Fragments.AddEventLocationsFragment;
import com.shir.androidfinalproject.Models.Event;
import com.shir.androidfinalproject.Models.EventDate;
import com.shir.androidfinalproject.R;

import java.util.ArrayList;
import java.util.Date;

public class NewEventActivity extends BaseActivity implements
        AddEventDatesFragment.AddDatesListener,
        AddEventDetailsFragment.AddEventDetailsListener,
        AddEventLocationsFragment.AddLocationsFragmentListener {

    private static final String TAG = "NewEventActivity";

    AddEventLocationsFragment addEventLocationsFragment;
    AddEventDatesFragment addEventDatesFragment;

    private Date lastUpdateDate;
    private int duration;
    private String title;
    private String description;

    ArrayList<String> locations;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        getSupportActionBar().setTitle("New Event");

        userName = getIntent().getStringExtra(MainActivity.USER_NAME);

        getFragmentManager()
                .beginTransaction()
                .add(R.id.nested_scroll_new_event, AddEventDetailsFragment.newInstance(), AddEventDetailsFragment.TAG)
                .commit();

//        if (findViewById(R.id.fragment_container) != null) {
//
//            // However, if we're being restored from a previous state,
//            // then we don't need to do anything and should return or else
//            // we could end up with overlapping fragments.
//            if (savedInstanceState != null) {
//                return;
//            }
//
//            // Create a new Fragment to be placed in the activity layout
//            addEventDetailsFragment = new AddEventDetailsFragment();
//
//            // Add the fragment to the 'fragment_container' FrameLayout
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.fragment_container, addEventDetailsFragment).commit();
//        }

    }

    @Override
    public void onGoToAddLocationClick(String Title, String Description, Date LastUpdateDate, int Duration) {
        toastMessage("onGoToAddLocationClick - ok");

        this.title = Title;
        this.description = Description;
        this.lastUpdateDate = LastUpdateDate;
        this.duration = Duration;
        //moveToLocationsFragment();
    }

    private void moveToLocationsFragment() {
        addEventLocationsFragment = new AddEventLocationsFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.nested_scroll_new_event, addEventLocationsFragment);
        transaction.addToBackStack(null);

// Commit the transaction
        transaction.commit();
    }

    private void moveToAddDatesFragment() {
        addEventDatesFragment = new AddEventDatesFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.nested_scroll_new_event, addEventDatesFragment);
        transaction.addToBackStack(null);

// Commit the transaction
        transaction.commit();
    }

    @Override
    public void onAddDatesClicked(ArrayList<String> lstLocations) {
        this.locations = lstLocations;
        //moveToAddDatesFragment();
    }

    @Override
    public void onInviteFriendsClick(Date lastUpdate, int duration, ArrayList<EventDate> EventDates) {

    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
