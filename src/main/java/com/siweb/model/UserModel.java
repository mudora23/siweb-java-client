package com.siweb.model;
import org.json.JSONObject;

public class UserModel extends ObservableModel<User> {

    // Declares variables
    private static final UserModel instance = new UserModel();

    private User currentUser;


    // Returns the instance of the controller
    public static UserModel getInstance(){
        return instance;
    }

    private UserModel(){}

    public void add(JSONObject jsonUser) {
        oList.add(new User(jsonUser));
    }

    public void setCurrentUser(JSONObject jsonUser) {
        currentUser = new User(jsonUser);
    }

    public int getCurrentUserID(){
        return currentUser.id;
    }

    public String getCurrentUserProfileRole(){
        return currentUser.profileRole;
    }

}
