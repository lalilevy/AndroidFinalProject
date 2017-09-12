package com.shir.androidfinalproject.Models;

import com.shir.androidfinalproject.Enums.Status;

import java.util.Date;

/**
 * Created by shir on 23-Jul-17.
 */

public class UserEvent {
    public String eventID;
    public String userID;
    public Status status;
    public EventLocation selectedEventLocation;
    public Date selectedDateRange;

    public  UserEvent(String strEventID, String strUser, Status sStatus)
    {
        this.eventID = strEventID;
        this.userID = strUser;
        this.status = sStatus;
    }
}

