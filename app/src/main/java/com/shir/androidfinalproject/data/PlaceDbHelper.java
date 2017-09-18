package com.shir.androidfinalproject.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PlaceDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "location.db";
    public static final String TABLE_NAME = "places";

    public static final String PLACE_ID = "_id";
    public static final String COLUMN_PLACE_ID = "placeID";

    // Database creation sql statement
    private final String CREATE_PLACES_TABLE =
            "CREATE TABLE " + TABLE_NAME + " ("
                + PLACE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_PLACE_ID + " TEXT NOT NULL, "
                + "UNIQUE (" + COLUMN_PLACE_ID + ") ON CONFLICT REPLACE"
            + "); ";

    // Constructor
    public PlaceDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create a table to hold the places data
        sqLiteDatabase.execSQL(CREATE_PLACES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}

