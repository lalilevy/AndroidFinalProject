package com.shir.androidfinalproject.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.shir.androidfinalproject.CallBacks.CreateUserCallback;
import com.shir.androidfinalproject.CallBacks.SigninCallback;
import com.shir.androidfinalproject.Fragments.SigninFragment;
import com.shir.androidfinalproject.Models.User;
import com.shir.androidfinalproject.R;

import com.shir.androidfinalproject.Fragments.RegistrationFragment;
import com.shir.androidfinalproject.Data.model;

import java.util.List;

public class LoginActivity extends AppCompatActivity implements
        SigninFragment.SignInListener,
        RegistrationFragment.RegistrationListener{

    private static final String TAG = "LoginActivity";
    private NestedScrollView loginNestedScroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Event Scheduler Login");

        loginNestedScroll = (NestedScrollView)findViewById(R.id.nested_scroll_login);

        User user = model.instance.getCurrentUser();

        // check if user is login
        if (user != null){
            if (model.instance.findUser(user.uid)){
                goToMainActivity(user.uid, user.username);
            } else{
                goToSignInFragment();
            }
        } else {
            List<User> lstAllUsersInPhone = model.instance.getAllUsersInPhone();
            if (lstAllUsersInPhone.size() == 0){
                goToRegistrationFragment();
            } else {
                goToSignInFragment();
            }
        }
    }

    private void goToSignInFragment() {
        getFragmentManager()
                .beginTransaction()
                .add(R.id.nested_scroll_login, SigninFragment.newInstance(), SigninFragment.TAG)
                .commit();
    }

    private void goToRegistrationFragment() {
        getFragmentManager()
                .beginTransaction()
                .add(R.id.nested_scroll_login, RegistrationFragment.newInstance(), RegistrationFragment.TAG)
                .commit();
    }

    private void goToMainActivity(String strUserID, String strUserDisplayName) {

        // Launching new Activity on selecting single List Item
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        Bundle bundle = new Bundle();
        i.putExtras(bundle);
        i.putExtra(MainActivity.USER_ID, strUserID);
        i.putExtra(MainActivity.USER_NAME, strUserDisplayName);

        startActivity(i);
        finish();
    }

    private void replaceToRegistrationFragment() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.nested_scroll_login, RegistrationFragment.newInstance(), RegistrationFragment.TAG)
                .addToBackStack(RegistrationFragment.TAG)
                .commit();
    }

    private void replaceToSignInFragment() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.nested_scroll_login, SigninFragment.newInstance(),  SigninFragment.TAG)
                .addToBackStack(SigninFragment.TAG)
                .commit();
    }

    @Override
    public void onLinkToSignin() {
        replaceToSignInFragment();
    }

    @Override
    public void onLinkToRegistretion() {
        replaceToRegistrationFragment();
    }

    @Override
    public void onUserSignIn(String userEmail, String password) {
        showProgressDialog();

        model.instance.signIn(userEmail, password, new SigninCallback() {
            @Override
            public void onSuccess(String userID, String userName) {
                hideProgressDialog();
                toastMessage("SignIn Successful");
                goToMainActivity(userID, userName);
            }

            @Override
            public void onFailed() {
                hideProgressDialog();
                toastMessage("SignIn failed");
            }
        });
    }

    @Override
    public void onUserRegisterd(String userName, String email, String password)  {
        showProgressDialog();

        model.instance.createUser(userName, email, password, new CreateUserCallback() {
            @Override
            public void onSuccess(String userID, String userName) {
                hideProgressDialog();
                toastMessage(getString(R.string.success_message));
                goToMainActivity(userID, userName );
            }

            @Override
            public void onFailed(String message) {
                hideProgressDialog();
                toastMessage(message);
            }
        });
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading...");
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
