package com.colman.androidfinalproject.CallBacks;

import com.colman.androidfinalproject.Models.Event;

public interface GetEventCallback {
    void onCompleted(Event event);
    void onCanceled();
}
