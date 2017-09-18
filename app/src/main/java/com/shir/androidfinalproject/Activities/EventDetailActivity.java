package com.shir.androidfinalproject.Activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.shir.androidfinalproject.Adapters.CommentAdapter;
import com.shir.androidfinalproject.CallBacks.AddCommentCallback;
import com.shir.androidfinalproject.CallBacks.GetAllCommentsCallback;
import com.shir.androidfinalproject.CallBacks.GetEventCallback;
import com.shir.androidfinalproject.CallBacks.GetSelectedItemCallback;
import com.shir.androidfinalproject.Enums.Status;
import com.shir.androidfinalproject.Fragments.SingleChoiceDialog;
import com.shir.androidfinalproject.Models.Comment;
import com.shir.androidfinalproject.Models.Event;
import com.shir.androidfinalproject.R;
import com.shir.androidfinalproject.Data.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventDetailActivity extends BaseActivity implements View.OnClickListener{

    public static final String TAG = "EventDetailActivity";
    public static final String EVENT_ID = "EVENT_ID";

    //    include_event_header eventHeaderLayout
    private TextView titleView;
    private TextView dateView;
    private TextView addressView;
    private TextView invitationInfoView;
    private Spinner spUserStatus;
    private Button btnEventDate;
    private Button btnEventLocation;
    private TextView tvEventDescription;
    private EditText editEventComment;
    private Button btnPostComment;

    private RecyclerView mCommentsRecycler;
    private CommentAdapter mAdapter;
    private RecyclerView.LayoutManager mManager;

    private String strEventID;
    private List<Comment> mComments = new ArrayList<>();
    private Event currEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        getSupportActionBar().setTitle("Event details");

        // Get event key from intent
        strEventID = getIntent().getStringExtra(EVENT_ID);

        initViews();
        initListeners();
    }

    private void initViews() {

        titleView = (TextView)findViewById(R.id.event_title);
        dateView = (TextView)findViewById(R.id.event_date);
        addressView = (TextView)findViewById(R.id.event_address);
        invitationInfoView  = (TextView)findViewById(R.id.event_invitation_info);
        spUserStatus = (Spinner)findViewById(R.id.spUserStatus);
        btnEventDate = (Button)findViewById(R.id.btn_event_date);
        btnEventLocation = (Button)findViewById(R.id.btn_event_location);
        tvEventDescription = (TextView)findViewById(R.id.tvEventDescription);
        editEventComment = (EditText) findViewById(R.id.editEventComment);
        btnPostComment = (Button) findViewById(R.id.btnPostComment);

        mCommentsRecycler = (RecyclerView) findViewById(R.id.recycler_event_comments);
        mCommentsRecycler.setHasFixedSize(true);
        mManager = new LinearLayoutManager(this);
        mCommentsRecycler.setLayoutManager(mManager);
        mAdapter = new CommentAdapter(mComments);
        mCommentsRecycler.setAdapter(mAdapter);
    }

    private void initListeners() {
        btnPostComment.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        getCurrEvent();
        getCurrEventComments();
    }

    private void getCurrEvent(){
        model.instance.getEvent(strEventID, new GetEventCallback() {
            @Override
            public void onCompleted(Event event) {
                currEvent = event;
                setEventDetails(currEvent);
            }

            @Override
            public void onCanceled() {}
        });
    }

    private void setEventDetails(Event event) {

        titleView.setText(event.title);
        dateView.setText(event.getChosenDate());
        addressView.setText(event.getChosenLocation());
        tvEventDescription.setText(event.description);

        Status userStatus = Status.fromName(event.usersStatus.get(userID));
        setPreferenceButton(userStatus, event.lastUpdateDate);

        if (btnEventDate.getVisibility() == View.VISIBLE){
            btnEventDate.setOnClickListener(this);
        }

        if (btnEventLocation.getVisibility() == View.VISIBLE){
            btnEventLocation.setOnClickListener(this);
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

        spUserStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (pos > 0){
                    String selectedStatus = parent.getItemAtPosition(pos).toString();

                    if (currEvent.setUserStatus(userID, Status.fromName(selectedStatus))){
                        model.instance.notifyEventChanged(currEvent);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
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

    private void getCurrEventComments(){

        showProgressDialog();

        model.instance.getEventComments(strEventID, new GetAllCommentsCallback() {
            @Override
            public void onCompleted(List<Comment> comments) {
                mComments.clear();
                mComments.addAll(comments);
                hideProgressDialog();
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCanceled() {
                hideProgressDialog();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_event_date:
                onSelectDateClicked();
                break;
            case R.id.btn_event_location:
                onSelectLocationClicked();
                break;
            case R.id.btnPostComment:
                editEventComment.setError(null);
                String strComment = editEventComment.getText().toString();

                if ((strComment != null) && (!strComment.isEmpty())){
                    postComment(strComment);
                } else {
                    editEventComment.setError("no comment");
                }

                break;
        }
    }

    private void onSelectDateClicked() {

        SingleChoiceDialog dialog = new SingleChoiceDialog(new GetSelectedItemCallback() {
            @Override
            public void onApproved(String selectedItem) {

                if (currEvent.setSelectedDate(userID, selectedItem)){
                    model.instance.notifyEventChanged(currEvent);
                }
            }

            @Override
            public void onCanceled() { }
        });

        Bundle bundle = new Bundle();
        bundle.putStringArrayList(SingleChoiceDialog.ITEMS, getDialogItems(currEvent.datesList));
        bundle.putString(SingleChoiceDialog.EVENT_ID, currEvent.eventID);
        bundle.putString(SingleChoiceDialog.TITLE, "SELECT DATE");
        bundle.putInt(SingleChoiceDialog.SELECTED, currEvent.getSelectedDateIndex(userID));
        dialog.setArguments(bundle);
        dialog.show(getFragmentManager(), "Dialog");
    }

    private void onSelectLocationClicked() {

        SingleChoiceDialog dialog = new SingleChoiceDialog(new GetSelectedItemCallback() {
            @Override
            public void onApproved(String selectedItem) {

                if (currEvent.setSelectedLocation(strEventID, selectedItem)){
                    model.instance.notifyEventChanged(currEvent);
                }
            }

            @Override
            public void onCanceled() { }
        });

        Bundle bundle = new Bundle();
        bundle.putStringArrayList(SingleChoiceDialog.ITEMS, getDialogItems(currEvent.locationsList));
        bundle.putString(SingleChoiceDialog.EVENT_ID, currEvent.eventID);
        bundle.putString(SingleChoiceDialog.TITLE, "SELECT location");
        bundle.putInt(SingleChoiceDialog.SELECTED, currEvent.getSelectedLocationIndex(strEventID));
        dialog.setArguments(bundle);
        dialog.show(getFragmentManager(), "Dialog");
    }

    private ArrayList<String> getDialogItems(List<String> ls) {
        ArrayList<String> ret_val = new ArrayList<>();
        ret_val.addAll(ls);

        return ret_val;
    }

    private void postComment(String strComment) {

        showProgressDialog();
        Toast.makeText(this, "posting comment..,", Toast.LENGTH_SHORT).show();

        Comment comment = new Comment(userID, userName, strComment);

        model.instance.addComment(strEventID, comment, new AddCommentCallback(){
            @Override
            public void onCompleted() {
                hideProgressDialog();
                editEventComment.setText(null);
            }

            @Override
            public void onCanceled() {
                hideProgressDialog();
            }
        });
    }
}
