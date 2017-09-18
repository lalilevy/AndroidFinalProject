package com.shir.androidfinalproject.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shir.androidfinalproject.Holders.CommentViewHolder;
import com.shir.androidfinalproject.Models.Comment;
import com.shir.androidfinalproject.R;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {

    private static final String TAG = "CommentAdapter";

    private List<Comment> mComments = new ArrayList<>();

    public CommentAdapter(List<Comment> comments) {
        mComments = comments;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        Comment comment = mComments.get(position);
        holder.bindToComment(comment);
    }

    @Override
    public int getItemCount() {
        if(mComments == null) return 0;
        return mComments.size();
    }
}
