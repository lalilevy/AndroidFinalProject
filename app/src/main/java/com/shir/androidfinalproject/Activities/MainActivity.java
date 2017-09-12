package com.shir.androidfinalproject.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.shir.androidfinalproject.Fragments.MyPostsFragment;
import com.shir.androidfinalproject.Fragments.MyTopPostsFragment;
import com.shir.androidfinalproject.Fragments.RecentPostsFragment;
import com.shir.androidfinalproject.data.common;
import com.shir.androidfinalproject.data.DataManager;

import com.shir.androidfinalproject.R;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    public static final String USER_ID = "USER_ID";
    public static final String USER_NAME = "USER_NAME";

    private AppCompatTextView textViewName;
    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    String userName;

//    private RecyclerView recyclerViewUsers;
//    private List<User> listUsers;
//    private UsersRecyclerAdapter usersRecyclerAdapter;
//    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Event Scheduler Feed");

        userName = getIntent().getStringExtra(MainActivity.USER_NAME);

        // Create the adapter that will return a fragment for each section
        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            private final Fragment[] mFragments = new Fragment[] {
                    new RecentPostsFragment(),
                    new MyPostsFragment(),
                    new MyTopPostsFragment(),
            };
            private final String[] mFragmentNames = new String[] {
                    getString(R.string.heading_recent),
                    getString(R.string.heading_my_posts),
                    getString(R.string.heading_my_top_posts)
            };
            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }
            @Override
            public int getCount() {
                return mFragments.length;
            }
            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentNames[position];
            }
        };

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        // Button launches NewPostActivity
        findViewById(R.id.fab_new_event).setOnClickListener(this);
    }

//    private void updateUser(Intent intent) {
//        if (intent == null)
//            intent = getIntent();
//        mUser = (User) intent.getSerializableExtra(MainActivity.CURR_USER);
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case ADD_STUDENT_REQUEST:
//                if (resultCode == RESULT_OK) {
//                    ((StudentListAdapter) mStudentList.getAdapter()).notifyDataSetChanged();
//                }
//                break;
//            case STUDENT_DETAILS:
//                if (resultCode == RESULT_OK) {
//                    ((StudentListAdapter) mStudentList.getAdapter()).notifyDataSetChanged();
//                }
//                else if (resultCode == EditStudentActivity.RESULT_DELETE) {
//                    ((StudentListAdapter) mStudentList.getAdapter()).notifyDataSetChanged();
//                }
//                break;
//        }
//    }

//    @Override
//    public void onEventClick(Event event) {
//        mEvent = event;
//        openEventDetails(mEvent);
//
//        Toast.makeText(getApplicationContext(),
//                "A event in list was clicked = " + event.title,
//                Toast.LENGTH_LONG).show();
//    }

//    private void openEventDetails(Event event) {
//        // Launching new Activity on selecting single List Item
//        Intent i = new Intent(getApplicationContext(), EventDetailActivity.class);
//
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(EventDetailActivity.CURR_EVENT, event);
//        i.putExtras(bundle);
//
//        startActivityForResult(i, this.EVENT_DETAILS_REQUEST);
//    }

//    @Override
//    public void onClick(View view) {
//        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show();
//        Intent intent = new Intent(this, NewEventActivity.class);
//        startActivity(intent);
//        finish();
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId){
            case R.id.main_logout:
                logOut();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private void logOut() {
        common.Instance().setCurrentUser(null);
        getDataManager().setLogin(false, null);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private DataManager getDataManager(){
        return DataManager.getInstance(getApplicationContext());
    }

    @Override
    public void onClick(View v) {
        int nViewID = v.getId();

        switch (nViewID){
            case R.id.fab_new_event:
                try {
                    Intent intent = new Intent(MainActivity.this, NewEventActivity.class);
                    Bundle bundle = new Bundle();
                    intent.putExtras(bundle);
                    intent.putExtra(MainActivity.USER_NAME, userName);
                    startActivity(intent);

                } catch (Exception ex){
                    Toast.makeText(MainActivity.this, "New Event Exception: " + ex.getMessage(), Toast.LENGTH_LONG);
                }

                break;
        }
    }
}
