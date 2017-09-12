package com.shir.androidfinalproject.Fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.shir.androidfinalproject.Dialogs.DialogFragments;
import com.shir.androidfinalproject.Models.EventDate;
import com.shir.androidfinalproject.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AddEventDatesFragment extends Fragment
        implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener,
        AdapterView.OnItemClickListener{

    public static final String TAG = "AddEventDatesFragment";

    TextView tvLastEventUpdaeDate;
    ImageView ivAddLastUpdateDate;
    NumberPicker npDuration;
    TextView tvAddDate;
    TextView tvAddTime;
    ImageView ivAddDate;
    ListView lvDatesList;
    ImageView btnInviteFriends;

    boolean bIsAddDate;
    Date dtLastUpdate, dtCurrAddDate;
    int nDuration, year,  month, dayOfMonth, hourOfDay, minute;
    ArrayList<EventDate> lstEventDates;
    ListAdapter adapter;

    private AddDatesListener mListener;

    public AddEventDatesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EditStudentFragment.
     */
    public static AddEventDatesFragment newInstance() {
        AddEventDatesFragment fragment = new AddEventDatesFragment();
        //Bundle args = new Bundle();
//        args.putSerializable(STUDENT, student);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_dates, container, false);

        tvLastEventUpdaeDate = (TextView) view.findViewById(R.id.tv_last_update_date);
        ivAddLastUpdateDate = (ImageView) view.findViewById(R.id.iv_last_update_date);
        npDuration = (NumberPicker) view.findViewById(R.id.np_duration);
        tvAddDate = (TextView) view.findViewById(R.id.tv_add_date);
        tvAddTime = (TextView) view.findViewById(R.id.tv_add_time);
        ivAddDate = (ImageView) view.findViewById(R.id.iv_add_date);
        lvDatesList = (ListView) view.findViewById(R.id.lv_dates_list);
        btnInviteFriends = (ImageView) view.findViewById(R.id.btn_invite_friends);

        // listening to single list item on click
        npDuration.setOnClickListener(this);
        ivAddDate.setOnClickListener(this);
        btnInviteFriends.setOnClickListener(this);

        npDuration.setMaxValue(8760);
        npDuration.setMinValue(1);
        npDuration.setWrapSelectorWheel(false);

        initDataMembers();

        //adapter = new VotesListAdapter(getActivity(), lstEventDates);

        // Binding resources Array to ListAdapter
        lvDatesList.setAdapter(adapter);

        // listening to single list item on click
        lvDatesList.setOnItemClickListener(this);

        return view;
    }

    private void initDataMembers(){
        nDuration = 1;
        lstEventDates = new ArrayList<>();
        year = 0;
        month = 0;
        dayOfMonth = 0;
        hourOfDay = 0;
        minute = 0;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_last_update_date:
                bIsAddDate = false;
//                DialogFragments.DatePickerFragment fragment1 = new DialogFragments.DatePickerFragment();
//                fragment1.show(getFragmentManager(), "date");
//

                String strLastUpdate = tvLastEventUpdaeDate.getText().toString();
                if (TextUtils.isEmpty(strLastUpdate)) {
                    tvLastEventUpdaeDate.setError("Must to Enter a Last Update date");

                    dtLastUpdate = (Date)tvLastEventUpdaeDate.getText();
                    return;
                }
                try {
                    DateFormat targetFormat = new SimpleDateFormat("MM/dd/yyyy"); //Output date format
                    dtLastUpdate = targetFormat.parse(strLastUpdate);
                } catch (Exception e) {}

                break;
            case R.id.np_duration:
                nDuration = npDuration.getValue();
                break;
            case R.id.iv_add_date:
                bIsAddDate = true;

//                DialogFragments.DatePickerFragment fragment2 = new DialogFragments.DatePickerFragment();
//                fragment2.show(getFragmentManager(), "date");

                String strDate = tvAddDate.getText().toString();
                String strTime = tvAddTime.getText().toString();
                if (TextUtils.isEmpty(strDate)) {
                    tvAddDate.setError("Must to Enter a start date");
                    return;
                }
                else if (TextUtils.isEmpty(strTime)) {
                    tvAddTime.setError("Must to Enter a start time");
                    return;
                }
                else{
                    try{
                        DateFormat targetFormat = new SimpleDateFormat("MM/dd/yyyy"); //Output date format
                        dtCurrAddDate = targetFormat.parse(strDate);

                        SimpleDateFormat df = new SimpleDateFormat("hh:mm");
                        Date d = df.parse(strTime);

                        dtCurrAddDate.setHours(d.getHours());
                        dtCurrAddDate.setMinutes(d.getMinutes());

                        EventDate ed = new EventDate(dtCurrAddDate);
                        lstEventDates.add(ed);
                    } catch (Exception e) {}
                }
                break;
            case R.id.btn_invite_friends:
                onInviteFriends();

                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (view.getId()) {
            case R.id.item_row:
                //onShowStudentDetails(position);
                break;
        }
    }

    private void onInviteFriends() {
        String strLastUpdateDate = tvLastEventUpdaeDate.getText().toString();
        if (TextUtils.isEmpty(strLastUpdateDate)) {
            tvLastEventUpdaeDate.setError("Must to Enter a last update date");
            return;
        }
        else if (lstEventDates.isEmpty()){
            tvAddDate.setError("Must to add a date option");
            return;
        }

        if (mListener != null) {
            mListener.onInviteFriendsClick(dtLastUpdate, nDuration, lstEventDates);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddDatesListener) {
            mListener = (AddDatesListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AddDatesListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar cal = new GregorianCalendar(year, month, day, this.hourOfDay, this.minute);

        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        if (bIsAddDate){
            this.year = year;
            this.month = month;
            this.dayOfMonth = day;

            setCurrAddDate();

            DialogFragments.TimePickerFragment frag = new DialogFragments.TimePickerFragment();
            frag.show(getFragmentManager(), "time");
        }
        else{
            dtLastUpdate = cal.getTime();
            tvLastEventUpdaeDate.setText(dateFormat.format(dtLastUpdate));
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
       this.hourOfDay = hourOfDay;
        this.minute = minute;

        setCurrAddDate();
    }

    private void setCurrAddDate(){
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        Calendar cal = new GregorianCalendar(this.year, this.month, this.dayOfMonth, this.hourOfDay, this.minute);
        dtCurrAddDate = cal.getTime();

        tvAddDate.setText(dateFormat.format(dtCurrAddDate));
        tvAddTime.setText(hourOfDay + ":" + minute);
    }

//    // Method for remove Single item from list
//    protected void removeItemFromList(int position)
//    {
//        final int deletePosition = position;
//
//        lstVotes.remove(deletePosition);
//        this.notifyDataSetChanged();
//        this.notifyDataSetInvalidated();
//    }
    public interface AddDatesListener {
        void onInviteFriendsClick(Date lastUpdate, int duration, ArrayList<EventDate> EventDates);
    }
}

