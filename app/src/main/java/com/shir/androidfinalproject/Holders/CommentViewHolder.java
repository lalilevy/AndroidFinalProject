package com.shir.androidfinalproject.Holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.shir.androidfinalproject.Models.Comment;
import com.shir.androidfinalproject.R;

public class CommentViewHolder extends RecyclerView.ViewHolder {

    public TextView authorView;
    public TextView bodyView;

    public CommentViewHolder(View itemView) {
        super(itemView);

        authorView = (TextView) itemView.findViewById(R.id.comment_author);
        bodyView = (TextView) itemView.findViewById(R.id.comment_body);
    }

    public void bindToComment(Comment comment) {
        authorView.setText(comment.author);
        bodyView.setText(comment.text);
    }
}
