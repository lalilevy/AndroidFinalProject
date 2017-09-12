package com.shir.androidfinalproject.Models;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by shir on 29-Aug-17.
 */

public class EventDate extends Votes  {
    public Date date;

    public EventDate (Date date){
        this.date = date;
    }

    public String displayDate() {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        String str = dateFormat.format(this.date);
        return str;
    }
}
