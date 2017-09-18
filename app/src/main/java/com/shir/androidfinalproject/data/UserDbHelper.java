package com.colman.androidfinalproject.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.colman.androidfinalproject.Models.User;

import java.util.ArrayList;
import java.util.List;

public class UserDbHelper extends SQLiteOpenHelper {

    private static final String TAG = "UserDbHelper";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "EventSchedulerUsers.db";
    static final String USER_TABLE = "USERS";

    // User Table Columns names
    private static final String USER_ID_COL = "user_id";
    private static final String USER_NAME_COL = "user_name";
    private static final String USER_EMAIL_COL = "user_email";
    private static final String USER_PASSWORD_COL = "user_password";

    // create table sql query
    private String CREATE_USER_TABLE =
            "CREATE TABLE " + USER_TABLE + "("
                    + USER_ID_COL + " TEXT PRIMARY KEY ,"
                    + USER_NAME_COL + " TEXT,"
                    + USER_EMAIL_COL + " TEXT,"
                    + USER_PASSWORD_COL + " TEXT"
                    + ")";

    // drop table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + USER_TABLE;

    public UserDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_USER_TABLE);
        onCreate(db);
    }

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_ID_COL, user.uid);
        values.put(USER_NAME_COL, user.username);
        values.put(USER_EMAIL_COL, user.email);
        values.put(USER_PASSWORD_COL, user.password);

        // Inserting Row
        db.insert(USER_TABLE, USER_ID_COL, values);
        db.close();
    }

    public boolean findUser(String userID){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;

        boolean bUserFound = false;
        try {

            String[] selectionArgs = {userID};
            cursor = db.rawQuery(
                    "SELECT " + USER_ID_COL +
                            " FROM " + USER_TABLE +
                            " WHERE " + USER_ID_COL + "= ?", selectionArgs);

            if(cursor.getCount() > 0) {
                bUserFound = true;
            }

        } finally {

            cursor.close();
            db.close();
            return bUserFound;
        }
    }

    /**
     * This method is to fetch all user and return the list of user records
     * @return list
     */
    public List<User> getAllUser() {

        Cursor cursor;
        SQLiteDatabase db = this.getReadableDatabase();

        // array of columns to fetch
        String[] columns = {
                USER_ID_COL,
                USER_NAME_COL,
                USER_EMAIL_COL,
                USER_PASSWORD_COL};

        List<User> userList = new ArrayList<>();

        try {
            // query the user table
            cursor = db.query(USER_TABLE, columns, null, null, null, null, null);

            // Traversing trough all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    User user = new User();
                    user.uid = cursor.getString(cursor.getColumnIndex(USER_ID_COL));
                    user.username = cursor.getString(cursor.getColumnIndex(USER_NAME_COL));
                    user.email = cursor.getString(cursor.getColumnIndex(USER_EMAIL_COL));

                    userList.add(user);
                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception ex){
            Log.d(TAG, ex.getMessage());
        } finally {
            db.close();
        }

        return userList;
    }
}