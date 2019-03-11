package com.example.abbieturner.restaurantsfinder.Singletons;

import com.example.abbieturner.restaurantsfinder.FirebaseModels.Friend;
import com.example.abbieturner.restaurantsfinder.FirebaseModels.UserFirebaseModel;

import java.util.ArrayList;
import java.util.List;

public class UserInstance {
    private static UserInstance theSingleton = null;
    private UserFirebaseModel user = null;
    private List<Friend> friends = new ArrayList<>();

    public static UserInstance getInstance(){
        if (theSingleton == null) {
            theSingleton = new UserInstance();
        }
        return theSingleton;
    }

    public void setUser(UserFirebaseModel _user){
        user = _user;
    }

    public UserFirebaseModel getUser(){
        return user;
    }

    public List<Friend> getFriends() {
        return friends;
    }

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
    }
}
