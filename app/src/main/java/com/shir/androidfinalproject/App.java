package com.colman.androidfinalproject;

import android.app.Application;
import android.content.Context;

/**
 * Created by shir on 01-Sep-17.
 */

public class App extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getMyContext(){
        return context;
    }
}
