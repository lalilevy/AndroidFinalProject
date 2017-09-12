package com.shir.androidfinalproject.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.shir.androidfinalproject.R;
import com.shir.androidfinalproject.data.InputValidation;

import java.util.Calendar;
import java.util.Date;

public class AddEventDetailsFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "AddEventDetailsFragment";
    private final String dateFormat = "dd/MM/yyyy";

    private AddEventDetailsListener mListener;

    TextInputLayout txtEventTitle;
    TextInputLayout txtEventDescription;
    TextInputLayout txtEventLastUpdateDate;
    TextInputLayout txtEventDuration;

    TextInputEditText editEventTitle;
    TextInputEditText editEventDescription;
    TextInputEditText editEventLastUpdateDate;
    TextInputEditText editEventDuration;

    FloatingActionButton btnGoToAddLocations;

    InputValidation inputValidation;
    Date dtLastUpdate;
    int nDuration;
    int nYear,  nMonth, nDay;

    public AddEventDetailsFragment() {
    }

    public static AddEventDetailsFragment newInstance() {
        return new AddEventDetailsFragment();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_event_details, container, false);

        initViews(view);
        initListeners();
        initObjects();

        return view;
    }

    /**
     * This method is to initialize views
     */
    private void initViews(View view) {

        txtEventTitle = (TextInputLayout)view.findViewById(R.id.txtEventTitle);
        txtEventDescription = (TextInputLayout)view.findViewById(R.id.txtEventDescription);
        txtEventLastUpdateDate = (TextInputLayout)view.findViewById(R.id.txtEventLastUpdateDate);
        txtEventDuration = (TextInputLayout)view.findViewById(R.id.txtEventDuration);

        editEventTitle = (TextInputEditText)view.findViewById(R.id.editEventTitle);
        editEventTitle.setFocusable(true);
        editEventDescription = (TextInputEditText)view.findViewById(R.id.editEventDescription);
        editEventLastUpdateDate = (TextInputEditText)view.findViewById(R.id.editEventLastUpdateDate);
        editEventLastUpdateDate.setText(Calendar.getInstance().getTime().toString());
        editEventDuration = (TextInputEditText)view.findViewById(R.id.editEventDuration);

        btnGoToAddLocations = (FloatingActionButton)view.findViewById(R.id.btnGoToAddLocations);
    }

    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        btnGoToAddLocations.setOnClickListener(this);
        //txtEventLastUpdateDate.setOnClickListener(this);
        //editEventLastUpdateDate.setOnClickListener(this);
    }

    /**
     * This method is to initialize objects to be used
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initObjects() {
        inputValidation = new InputValidation(getContext());
        Calendar calendar = Calendar.getInstance();
        nYear = calendar.get(Calendar.YEAR);
        nMonth = calendar.get(Calendar.MONTH);
        nDay = calendar.get(Calendar.DAY_OF_MONTH);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnGoToAddLocations:
                if (isInputsValid() && (mListener != null)){
                    mListener.onGoToAddLocationClick(
                            editEventTitle.getText().toString(),
                            editEventDescription.getText().toString(),
                            dtLastUpdate,
                            nDuration);
                }
                break;
            case R.id.editEventLastUpdateDate:

                showDialog(0);
                //showDialog(DATE_ID);
                //new DatePickerDialog(getContext(), mDateSetListener, nYear, nMonth, nDay);
                break;
            case R.id.txtEventLastUpdateDate:
                showDialog(0);
                //new DatePickerDialog(getContext(), mDateSetListener, nYear, nMonth, nDay);
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private Dialog showDialog(int id) {
        return new DatePickerDialog(getContext(), mDateSetListener, nYear, nMonth, nDay);
        //return null;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener(){
                public void onDateSet(DatePicker view, int year, int month, int day){
                    nYear = year;
                    nMonth = month;
                    nDay = day;

                    editEventLastUpdateDate.setText(nYear + "-" + (nMonth + 1) + "-" + nDay);
                }
            };

    /**
     * This method is to validate the input text fields and post data to SQLite
     */
    private boolean isInputsValid() {

        if (!inputValidation.isInputEditTextFilled
                (editEventTitle, txtEventTitle, "Must to Enter a title")) {
            return false;
        }
        if (!inputValidation.isInputEditTextFilled
                (editEventDescription, txtEventDescription, "Must to Enter a description")) {
            return false;
        }

        Date dtValid = inputValidation.getValidateDate
                (editEventLastUpdateDate, txtEventLastUpdateDate, "Must to Enter a date in format: " + dateFormat, dateFormat);

        if (dtValid == null) {
            return false;
        } else {
            dtLastUpdate = dtValid;
        }

        int nValid = inputValidation.getValidateInt
                (editEventDuration, txtEventDuration, "Must to Enter a Duration");

        if (nValid == -1) {
            return false;
        } else {
            nDuration = nValid;
        }

        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddEventDetailsListener) {
            mListener = (AddEventDetailsListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface AddEventDetailsListener {
        void onGoToAddLocationClick(String Title, String Description, Date LastUpdateDate, int Duration);
    }
}
