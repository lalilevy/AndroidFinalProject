package com.shir.androidfinalproject.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.shir.androidfinalproject.Models.Event;
import com.shir.androidfinalproject.Models.User;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by shir on 02-Aug-17.
 */

public class common {

    private static common _instance = null;
    private User currUser;

    public static common Instance() {
        if(_instance == null) {
            _instance = new common();
        }
        return _instance;
    }

    protected common() {
        //currUser = getFakeUser();
    }

//    public ArrayList<Event> getAllEvents() {
//        return currUser.eventsList;
//    }

//    public boolean AddEvent(Event newEvent) {
//        //if (lstAllStudents.stream().filter(x -> x.ID == st.ID) == null)
//        boolean bEventInList = currUser.isEventInList(newEvent.uid);
//
//        if (!bEventInList)
//        {
//            currUser.eventsList.add(newEvent);
//            //newEvent.setIndex(lstAllStudents.indexOf(newEvent));
//        }
//
//        return !bEventInList;
//    }

//    public void UpdateEvent(Event newEvent) {
//        boolean bEventInList = currUser.isEventInList(newEvent.uid);
//
//        if (bEventInList) {
//            int nIndex = currUser.getIndexOfEvent(newEvent.uid);
//
//            currUser.eventsList.set(nIndex, newEvent);
//            //newEvent.setIndex(lstAllStudents.indexOf(newEvent));
//        }
//    }

//    public void DeleteEvent(Event event) {
//        boolean bEventInList = currUser.isEventInList(event.uid);
//
//        if (bEventInList) {
//            int nIndex = currUser.getIndexOfEvent(event.uid);
//
//            currUser.eventsList.remove(nIndex);
//            //newEvent.setIndex(lstAllStudents.indexOf(newEvent));
//        }
//    }

    public String getUserID()
    {
        return currUser.uid;
    }

    public void setCurrentUser(User newUser){
        this.currUser = newUser;
    }

    private User getFakeUser() {
        String strFacebbokID = "1041745564";
        String strName = "shir bruchim1";
        String strEmail = "shirbruchim1@walla.com";

        // tyep can be normal, large
        String src = "http://graph.facebook.com/1041745564/picture?type=large";
        Bitmap bmpImage = Bitmap.createBitmap (0,0, Bitmap.Config.ARGB_8888);
        try{
            URL url = new URL(src);
            URLConnection connection =  url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            bmpImage = BitmapFactory.decodeStream(input);
        }catch (IOException e){
            System.out.println("IOException ::" + e.toString());
        }

        //User fakeUser = new User(strFacebbokID, strFirstName, strLastName, strEmail, bmpImage, src);
        User fakeUser = new User(strName, strEmail);

        return fakeUser;
    }
//    public static class Instance{
//
//        private static User currUser;
//
//        private common()
//        {
//            currUser = new User();
//        }
//
//        public static ArrayList<Event> getAllEvents() {
//            if (currUser != null)
//            {
//                return currUser.eventsList;
//            }
//
//            return null;
//        }
//
//        public static boolean AddEvent(Event newEvent) {
//            //if (lstAllStudents.stream().filter(x -> x.ID == st.ID) == null)
//            boolean bEventInList = currUser.isEventInList(newEvent.id);
//
//            if (currUser != null)
//            {
//                if (!bEventInList)
//                {
//                    currUser.eventsList.add(newEvent);
//                    //newEvent.setIndex(lstAllStudents.indexOf(newEvent));
//                }
//            }
//
//            return !bEventInList;
//        }
//
//        public static void UpdateEvent(Event newEvent) {
//            if (currUser != null) {
//
//                boolean bEventInList = currUser.isEventInList(newEvent.id);
//
//                if (bEventInList) {
//                    int nIndex = currUser.getIndexOfEvent(newEvent.id);
//
//                    currUser.eventsList.set(nIndex, newEvent);
//                    //newEvent.setIndex(lstAllStudents.indexOf(newEvent));
//                }
//            }
//        }
//
//        public static void DeleteEvent(Event event) {
//            if (currUser != null) {
//
//                boolean bEventInList = currUser.isEventInList(event.id);
//
//                if (bEventInList) {
//                    int nIndex = currUser.getIndexOfEvent(event.id);
//
//                    currUser.eventsList.remove(nIndex);
//                    //newEvent.setIndex(lstAllStudents.indexOf(newEvent));
//                }
//            }
//        }
//
//        public String getUserName()
//        {
//            if (currUser !=null)
//                return currUser.userName;
//            else
//                return "";
//
//        }
//    }
}
