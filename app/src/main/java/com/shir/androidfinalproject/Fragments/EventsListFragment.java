package com.shir.androidfinalproject.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.shir.androidfinalproject.Activities.EventDetailActivity;
import com.shir.androidfinalproject.Dialogs.PreferenceDialog;
import com.shir.androidfinalproject.Holders.EventViewHolder;
import com.shir.androidfinalproject.Models.User;
import com.shir.androidfinalproject.Models.Event;
import com.shir.androidfinalproject.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventsListListener} interface
 * to handle interaction events.
 * Use the {@link EventsListListener#newInstance} factory method to
 * create an instance of this fragment.
 */
public abstract class EventsListFragment extends Fragment {

    public static final String TAG = "EventsListFragment";

    // [START define_database_reference]
    private DatabaseReference mDatabase;
    // [END define_database_reference]

    private FirebaseRecyclerAdapter<Event, EventViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    public EventsListFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_events, container, false);

        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END create_database_reference]

        mRecycler = (RecyclerView) view.findViewById(R.id.event_list);
        mRecycler.setHasFixedSize(true);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with the Query
        Query eventsQuery = getQuery(mDatabase);
        mAdapter = new FirebaseRecyclerAdapter<Event, EventViewHolder>(Event.class, R.layout.item_event,
                EventViewHolder.class, eventsQuery) {
            @Override
            protected void populateViewHolder(final EventViewHolder viewHolder, final Event model, final int position) {
                final DatabaseReference eventRef = getRef(position);

                // Set click listener for the whole post view
                final String eventKey = eventRef.getKey();
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Launch PostDetailActivity
                        Intent intent = new Intent(getActivity(), EventDetailActivity.class);
                        intent.putExtra(EventDetailActivity.EXTRA_EVENT_KEY, eventKey);
                        startActivity(intent);
                    }
                });

                // Bind Post to ViewHolder, setting OnClickListener for the star button
                viewHolder.bindToEvent(model, getUid(), new View.OnClickListener() {
                    @Override
                    public void onClick(View preferenceView) {

                        // Need to write to both places the post is stored
                        DatabaseReference globalEventRef = mDatabase.child("events").child(eventRef.getKey());
                        DatabaseReference userEventRef = mDatabase.child("user-events").child(model.uid).child(eventRef.getKey());

                        int viewID= preferenceView.getId();

                        // Run two transactions
                        switch (viewID){
                            case R.id.btn_event_date:
                                onSelectDateClicked(globalEventRef);
                                onSelectDateClicked(userEventRef);
                                break;
                            case R.id.btn_event_location:
                                onSelectLocationClicked(globalEventRef);
                                onSelectLocationClicked(userEventRef);
                                break;
                        }
                    }
                });
            }
        };
        mRecycler.setAdapter(mAdapter);
    }

    // [START event_preference_transaction]
    private void onSelectDateClicked(DatabaseReference eventRef) {

        eventRef.runTransaction(new Transaction.Handler() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Event e = mutableData.getValue(Event.class);
                if (e == null) {
                    return Transaction.success(mutableData);
                }

                String uid = getUid();
                Date minDate = new Date(Long.MIN_VALUE);
                Date currDate = minDate;

                List<String> dateListString = new ArrayList<String>(e.datesList.size());

                dateListString.addAll(e.datesList.stream().map(String::valueOf).collect(Collectors.toList()));

                if (e.usersDates.containsKey(uid)) {
                    currDate = e.usersDates.get(uid);
                }

                PreferenceDialog datePreferenceDialog = new PreferenceDialog();
                datePreferenceDialog.initVars(currDate.toString(), dateListString);
                //TODO SHIR
                // datePreferenceDialog.show(getFragmentManager(), "123");//getFragmentManager(), "datePreferenceDialog");

                DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy G HH:mm:ss Z");
                try{
                    Date selectedDate = df.parse(datePreferenceDialog.selectedValue);

                    if ((selectedDate == minDate) &&
                        (currDate != minDate)){

                       // p.starCount = p.starCount - 1;
                        //p.stars.remove(getUid());
                    } else {
                        // Star the post and add self to stars
                        //p.starCount = p.starCount + 1;
                        e.usersDates.put(uid, currDate);
                    }

                } catch (ParseException e1) {
                    e1.printStackTrace();
                }


                // Set value and report transaction success
                mutableData.setValue(e);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(TAG, "eventTransaction:onComplete:" + databaseError);
            }
        });
    }

    private void onSelectLocationClicked(DatabaseReference eventRef) {

        eventRef.runTransaction(new Transaction.Handler() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Event e = mutableData.getValue(Event.class);
                if (e == null) {
                    return Transaction.success(mutableData);
                }

                String uid = getUid();
                String minLoc = "";
                String currLoc = minLoc;

                List<String> dateListString = new ArrayList<String>(e.locationsList.size());

                dateListString.addAll(e.locationsList);

                if (e.usersLocations.containsKey(uid)) {
                    currLoc = e.usersLocations.get(uid);
                }

                PreferenceDialog locationPreferenceDialog = new PreferenceDialog();
                locationPreferenceDialog.initVars(currLoc.toString(), dateListString);
                //TODO SHIR
                // locationPreferenceDialog.show(getFragmentManager(), "123");//getFragmentManager(), "datePreferenceDialog");

                String selectedLoc = locationPreferenceDialog.selectedValue;

                if ((selectedLoc == minLoc) &&
                        (currLoc != minLoc)){

                    // p.starCount = p.starCount - 1;
                    //p.stars.remove(getUid());
                } else {
                    // Star the post and add self to stars
                    //p.starCount = p.starCount + 1;
                    e.usersLocations.put(uid, currLoc);
                }


                // Set value and report transaction success
                mutableData.setValue(e);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(TAG, "eventTransaction:onComplete:" + databaseError);
            }
        });
    }
    // [END event_preference_transaction]

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public abstract Query getQuery(DatabaseReference databaseReference);
}
