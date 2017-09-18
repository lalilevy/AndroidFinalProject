package com.shir.androidfinalproject.Fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.shir.androidfinalproject.R;
import com.shir.androidfinalproject.Data.InputValidation;

import java.util.Calendar;
import java.util.Date;

public class AddEventDetailsFragment extends Fragment
        implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener {
    public static final String TAG = "AddEventDetailsFragment";
    private final String dateFormat = "dd/MM/yyyy";

    private AddEventDetailsListener mListener;

    EditText editEventTitle;
    EditText editEventDescription;
    EditText editEventDuration;
    EditText tvEventLastUpdateDate;
    FloatingActionButton btnGoToAddLocations;
    Button btnEventLastUpdateDate;

    InputValidation inputValidation;
    Date dtLastUpdate;
    int nDuration;
    int nYear,  nMonth, nDay;

    public AddEventDetailsFragment() {
    }

    public static AddEventDetailsFragment newInstance() {
        return new AddEventDetailsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_event_details, container, false);

        initViews(view);
        initListeners();
        initObjects();

        return view;
    }

    private void initViews(View view) {

        editEventTitle = (EditText)view.findViewById(R.id.editEventTitle);
        editEventTitle.setFocusable(true);
        editEventDescription = (EditText)view.findViewById(R.id.editEventDescription);
        editEventDuration = (EditText)view.findViewById(R.id.editEventDuration);
        tvEventLastUpdateDate = (EditText)view.findViewById(R.id.tvEventLastUpdateDate);
        btnGoToAddLocations = (FloatingActionButton)view.findViewById(R.id.btnGoToAddLocations);
        btnEventLastUpdateDate = (Button)view.findViewById(R.id.btnEventLastUpdateDate);
    }

    private void initListeners() {
        btnGoToAddLocations.setOnClickListener(this);
        btnEventLastUpdateDate.setOnClickListener(this);
    }

    private void initObjects() {
        inputValidation = new InputValidation(getActivity());
        Calendar calendar = Calendar.getInstance();
        nYear = calendar.get(Calendar.YEAR);
        nMonth = calendar.get(Calendar.MONTH);
        nDay = calendar.get(Calendar.DAY_OF_MONTH);
    }

    //@RequiresApi(api = Build.VERSION_CODES.M)
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
            case R.id.btnEventLastUpdateDate:
                showDatePickerDialog();
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

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        nYear = year;
        nMonth = month +1;
        nDay = dayOfMonth;

        tvEventLastUpdateDate.setText(nDay+"/"+nMonth+"/"+nYear);
    }

    /**
     * This method is to validate the input text fields and post data to SQLite
     */
    private boolean isInputsValid() {

        if (!inputValidation.isEditTextFilled(editEventTitle, "Must to Enter a title")) {
            return false;
        }
        if (!inputValidation.isEditTextFilled(editEventDescription, "Must to Enter a description")) {
            return false;
        }

        Date dtValid = inputValidation.getValidateDate
                (tvEventLastUpdateDate, "Must to Enter a date in format: " + dateFormat, dateFormat);

        if (dtValid == null) {
            return false;
        } else {
            dtLastUpdate = dtValid;
        }

        int nValid = inputValidation.getValidateInt(editEventDuration, "Must to Enter a Duration");

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
