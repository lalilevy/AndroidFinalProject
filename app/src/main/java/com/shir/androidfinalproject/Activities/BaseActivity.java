package com.colman.androidfinalproject.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.colman.androidfinalproject.R;
import com.colman.androidfinalproject.Data.model;

public class BaseActivity extends AppCompatActivity {

    public static final String USER_ID = "USER_ID";
    public static final String USER_NAME = "USER_NAME";

    protected String userName;
    protected String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userName = getIntent().getStringExtra(USER_NAME);
        userID = getIntent().getStringExtra(USER_ID);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId){
            case R.id.main_logout:
                logOut();
                break;
            case R.id.menu_events:
                goToMainActivity();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private void logOut() {
        model.instance.signOut();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        intent.putExtra(MainActivity.USER_NAME, userName);
        intent.putExtra(MainActivity.USER_ID, userID);

        startActivity(intent);
        finish();

    }
}
