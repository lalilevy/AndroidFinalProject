package com.colman.androidfinalproject.Holders;

import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.colman.androidfinalproject.Models.User;
import com.colman.androidfinalproject.R;

public class UserViewHolder extends RecyclerView.ViewHolder {

    public AppCompatCheckBox cbItemUserName;
    public AppCompatTextView textViewEmail;

    public UserViewHolder(View view) {
        super(view);
        cbItemUserName = (AppCompatCheckBox) view.findViewById(R.id.cbItemUserName);
        textViewEmail = (AppCompatTextView) view.findViewById(R.id.tvItemUserEmail);
    }

    public void bindToUser(User user) {
        textViewEmail.setText(user.email);
        cbItemUserName.setText(user.username);
    }
}
