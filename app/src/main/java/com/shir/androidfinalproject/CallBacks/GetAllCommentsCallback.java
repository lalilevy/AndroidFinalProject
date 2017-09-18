package com.shir.androidfinalproject.CallBacks;

import com.shir.androidfinalproject.Models.Comment;

import java.util.List;

public interface GetAllCommentsCallback {
    void onCompleted(List<Comment> comments);
    void onCanceled();
}
