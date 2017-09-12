package com.shir.androidfinalproject.Models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.shir.androidfinalproject.Enums.Status;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shir on 22-Jul-17.
 */
@IgnoreExtraProperties
public class Event {

    public String uid;
    public String hostName;
    public String title;
    public String description;
    public Date lastUpdateDate;
    public int duration;
    public List<Date> datesList = new ArrayList<>();
    public List<String> locationsList = new ArrayList<>();
    public List<String> usersList = new ArrayList<>();

    public String selectedLocation = "";
    public Map<String, String> usersLocations = new HashMap<>();
    public Date selectedDate = new Date(Long.MIN_VALUE);
    public Map<String, Date> usersDates = new HashMap<>();
    public int goingCount = 0;
    public Map<String, Status> usersStatus = new HashMap<>();

    public Event() {
        // Default constructor required for calls to DataSnapshot.getValue(Event.class)
    }

    public Event(String uid, String hostName, String title, String description, Date lastUpdateDate, int duration,
                 ArrayList<Date> lstDates, ArrayList<String> lstLocations, ArrayList<String> lstUsers) {
        this.uid = uid;
        this.hostName = hostName;
        this.description = description;
        this.title = title;
        this.lastUpdateDate = lastUpdateDate;
        this.duration = duration;
        this.datesList = lstDates;
        this.locationsList = lstLocations;
        this.usersList = lstUsers;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("hostName", hostName);
        result.put("title", title);
        result.put("description", description);
        result.put("lastUpdateDate", lastUpdateDate);
        result.put("description", description);
        result.put("duration", duration);
        result.put("datesList", datesList);
        result.put("locationsList", locationsList);
        result.put("usersList", usersList);
        result.put("selectedLocation", selectedLocation);
        result.put("usersLocations", usersLocations);
        result.put("selectedDate", selectedDate);
        result.put("usersDates", usersDates);
        result.put("goingCount", goingCount);
        result.put("usersStatus", usersStatus);

        return result;
    }
}