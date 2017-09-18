package com.colman.androidfinalproject.Models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Comment {

    public String commentID;
    public String userID;
    public String author;
    public String text;
    public Date createDate;

    public Comment() {
        // Default constructor required for calls to DataSnapshot.getValue(Comment.class)
    }

    public Comment(String uid, String author, String text) {
        this.userID = uid;
        this.author = author;
        this.text = text;
        createDate = new Date();
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userID", userID);
        result.put("author", author);
        result.put("text", text);
        result.put("createDate", createDate);
        return result;
    }
}