package com.shir.androidfinalproject.Models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by shir on 23-Jul-17.
 */

@IgnoreExtraProperties
public class User {

    public String uid;
    public String username;
    public String email;
    public String password = "";

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String userID, String username, String email) {
        this.uid = userID;
        this.username = username;
        this.email = email;
    }

    public User(String userID, String username, String email, String password) {
        this.uid = userID;
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
