package com.shir.androidfinalproject.data;

import android.content.Context;
import android.content.SharedPreferences;

//import com.facebook.Profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shir.androidfinalproject.Models.User;

/**
 * Created by shir on 01-Sep-17.
 */

public class DataManager {

    public static final String CHECK = "CHECK";
    public static final String IS_CONNECTED = "IS_CONNECTED";
    public static final String USER_ID = "USER_ID";
    public static final String LOGIN_OPTION = "LOGIN_OPTION";
    private static final String USERS_REF = "users";

    private static DataManager _Instance;
    private Context mContext;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase mRootRef;
    private DatabaseReference mUsersRef;
    //private FirebaseUser firebaseUser;

    public static DataManager getInstance(Context context) {
        if (_Instance == null) {
            _Instance = new DataManager(context);
        }
        return _Instance;
    }

    private DataManager(Context context) {
        mContext = context;
    }

    public FirebaseAuth getAuth(){
        if (firebaseAuth == null)
            firebaseAuth = FirebaseAuth.getInstance();

        return firebaseAuth;
    }

    public DatabaseReference getUsersRef(){
        if (mUsersRef == null)
            mUsersRef = this.getDBReference(USERS_REF);

        return mUsersRef;
    }

    private SharedPreferences getSharedPreferences() {
        return mContext.getSharedPreferences(CHECK, Context.MODE_PRIVATE);
    }

    private DatabaseReference getDBReference(String childName){
        if (mRootRef == null)
            mRootRef = FirebaseDatabase.getInstance();

        return mRootRef.getReference(childName);
    }

    public void destroy() {
        mContext = null;
        _Instance = null;
        mUsersRef = null;
        mRootRef = null;
        firebaseAuth = null;
    }

    public String getUserId() {
        return getSharedPreferences().getString(USER_ID, null);
    }

    public void setLogin(boolean bLogin, String userID) {
        getSharedPreferences().edit().putBoolean(IS_CONNECTED, bLogin).apply();

        if (bLogin){
            getSharedPreferences().edit().putString(USER_ID, userID).apply();
            //firebaseUser = user;
        } else {
            firebaseAuth.signOut();
        }
    }

    public User getUserByID(String userId){
        final User[] user = new User[1];

        DatabaseReference  drUserRef = getUsersRef().child(userId);

        drUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(userId)) {
                    user[0] = dataSnapshot.child(userId).getValue(User.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return user[0];
    }

    public boolean isLoggedIn() {
        return getSharedPreferences().getBoolean(IS_CONNECTED, false);
    }
}
