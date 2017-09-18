package com.colman.androidfinalproject.CallBacks;

import com.colman.androidfinalproject.Models.User;

import java.util.List;

public interface GetAllUsersCallback {
    void onCompleted(List<User> users);
    void onCanceled();
}
