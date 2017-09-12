package com.shir.androidfinalproject.Activities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.shir.androidfinalproject.Fragments.SigninFragment;
import com.shir.androidfinalproject.Models.User;
import com.shir.androidfinalproject.R;
import com.shir.androidfinalproject.data.DataManager;

import com.shir.androidfinalproject.Fragments.RegistrationFragment;

public class LoginActivity extends BaseActivity implements
        SigninFragment.SignInListener,
        RegistrationFragment.RegistrationListener{

    private static final String TAG = "LoginActivity";
    private NestedScrollView loginNestedScroll;
    // HASHKEY cSoxPH00SgxvFfCVOExPXUv23QE=

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Event Scheduler Login");

        loginNestedScroll = (NestedScrollView)findViewById(R.id.nested_scroll_login);

        String strUserID = getDataManager().getUserId();

        if (strUserID == null){
            goToRegistrationFragment();
        }
        else  if (getDataManager().isLoggedIn()) {
            FirebaseUser user = getDataManager().getAuth().getCurrentUser();

            if (user != null){
                goToMainActivity(user.getUid(), user.getDisplayName());
            }
            else {
                goToSignInFragment();
            }
        }
        else {
            goToSignInFragment();
        }
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

        getDataManager().setLogin(true, strUserID);

        // Launching new Activity on selecting single List Item
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        Bundle bundle = new Bundle();
        i.putExtras(bundle);
        i.putExtra(MainActivity.USER_ID, strUserID);
        i.putExtra(MainActivity.USER_NAME, strUserDisplayName);

        startActivity(i);
        finish();
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

        getDataManager().getAuth().signInWithEmailAndPassword(userEmail, password)
                .addOnCompleteListener(LoginActivity.this, task -> {
                    hideProgressDialog();

                    if (!task.isSuccessful()){
                        toastMessage("SignIn failed: " + task.getException());
                    } else {
                        // Snack Bar to show success message that record is wrong
                        snackbarMake("SignIn Successful");

                        FirebaseUser user = task.getResult().getUser();
                        goToMainActivity(user.getUid(), user.getDisplayName());
                    }
                });
    }

    @Override
    public void onUserRegisterd(String userName, String email, String password)  {
        showProgressDialog();

        getDataManager().getAuth().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, task ->{
                    hideProgressDialog();

                    if (!task.isSuccessful()){
                       // Snack Bar to show Registretion error message
                        snackbarMake("Registretion failed: " + task.getException());
                   } else {
                        snackbarMake(getString(R.string.success_message));

                        FirebaseUser firebaseUser = task.getResult().getUser();
                        User user = new User (userName, email);
                        updateUserProfile(firebaseUser, user);
                        goToMainActivity(firebaseUser.getUid(),userName );
                   }
                });
    }

    private void updateUserProfile(FirebaseUser firebaseUser, User user){
         UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(user.username)
                //.setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                .build();

        firebaseUser.updateProfile(profileUpdates)
                .addOnCompleteListener(LoginActivity.this, task -> {

                    if (!task.isSuccessful()){
                        toastMessage("updated failed. " + task.getException());
                    } else {
                        toastMessage("User profile updated.");
                    }
                });

        getDataManager().getUsersRef().child(firebaseUser.getUid()).setValue(user);
    }

    private DataManager getDataManager(){
        return DataManager.getInstance(getApplicationContext());
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void snackbarMake(String message){
        Snackbar.make(loginNestedScroll, message, Snackbar.LENGTH_SHORT).show();
    }

   //    private void logInWithFacebook() {
//        loginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//
//                accessToken = loginResult.getAccessToken();
//                GraphRequest req = GraphRequest.newMeRequest(loginResult.getAccessToken(),
//                        new GraphRequest.GraphJSONObjectCallback(){
//                    @Override
//                    public void onCompleted(JSONObject object, GraphResponse response){
//                        DataManager.getInstance(getApplicationContext()).setLogin(true);
//                        saveUserFacebookInfo(object);
//                        goToMainActivity();
//                    }
//                });
//
//                Bundle bundle = new Bundle();
//                bundle.putString("fields", "id, first_name, last_name, email");
//                req.setParameters(bundle);
//                req.executeAsync();
//
//                tvText.setText("Login Success");
//            }
//
//            @Override
//            public void onCancel() {
//                tvText.setText("Login canceled");
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//                tvText.setText("network error " +  error.toString());
//            }
//        });
//    }

//    private void saveUserFacebookInfo(JSONObject object) {
//
//        try {
////            String strUserDetails = response.getRawResponse();
////            JSONObject jsonObject = new JSONObject(strUserDetails);
//
//            String strFacebbokID = object.getString("id");
//            String strFirstName = object.getString("first_name");
//            String strLastName = object.getString("last_name");
//            String strEmail = object.getString("email");
//            // tyep can be normal, large
//            String strImage = "http://graph.facebook.com/" + strFacebbokID + "/picture?type=large";
//            Glide.with(getApplicationContext()).load(strImage).into(image);
//            image.buildDrawingCache();
//            Bitmap bmpImage = image.getDrawingCache();
//
////            User connectedUser = new User(strFacebbokID, strFirstName, strLastName, strEmail, bmpImage, strImage);
////            common.Instance().setCurrentUser(connectedUser);
////            //  mCondiotionRef.setValue(common.Instance().getUserID());
////            DataManager.getInstance(this).setUserId(connectedUser.id);
////            mUsersRef.child(connectedUser.id).setValue(connectedUser);
//
//        } catch (JSONException e){
//            e.printStackTrace();
//        }
//    }
}
