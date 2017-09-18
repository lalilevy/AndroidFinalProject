package com.colman.androidfinalproject.Data;

import com.colman.androidfinalproject.App;
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

import java.util.List;

/**
 * Created by shir on 02-Aug-17.
 */

public class model {

    public final static model instance = new model();

    private FirebaseModel firebaseModel;
    private UserDbHelper userDbHelper;

    private model() {
        firebaseModel = new FirebaseModel();
        userDbHelper = new UserDbHelper(App.getMyContext());
    }

    public void addEvent(Event event, SaveEventCallback callback) {
        firebaseModel.addEvent(event, callback);
    }

    public void getEvents(String strUserID, GetAllEventsCallback callback) {
        firebaseModel.getEvents(strUserID, callback);
    }

    public void getEvent(String eventID, GetEventCallback callback) {
        firebaseModel.getEvent(eventID, callback);
    }

    public void getUsers(String userID, GetAllUsersCallback callback) {
        firebaseModel.getUsers(userID, callback);
    }

    public void notifyEventChanged(Event updatedEvent){
        firebaseModel.notifyEventChanged(updatedEvent);
    }

    public void getEventComments(String eventID, GetAllCommentsCallback callback) {
        firebaseModel.getEventComments(eventID, callback);
    }

    public void addComment(String eventID, Comment comment, AddCommentCallback callback) {
        firebaseModel.addComment(eventID, comment, callback);
    }

    public User getCurrentUser(){
        return firebaseModel.getCurrentUser();
    }

    public List<User> getAllUsersInPhone(){
        return userDbHelper.getAllUser();
    }

    public void signIn(String userEmail, String password, SigninCallback callback){
        firebaseModel.signInWithEmailAndPassword(userEmail, password, new SigninCallback() {
            @Override
            public void onSuccess(String userID, String userName) {
                if (!userDbHelper.findUser(userID)){
                    userDbHelper.addUser(new User(userID, userName, userEmail, password));
                }

                callback.onSuccess(userID, userName);
            }

            @Override
            public void onFailed() {
                callback.onFailed();
            }
        });
    }

    public void createUser(String userName,
                           String email,
                           String password,
                           CreateUserCallback callback){

        firebaseModel.createUserWithEmailAndPassword(userName, email, password, new CreateUserCallback() {
            @Override
            public void onSuccess(String userID, String userName) {
                userDbHelper.addUser(new User(userID, userName, email, password));
                callback.onSuccess(userID, userName);
            }

            @Override
            public void onFailed(String message) {
                callback.onFailed(message);
            }
        });
    }

    public void signOut(){
        firebaseModel.signOut();
    }

    public boolean findUser(String userID){
        return userDbHelper.findUser(userID);
    }
}
