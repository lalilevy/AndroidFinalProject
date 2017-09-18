package com.shir.androidfinalproject.CallBacks;

import com.shir.androidfinalproject.Models.Event;

import java.util.List;

/**
 * Created by shir on 15-Aug-17.
 */

public interface GetAllEventsCallback {
    void onCompleted(List<Event> events);
    void onCanceled();
}
