package com.colman.androidfinalproject.Models;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.colman.androidfinalproject.Enums.Status;

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

    public String eventID;
    public String hostID;
    public String hostName;
    public String title;
    public String description;
    public Date lastUpdateDate;
    public int duration;
    public Date createDate;
    public List<String> datesList = new ArrayList<>();
    public List<String> locationsList = new ArrayList<>();
    public List<String> usersList = new ArrayList<>();

    public Map<String, String> usersLocations = new HashMap<>();
    public Map<String, String> usersDates = new HashMap<>();
    public Map<String, String> usersStatus = new HashMap<>();

    public Event() {
        // Default constructor required for calls to DataSnapshot.getValue(Event.class)
    }

    public Event(String hostID, String hostName, String title, String description, Date lastUpdateDate, int duration,
                 List<String> lstLocations, List<String> lstDates, List<String> lstUsers) {
        this.hostID = hostID;
        this.hostName = hostName;
        this.description = description;
        this.title = title;
        this.lastUpdateDate = lastUpdateDate;
        this.duration = duration;
        this.datesList = lstDates;
        this.locationsList = lstLocations;
        this.usersList = lstUsers;
        this.createDate = new Date();
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("hostID", hostID);
        result.put("hostName", hostName);
        result.put("title", title);
        result.put("description", description);
        result.put("lastUpdateDate", lastUpdateDate);
        result.put("duration", duration);
        result.put("createDate", createDate);
        result.put("datesList", datesList);
        result.put("locationsList", locationsList);
        result.put("usersList", usersList);
        result.put("usersLocations", usersLocations);
        result.put("usersDates", usersDates);
        result.put("usersStatus", usersStatus);

        return result;
    }

    public String getChosenLocation(){
        String strChosendLocation = "No location has chosen yet";
        if ((usersLocations != null) && (!usersLocations.isEmpty())){
            strChosendLocation = getMaxValue(usersLocations).getKey();
        }

        return strChosendLocation;
    }

    public String getChosenDate(){
        String strChosenDate = "No date has chosen yet";
        if ((usersDates != null) && (!usersDates.isEmpty())){
            strChosenDate = getMaxValue(usersDates).getKey();
        }

        return strChosenDate;
    }

    public int getGoingUsers(){
        int nGoingUsers = 0;
        if ((usersStatus != null) && (!usersStatus.isEmpty())){

            for (String strStatus: usersStatus.values()) {
                if(strStatus.equals(Status.Going.getName()) ||
                   strStatus.equals(Status.Host.getName())){
                    nGoingUsers++;
                }
            }
        }

        return nGoingUsers;
    }

    private Map.Entry<String, Integer> getMaxValue(Map<String, String> lst){
        HashMap<String, Integer> map = new HashMap<>();

        for (String strValue: lst.values()) {
            if(map.containsKey(strValue)){
                map.put(strValue, map.get(strValue) + 1);
            } else {
                map.put(strValue,1);
            }
        }

        Map.Entry<String, Integer> maxEntry = null;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0){
                maxEntry = entry;
            }
        }

        return   maxEntry;
    }

    public int getSelectedDateIndex(String userID){
        int nSelectedDateIndex = 0;

        if (usersDates.containsKey(userID)){
            nSelectedDateIndex = datesList.indexOf(usersDates.get(userID));
        }

        return nSelectedDateIndex;
    }

    public int getSelectedLocationIndex(String userID){
        int nSelectedLocationIndex = 0;

        if (usersLocations.containsKey(userID)){
            nSelectedLocationIndex = locationsList.indexOf(usersLocations.get(userID));
        }

        return nSelectedLocationIndex;
    }

    public boolean setSelectedDate(String currUserID, String selectedItem) {
        boolean isSelectedDateUpdated = false;

        if ((selectedItem != null) && (!selectedItem.isEmpty())){

            if (usersDates.containsKey(currUserID)){

                if (!usersDates.get(currUserID).equals(selectedItem)) {

                    usersDates.remove(currUserID);
                    usersDates.put(currUserID, selectedItem);
                    isSelectedDateUpdated = true;
                }
            } else {

                usersDates.put(currUserID, selectedItem);
                isSelectedDateUpdated = true;
            }
        } else if (usersDates.containsKey(currUserID)){

            usersDates.remove(currUserID);
            isSelectedDateUpdated = true;
        }

        return isSelectedDateUpdated;
    }

    public boolean setSelectedLocation(String currUserID, String selectedItem) {
        boolean isSelectedLocationUpdated = false;

        if ((selectedItem != null) && (!selectedItem.isEmpty())){

            if (usersLocations.containsKey(currUserID)){

                if (!usersLocations.get(currUserID).equals(selectedItem)) {

                    usersLocations.remove(currUserID);
                    usersLocations.put(currUserID, selectedItem);
                    isSelectedLocationUpdated = true;
                }
            } else {

                usersLocations.put(currUserID, selectedItem);
                isSelectedLocationUpdated = true;
            }
        } else if (usersLocations.containsKey(currUserID)){

            usersLocations.remove(currUserID);
            isSelectedLocationUpdated = true;
        }

        return isSelectedLocationUpdated;
    }

    public boolean setUserStatus(String currUserID, @NonNull Status userStatus) {
        boolean isUserStatusUpdated = false;

        if (usersStatus.containsKey(currUserID) &&
            !usersStatus.get(currUserID).equals(userStatus)){
            if (Status.Going.equals( userStatus)){
                usersStatus.remove(currUserID);
                usersStatus.put(currUserID, userStatus.getName());

                isUserStatusUpdated = true;
            } else if (Status.Ignore.equals(userStatus)){
                usersStatus.remove(currUserID);
                usersStatus.put(currUserID, userStatus.getName());

                setSelectedDate(currUserID, null);
                setSelectedLocation(currUserID, null);
                isUserStatusUpdated = true;
            } else if (Status.None.equals(userStatus)){
                usersStatus.remove(currUserID);
                setSelectedDate(currUserID, null);
                setSelectedLocation(currUserID, null);
                isUserStatusUpdated = true;
            }
        }

        return isUserStatusUpdated;
    }
}