package com.example.abbieturner.restaurantsfinder.Singletons;

import com.example.abbieturner.restaurantsfinder.FirebaseModels.UserFirebaseModel;

public class UserInstance {
    private static UserInstance theSingleton = null;
    private UserFirebaseModel user = null;

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
}
