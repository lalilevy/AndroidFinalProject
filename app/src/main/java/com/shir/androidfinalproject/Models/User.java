package com.shir.androidfinalproject.Models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by shir on 23-Jul-17.
 */

// [START blog_user_class]
@IgnoreExtraProperties
public class User {

    public String uid;
    public String username;
    public String email;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

//    public boolean isEventInList(String strEventID)
//    {
//        if (this.eventsList.isEmpty()) {
//            return false;
//        }
//        else {
//            return this.eventsList.stream().anyMatch(e -> e.uid == strEventID);
//        }
//    }

//    public int getIndexOfEvent(String strEventID)
//    {
//        return this.eventsList.indexOf(
//                this.eventsList.stream().filter(x-> x.uid == strEventID).findFirst());
//
//    }
}
