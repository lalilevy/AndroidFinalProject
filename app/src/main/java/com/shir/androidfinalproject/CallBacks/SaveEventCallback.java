package com.shir.androidfinalproject.CallBacks;

public interface SaveEventCallback {
    void onCompleted(String eventKey);
    void onCanceled(Exception e);
}
