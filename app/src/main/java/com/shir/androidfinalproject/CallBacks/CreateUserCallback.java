package com.shir.androidfinalproject.CallBacks;

public interface CreateUserCallback {
    void onSuccess(String userID, String userName);
    void onFailed(String message);
}
