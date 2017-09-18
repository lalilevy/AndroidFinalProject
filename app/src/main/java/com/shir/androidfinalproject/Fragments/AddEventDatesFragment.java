package com.colman.androidfinalproject.Fragments;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.colman.androidfinalproject.Adapters.DateListAdapter;
import com.colman.androidfinalproject.R;
import com.colman.androidfinalproject.Data.InputValidation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddEventDatesFragment extends Fragment
        implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    public static final String TAG = "AddEventDatesFragment";
    private final String dateFormat = "dd/MM/yyyy";
    private final String timeFormat = "hh:mm";

    EditText tvSelectedEventDate;
    EditText tvSelectedEventTime;
    Button btnSelectEventDate;
    Button btnSelectEventTime;
    Button btnAddDate;
    FloatingActionButton btnGoToAddUsers;

    RecyclerView datesListRecycler;
    DateListAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    List<String> lstDates = new ArrayList<>();

    InputValidation inputValidation;
    String strCurrAddDate;
    int nYear, nMonth, nDayOfMonth, nHourOfDay, nMinute;

    private AddDatesListener mListener;

    public AddEventDatesFragment() {
        // Required empty public constructor
    }

    public static AddEventDatesFragment newInstance() {
        return new AddEventDatesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_event_dates, container, false);

        initViews(view);
        initListeners();
        initObjects();

        return view;
    }

    private void initViews(View view) {

        tvSelectedEventDate = (EditText) view.findViewById(R.id.tvSelectedEventDate);
        tvSelectedEventTime = (EditText) view.findViewById(R.id.tvSelectedEventTime);
        btnSelectEventDate = (Button) view.findViewById(R.id.btnSelectEventDate);
        btnSelectEventTime = (Button) view.findViewById(R.id.btnSelectEventTime);
        btnAddDate = (Button) view.findViewById(R.id.btnAddDate);
        btnGoToAddUsers = (FloatingActionButton) view.findViewById(R.id.btnGoToAddUsers);

        datesListRecycler = (RecyclerView) view.findViewById(R.id.datesListRecycler);
        datesListRecycler.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());// getApplicationContext());
        datesListRecycler.setLayoutManager(mLayoutManager);
        mAdapter = new DateListAdapter(lstDates);
        datesListRecycler.setAdapter(mAdapter);
    }

    /**
     * This method is to initialize listeners
     */
    private void initListeners() {

        btnSelectEventDate.setOnClickListener(this);
        btnSelectEventTime.setOnClickListener(this);
        btnAddDate.setOnClickListener(this);
        btnGoToAddUsers.setOnClickListener(this);
    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        inputValidation = new InputValidation(getActivity());

        Calendar calendar = Calendar.getInstance();
        nYear = calendar.get(Calendar.YEAR);
        nMonth = calendar.get(Calendar.MONTH);
        nDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        nHourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        nMinute = calendar.get(Calendar.MINUTE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSelectEventDate:
                showDatePickerDialog();
                break;
            case R.id.btnSelectEventTime:
                showTimePickerDialog();
                break;
            case R.id.btnAddDate:
                if (isInputsValid()) {
                    addNewDate();
                }
                break;
            case R.id.btnGoToAddUsers:
                if (!lstDates.isEmpty() && (mListener != null)){
                    mListener.onGoToAddUserClick(lstDates);
                } else {
                    tvSelectedEventDate.setError("Must to add a date");
                }
                break;
        }
    }

    private void showDatePickerDialog(){
        Calendar now = Calendar.getInstance();

        int yy = now.get(Calendar.YEAR);
        int mm = now.get(Calendar.MONTH);
        int dd = now.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd1 = new DatePickerDialog(getActivity(), this, yy, mm, dd);
        dpd1.getDatePicker().setMinDate(System.currentTimeMillis());
        dpd1.setTitle("Select the date");
        dpd1.show();
    }

    private void showTimePickerDialog(){
        Calendar now = Calendar.getInstance();

        int hh = now.get(Calendar.HOUR_OF_DAY);
        int mm = now.get(Calendar.MINUTE);

        TimePickerDialog tpd = new TimePickerDialog(getActivity(), this, hh, mm, true);
        tpd.setTitle("Select the time");
        tpd.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        nYear = year;
        nMonth = month + 1;
        nDayOfMonth = dayOfMonth;

        tvSelectedEventDate.setText(nDayOfMonth+"/"+nMonth+"/"+nYear);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        nHourOfDay = hourOfDay;
        nMinute = minute;

        tvSelectedEventTime.setText(nHourOfDay+":"+nMinute);
    }

    /**
     * This method is to validate the input text fields and post data to SQLite
     */
    private boolean isInputsValid() {

        Date dtValidDate = inputValidation.getValidateDate
                (tvSelectedEventDate, "Must to Enter a date in format: " + dateFormat, dateFormat);

        if (dtValidDate == null) {
            return false;
        } else {
            Date dtValidTime = inputValidation.getValidateDate
                    (tvSelectedEventTime, "Must to Enter a time in format: " + timeFormat, timeFormat);

            if (dtValidTime == null){
                return false;
            } else {
                try {
                    SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm");
                    String strDate = sdfDate.format(dtValidDate);
                    String strTime = sdfTime.format(dtValidTime);
                    strCurrAddDate = strDate + " " + strTime;
                } catch (Exception ex){
                    return false;
                }
            }
        }

        return true;
    }

    private void addNewDate(){
        if ((strCurrAddDate != null) && (!lstDates.contains(strCurrAddDate))){
            lstDates.add(strCurrAddDate);

            mAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getActivity(), "select new date", Toast.LENGTH_SHORT).show();
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

    public interface AddDatesListener {
        void onGoToAddUserClick(List<String> dates);
    }
}

