package com.colman.androidfinalproject.CallBacks;

import com.colman.androidfinalproject.Models.Comment;

import java.util.List;

public interface GetAllCommentsCallback {
    void onCompleted(List<Comment> comments);
    void onCanceled();
}
