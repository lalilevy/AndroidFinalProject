package com.shir.androidfinalproject.CallBacks;

/**
 * Created by shir on 17-Aug-17.
 */

public interface SigninCallback {
    void onSuccess(String userID, String userName);
    void onFailed();
}
