package com.shir.androidfinalproject.Fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shir.androidfinalproject.R;
import com.shir.androidfinalproject.data.InputValidation;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SigninFragment.SignInListener} interface
 * to handle interaction events.
 * Use the {@link SigninFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SigninFragment extends Fragment implements View.OnClickListener {

    //region DataMembers

    private TextInputLayout txtLoginEmail;
    private TextInputLayout txtLoginPassword;
    private TextInputEditText editLoginEmail;
    private TextInputEditText editLoginPassword;

    private AppCompatButton btnSignin;
    private AppCompatTextView linkToRegister;

    private InputValidation inputValidation;
    private SignInListener mListener;

    public static final String TAG = "SigninFragment";

    //endregion

    public SigninFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SigninFragment.
     */
    public static SigninFragment newInstance() {
        SigninFragment fragment = new SigninFragment();
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin, container, false);
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

        txtLoginEmail = (TextInputLayout)view.findViewById(R.id.txtLoginEmail);
        txtLoginPassword = (TextInputLayout)view.findViewById(R.id.txtLoginPassword);

        editLoginEmail = (TextInputEditText)view.findViewById(R.id.editLoginEmail);
        editLoginPassword = (TextInputEditText)view.findViewById(R.id.editLoginPassword);

        btnSignin = (AppCompatButton)view.findViewById(R.id.btnSignin);
        linkToRegister = (AppCompatTextView)view.findViewById(R.id.linkToRegister);
    }

    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        btnSignin.setOnClickListener(this);
        linkToRegister.setOnClickListener(this);
    }

    /**
     * This method is to initialize objects to be used
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initObjects() {
        inputValidation = new InputValidation(getContext());
    }

    /**
     * This implemented method is to listen the click on view
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignin:
                if (isInputsValid() && (mListener != null)){
                    mListener.onUserSignIn
                            (editLoginEmail.getText().toString(), editLoginPassword.getText().toString());
                }
                break;
            case R.id.linkToRegister:
                if (mListener != null)
                    mListener.onLinkToRegistretion();
                break;
        }
    }

    /**
     * This method is to validate the input text fields and verify login credentials from SQLite
     */
    private boolean isInputsValid() {
        if (!inputValidation.isInputEditTextEmail
                (editLoginEmail, txtLoginEmail, getString(R.string.error_message_email))) {
            return false;
        }
        if (!inputValidation.isInputEditTextFilled
                (editLoginPassword, txtLoginPassword, getString(R.string.error_message_email))) {
            return false;
        }
        
        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SignInListener) {
            mListener = (SignInListener) context;
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

    public interface SignInListener {
        void onUserSignIn(String userEmail, String password);
        void onLinkToRegistretion();
    }
    
    //endregion
}
