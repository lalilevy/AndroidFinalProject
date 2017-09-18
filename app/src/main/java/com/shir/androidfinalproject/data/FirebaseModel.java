package com.colman.androidfinalproject.Data;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.colman.androidfinalproject.CallBacks.AddCommentCallback;
import com.colman.androidfinalproject.CallBacks.CreateUserCallback;
import com.colman.androidfinalproject.CallBacks.GetAllCommentsCallback;
import com.colman.androidfinalproject.CallBacks.GetAllEventsCallback;
import com.colman.androidfinalproject.CallBacks.GetAllUsersCallback;
import com.colman.androidfinalproject.CallBacks.GetEventCallback;
import com.colman.androidfinalproject.CallBacks.SaveEventCallback;
import com.colman.androidfinalproject.CallBacks.SigninCallback;
import com.colman.androidfinalproject.Models.Comment;
import com.colman.androidfinalproject.Models.Event;
import com.colman.androidfinalproject.Models.User;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by shir on 15-Aug-17.
 */

public class FirebaseModel {

    public void addEvent(Event event, final SaveEventCallback callback) {

        // Define database reference for events
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("events");

        // Generate new key for event
        String eventKey = databaseRef.push().getKey();
        Map<String, Object> eventValues = event.toMap();

        // Set new event on the events node
        databaseRef.child(eventKey).setValue
                (eventValues, (databaseError, databaseReference) ->
                        callback.onCompleted(eventKey));
    }

    public void getEvents(String strUserID, final GetAllEventsCallback callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef1 = database.getReference("events");
        final ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Event> list = new LinkedList<>();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Event event = snap.getValue(Event.class);
                    if (event.usersList.contains(strUserID)) {
                        event.eventID = snap.getKey();
                        list.add(event);
                    }
                }

                Collections.sort(list, (o1, o2) -> o2.createDate.compareTo(o1.createDate));
                callback.onCompleted(list);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onCanceled();
            }
        };

        myRef1.addValueEventListener(listener);
    }

    public void getUsers(String userID, final GetAllUsersCallback callback){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef1 = database.getReference("users");
        final ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<User> list = new LinkedList<>();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    User user = snap.getValue(User.class);
                    user.uid =snap.getKey();
                    if (!user.uid.equals(userID)){
                        list.add(user);
                    }
                }

                Collections.sort(list, (o1, o2) -> o2.username.compareTo(o1.username));
                callback.onCompleted(list);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onCanceled();
            }
        };

        myRef1.addListenerForSingleValueEvent(listener);
    }

    public void notifyEventChanged(Event updatedEvent){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference eventRef = database.getReference("events");

        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> eventValues = updatedEvent.toMap();
        childUpdates.put(updatedEvent.eventID, eventValues);

        eventRef.updateChildren(childUpdates);
    }

    public void getEvent(String eventID, final GetEventCallback callback) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("events").child(eventID);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Event event = dataSnapshot.getValue(Event.class);
                event.eventID = dataSnapshot.getKey();
                callback.onCompleted(event);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onCanceled();
            }
        });
    }

    public void getEventComments(String eventID, final GetAllCommentsCallback callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef1 = database.getReference("event-comments");
        final ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Comment> list = new LinkedList<>();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Comment comment = snap.getValue(Comment.class);
                    comment.commentID = snap.getKey();
                    list.add(comment);
                }

                Collections.sort(list, (o1, o2) -> o2.createDate.compareTo(o1.createDate));
                callback.onCompleted(list);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onCanceled();
            }
        };

        myRef1.child(eventID).addValueEventListener(listener);
    }

    public void addComment(String eventId, Comment comment, final AddCommentCallback callback) {
        DatabaseReference databaseRef =
                FirebaseDatabase.getInstance().getReference("event-comments").child(eventId);

        // Generate new key for comment
        String commentKey = databaseRef.push().getKey();
        Map<String, Object> commentValues = comment.toMap();

        // Set new comment on the comments node
        databaseRef.child(commentKey).setValue
                (commentValues, (databaseError, databaseReference) ->
                        callback.onCompleted());
    }

    public User getCurrentUser(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        User user = null;

        if (firebaseUser != null){
            user = new User(firebaseUser.getUid(), firebaseUser.getDisplayName(), firebaseUser.getEmail());
        }

        return user;
    }

    public void signInWithEmailAndPassword(String userEmail, String password, SigninCallback callback){

        FirebaseAuth.getInstance().signInWithEmailAndPassword(userEmail, password)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()){
                        callback.onFailed();
                    } else {
                        FirebaseUser user = task.getResult().getUser();
                        callback.onSuccess(user.getUid(), user.getDisplayName());
                    }
                });
    }

    public void createUserWithEmailAndPassword(String userName,
                                                String email,
                                                String password,
                                                CreateUserCallback callback){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(task -> {

                if (!task.isSuccessful()){
                    callback.onFailed("Registretion failed");
                } else {

                    FirebaseUser firebaseUser = task.getResult().getUser();
                    updateUserProfile(firebaseUser, userName, callback);
                }
            });
    }

    private void updateUserProfile(FirebaseUser firebaseUser,
                                   String userName,
                                   CreateUserCallback callback) {

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
            .setDisplayName(userName)
            .build();

        firebaseUser.updateProfile(profileUpdates)
            .addOnCompleteListener(task -> {

                if (!task.isSuccessful()){
                    callback.onFailed("failed to update user profile");
                } else {

                    User user = new User();
                    user.uid = firebaseUser.getUid();
                    user.email = firebaseUser.getEmail();
                    user.username = userName;

                    addUser(user, callback);
                }
            });
    }

    private void addUser(User user, CreateUserCallback callback){
        DatabaseReference mUsersRef =
                FirebaseDatabase.getInstance().getReference("users");

        mUsersRef.child(user.uid).setValue(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                callback.onSuccess(user.uid, user.username);
            } else {
                callback.onFailed("faild on adding user to firebase DB");
            }
        });
    }

    public void signOut(){
        FirebaseAuth.getInstance().signOut();
    }
}
