package com.shir.androidfinalproject.CallBacks;

import com.shir.androidfinalproject.Models.User;

import java.util.List;

public interface GetAllUsersCallback {
    void onCompleted(List<User> users);
    void onCanceled();
}
