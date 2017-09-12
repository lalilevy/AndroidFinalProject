package com.shir.androidfinalproject.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.shir.androidfinalproject.Adapters.UsersRecyclerAdapter;
import com.shir.androidfinalproject.Models.User;
import com.shir.androidfinalproject.R;

import java.util.ArrayList;
import java.util.List;

//import static com.facebook.FacebookSdk.getApplicationContext;

public class AddEventUsersFragment extends Fragment
//        implements CompoundButton.OnCheckedChangeListener
{
    public static final String TAG = "AddEventUsersFragment";
    private static final String FRIENDS_LIST = "FRIENDS_LIST";

    private InviteFriendsListener mListener;
    private ListView lvFriendsList;
    private UsersRecyclerAdapter usersRecyclerAdapter;
    private RecyclerView recyclerViewUsers;
    private List<User> listUsers;

    public AddEventUsersFragment() {
        // Required empty public constructor
    }

    public static AddEventUsersFragment newInstance(ArrayList<User> friendsList) {
        AddEventUsersFragment fragment = new AddEventUsersFragment();
        Bundle args = new Bundle();
        args.putSerializable(FRIENDS_LIST, friendsList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            listUsers = (List<User>)getArguments().getSerializable(FRIENDS_LIST);

            usersRecyclerAdapter = new UsersRecyclerAdapter(listUsers);
            //lv.setAdapter(usersRecyclerAdapter);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_invite_friends, container, false);

//        lvFriendsList = (ListView)view.findViewById(R.id.lv_friends_list);
//        for (User user: lstFriends) {
//            CheckBox cb = new CheckBox(getContext());
//            cb.setText(user.getFullName());
//            cb.setOnCheckedChangeListener(this);
//            lvFriendsList.addView(cb);
//        }

        initViews(view);
        initObjects();

        //vSelectedFriendsList = (ListView)view.findViewById(R.id.lv_selected_friendsd_list);
        return view;
    }

    /**
     * This method is to initialize views
     */
    private void initViews(View view) {
        //recyclerViewUsers = (RecyclerView)view.findViewById(R.id.recyclerViewUsers);
    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        listUsers = new ArrayList<>();
        usersRecyclerAdapter = new UsersRecyclerAdapter(listUsers);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());// getApplicationContext());
        recyclerViewUsers.setLayoutManager(mLayoutManager);
        recyclerViewUsers.setItemAnimator(new DefaultItemAnimator());
        recyclerViewUsers.setHasFixedSize(true);
        recyclerViewUsers.setAdapter(usersRecyclerAdapter);

        getAllUsers();
    }

    /**
     * This method is to fetch all user records from SQLite
     */
    private void getAllUsers() {

        //TODO: sign to firebase and get all users
//        // AsyncTask is used that SQLite operation not blocks the UI Thread.
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... params) {
//                listUsers.clear();
//                listUsers.addAll(databaseHelper.getAllUser());
//
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                super.onPostExecute(aVoid);
//                usersRecyclerAdapter.notifyDataSetChanged();
//            }
//        }.execute();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InviteFriendsListener) {
            mListener = (InviteFriendsListener) context;
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

//    @Override
//    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        int pos = lvFriendsList.getPositionForView(buttonView);
//        if (pos != ListView.INVALID_POSITION) {
//            User user = lstFriends.get(pos);
//            user.setSelected(isChecked);
//
//            Toast.makeText(
//                    this,
//                    "Clicked on Planet: " + p.getName() + ". State: is "
//                            + isChecked, Toast.LENGTH_SHORT).show();
//        }
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface InviteFriendsListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
