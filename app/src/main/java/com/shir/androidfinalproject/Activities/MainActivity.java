package com.shir.androidfinalproject.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.shir.androidfinalproject.Adapters.EventsListAdapter;
import com.shir.androidfinalproject.CallBacks.GetAllEventsCallback;
import com.shir.androidfinalproject.Models.Event;
import com.shir.androidfinalproject.R;
import com.shir.androidfinalproject.Data.model;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private FloatingActionButton btnAddNewEvent;

    private RecyclerView mRecycler;
    private EventsListAdapter mAdapter;
    private RecyclerView.LayoutManager mManager;

    private List<Event> mEvents = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Event Scheduler Feed");

        initViews();
        initListeners();
    }

    private void initViews() {
        btnAddNewEvent = (FloatingActionButton) findViewById(R.id.btnAddNewEvent);

        mRecycler = (RecyclerView)findViewById(R.id.event_list);
        mRecycler.setHasFixedSize(true);
        mManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(mManager);
        mAdapter = new EventsListAdapter(this, mEvents, userID, userName);
        mRecycler.setAdapter(mAdapter);
    }

    private void initListeners(){
        btnAddNewEvent.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        getEvents();
    }

    private void getEvents(){
        showProgressDialog();

        try{
            model.instance.getEvents(userID, new GetAllEventsCallback() {
                @Override
                public void onCompleted(List<Event> events) {
                    mEvents.clear();
                    mEvents.addAll(events);
                    hideProgressDialog();
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCanceled() {
                    hideProgressDialog();
                }
            });
        }catch (Exception ex){
            hideProgressDialog();
            Log.d(TAG, ex.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        int nViewID = v.getId();

        switch (nViewID){
            case R.id.btnAddNewEvent:
                Intent intent = new Intent(MainActivity.this, NewEventActivity.class);
                Bundle bundle = new Bundle();
                intent.putExtras(bundle);
                intent.putExtra(USER_NAME, userName);
                intent.putExtra(USER_ID, userID);
                startActivity(intent);

                break;
        }
    }
}
