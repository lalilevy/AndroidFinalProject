package com.shir.androidfinalproject.Fragments;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * Created by shir on 11-Sep-17.
 */

public class RecentEventsFragment extends PostListFragment {

    public RecentEventsFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // [START recent_posts_query]
        // Last 100 posts, these are automatically the 100 most recent
        // due to sorting by push() keys
        Query recentEventsQuery = databaseReference.child("events")
                .limitToFirst(100);
        // [END recent_posts_query]

        return recentEventsQuery;
    }
}