package com.shir.androidfinalproject.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shir.androidfinalproject.Adapters.UsersListAdapter;
import com.shir.androidfinalproject.CallBacks.GetAllUsersCallback;
import com.shir.androidfinalproject.Models.User;
import com.shir.androidfinalproject.R;
import com.shir.androidfinalproject.Data.model;

import java.util.ArrayList;
import java.util.List;

public class AddEventUsersFragment extends Fragment
        implements View.OnClickListener {
    public static final String TAG = "AddEventUsersFragment";
    private static final String USER_ID = "USER_ID";

    private FloatingActionButton btnCreateEvent;

    private RecyclerView mRecycler;
    private UsersListAdapter mAdapter;
    RecyclerView.LayoutManager mManager;

    private List<User> lstAllUsers = new ArrayList<>();
    private String userID;

    private AddEventUsersListener mListener;

    public AddEventUsersFragment() {
        // Required empty public constructor
    }

    public static AddEventUsersFragment newInstance(String strUserID) {
        AddEventUsersFragment fragment = new AddEventUsersFragment();
        Bundle args = new Bundle();
        args.putString(USER_ID, strUserID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userID = getArguments().getString(USER_ID);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_event_users, container, false);

        initViews(view);
        initListeners();

        getUsers();

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initViews(View view) {
        btnCreateEvent = (FloatingActionButton) view.findViewById(R.id.btnCreateEvent);

        mRecycler = (RecyclerView) view.findViewById(R.id.usersListRecycler);
        mRecycler.setHasFixedSize(true);
        mManager = new LinearLayoutManager(getContext());
        mRecycler.setLayoutManager(mManager);
        mAdapter = new UsersListAdapter(lstAllUsers);
        mRecycler.setAdapter(mAdapter);
    }

    private void initListeners(){
        btnCreateEvent.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getUsers(){

        try{
            model.instance.getUsers(userID, new GetAllUsersCallback() {
                @Override
                public void onCompleted(List<User> users) {
                    lstAllUsers.clear();
                    lstAllUsers.addAll(users);
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCanceled() {

                }
            });
        }catch (Exception ex){
            Toast.makeText(getContext(), "Error while getting users", Toast.LENGTH_LONG);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCreateEvent:
                if (mAdapter.isUsersSelected() && (mListener != null)){
                    List<String> lstSelectedUsers = mAdapter.getSelectedUsers();
                    lstSelectedUsers.add(userID);
                    mListener.onCreateEventClick(lstSelectedUsers);
                }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddEventUsersListener) {
            mListener = (AddEventUsersListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface AddEventUsersListener {
        void onCreateEventClick(List<String> users);
    }
}
