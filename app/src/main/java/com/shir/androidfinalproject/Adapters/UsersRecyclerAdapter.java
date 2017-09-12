package com.shir.androidfinalproject.Adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.shir.androidfinalproject.Models.User;
import com.shir.androidfinalproject.R;

import java.util.List;

/**
 * Created by shir on 03-Sep-17.
 */
public class UsersRecyclerAdapter
        extends RecyclerView.Adapter<UsersRecyclerAdapter.UserViewHolder> {
    private List<User> listUsers;

    public UsersRecyclerAdapter(List<User> listUsers) {
        this.listUsers = listUsers;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_recycler, parent, false);

        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        holder.textViewName.setText(listUsers.get(position).username);
        holder.textViewEmail.setText(listUsers.get(position).email);
    }

    @Override
    public int getItemCount() {
        Log.v(UsersRecyclerAdapter.class.getSimpleName(),""+listUsers.size());
        return listUsers.size();
    }

    /**
     * ViewHolder class
     */
    public class UserViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView textViewName;
        public AppCompatTextView textViewEmail;
        public AppCompatTextView textViewPassword;

        public UserViewHolder(View view) {
            super(view);
            textViewName = (AppCompatTextView) view.findViewById(R.id.textViewName);
            textViewEmail = (AppCompatTextView) view.findViewById(R.id.textViewEmail);
            textViewPassword = (AppCompatTextView) view.findViewById(R.id.textViewPassword);
        }
    }

    private static class FriendHolder {
        public TextView userName;
        public CheckBox chkBox;
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        View v = convertView;
//
//        FriendHolder holder = new FriendHolder();
//
//        if(convertView == null) {
//
//            LayoutInflater inflater = (LayoutInflater)convertView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            v = inflater.inflate(R.layout.item_user_recycler, null);
//
//            holder.userName = (TextView) v.findViewById(R.id.textViewName);
//            //holder.chkBox = (CheckBox) v.findViewById(R.id.chk_box);
//
//            //holder.chkBox.setOnCheckedChangeListener((AddEventUsersFragment) context);
//
//        } else {
//            holder = (FriendHolder) v.getTag();
//        }
//
//        User p = listUsers.get(position);
//        holder.userName.setText(p.getName());
//        //holder.chkBox.setChecked(p.isSelected());
//        holder.chkBox.setTag(p);
//
//        return v;
//    }
}
