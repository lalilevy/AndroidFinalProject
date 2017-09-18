package com.shir.androidfinalproject.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shir.androidfinalproject.Holders.UserViewHolder;
import com.shir.androidfinalproject.Models.User;
import com.shir.androidfinalproject.R;

import java.util.ArrayList;
import java.util.List;

public class UsersListAdapter extends RecyclerView.Adapter<UserViewHolder>  {
    private List<User> listUsers;
    private List<User> lstSelectedUsers = new ArrayList<>();

    public UsersListAdapter(List<User> listUsers) {
        this.listUsers = listUsers;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);

        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        holder.bindToUser(listUsers.get(position));

        //in some cases, it will prevent unwanted situations
        holder.cbItemUserName.setOnCheckedChangeListener(null);

        holder.cbItemUserName.setOnCheckedChangeListener((buttonView, isChecked) ->{
            holder.cbItemUserName.setChecked(isChecked);
            User checkUser = listUsers.get(position);
            if (isChecked) {
                lstSelectedUsers.add(checkUser);
            } else {
                lstSelectedUsers.remove(checkUser);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(listUsers == null) return 0;
        return listUsers.size();
    }

    public List<String> getSelectedUsers(){
        List<String> lstString = new ArrayList<>(lstSelectedUsers.size());

        if (!lstSelectedUsers.isEmpty()){
            for (User user: lstSelectedUsers) {
                lstString.add(user.uid);
            }
        }

        return lstString;
    }

    public boolean isUsersSelected(){
        return !lstSelectedUsers.isEmpty();
    }
}
