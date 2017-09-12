package com.shir.androidfinalproject.Fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shir.androidfinalproject.Models.User;
import com.shir.androidfinalproject.R;
import com.shir.androidfinalproject.data.InputValidation;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegistrationFragment.RegistrationListener} interface
 * to handle interaction events.
 * Use the {@link RegistrationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrationFragment extends Fragment implements View.OnClickListener {

    //region DataMembers

    private TextInputLayout txtRegisterName;
    private TextInputLayout txtRegisterEmail;
    private TextInputLayout txtRegisterPassword;
    private TextInputLayout txtRegisterConfirmPassword;

    private TextInputEditText editRegisterName;
    private TextInputEditText editRegisterEmail;
    private TextInputEditText editRegisterPassword;
    private TextInputEditText editRegisterConfirmPassword;

    private AppCompatButton btnRegister;
    private AppCompatTextView linkToSignin;

    private InputValidation inputValidation;

    private User user;
    private RegistrationListener mListener;

    public static final String TAG = "RegistrationFragment";

    //endregion

    public RegistrationFragment() {
        // Required empty public constructor
    }

    public static RegistrationFragment newInstance() {
        RegistrationFragment fragment = new RegistrationFragment();
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        //((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        initViews(view);
        initListeners();
        initObjects();

        return view;
    }

    /**
     * This method is to initialize views
     */
    private void initViews(View view) {

        txtRegisterName = (TextInputLayout)view.findViewById(R.id.txtRegisterName);
        txtRegisterEmail = (TextInputLayout)view.findViewById(R.id.txtRegisterEmail);
        txtRegisterPassword = (TextInputLayout)view.findViewById(R.id.txtRegisterPassword);
        txtRegisterConfirmPassword = (TextInputLayout)view.findViewById(R.id.txtRegisterConfirmPassword);

        editRegisterName = (TextInputEditText)view.findViewById(R.id.editRegisterName);
        editRegisterEmail = (TextInputEditText)view.findViewById(R.id.editRegisterEmail);
        editRegisterPassword = (TextInputEditText)view.findViewById(R.id.editRegisterPassword);
        editRegisterConfirmPassword = (TextInputEditText)view.findViewById(R.id.editRegisterConfirmPassword);

        btnRegister = (AppCompatButton)view.findViewById(R.id.btnRegister);
        linkToSignin = (AppCompatTextView)view.findViewById(R.id.linkToSignin);
    }

    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        btnRegister.setOnClickListener(this);
        linkToSignin.setOnClickListener(this);
    }

    /**
     * This method is to initialize objects to be used
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initObjects() {
        inputValidation = new InputValidation(getContext());
        user = new User();
    }

    /**
     * This implemented method is to listen the click on view
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnRegister:
                if (isInputsValid() && (mListener != null)){
                    mListener.onUserRegisterd(
                            editRegisterName.getText().toString(),
                            editRegisterEmail.getText().toString(),
                            editRegisterPassword.getText().toString());
                }
                break;

            case R.id.linkToSignin:
                if (mListener != null)
                    mListener.onLinkToSignin();
                break;
        }
    }

    /**
     * This method is to validate the input text fields and post data to SQLite
     */
    private boolean isInputsValid() {
        if (!inputValidation.isInputEditTextFilled
                (editRegisterName, txtRegisterName, getString(R.string.error_message_name))) {
            return false;
        }
        if (!inputValidation.isInputEditTextEmail
                (editRegisterEmail, txtRegisterEmail, getString(R.string.error_message_email))) {
            return false;
        }
        if (!inputValidation.isInputEditTextFilled
                (editRegisterPassword, txtRegisterPassword, getString(R.string.error_message_password))) {
            return false;
        }
        if (!inputValidation.isInputEditTextMatches
                (editRegisterPassword,
                editRegisterConfirmPassword,
                txtRegisterConfirmPassword,
                getString(R.string.error_password_match))) {
            return false;
        }

        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RegistrationListener) {
            mListener = (RegistrationListener) context;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface RegistrationListener {
        void onUserRegisterd(String userName, String email, String password);
        void onLinkToSignin();
    }
}
