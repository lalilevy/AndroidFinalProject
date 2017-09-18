package com.shir.androidfinalproject.CallBacks;

import com.shir.androidfinalproject.Models.Event;

public interface GetEventCallback {
    void onCompleted(Event event);
    void onCanceled();
}
