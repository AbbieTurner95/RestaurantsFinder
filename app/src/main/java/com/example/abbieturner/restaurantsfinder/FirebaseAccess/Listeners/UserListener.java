package com.example.abbieturner.restaurantsfinder.FirebaseAccess.Listeners;

import com.example.abbieturner.restaurantsfinder.FirebaseModels.Friend;
import com.example.abbieturner.restaurantsfinder.FirebaseModels.UserFirebaseModel;

import java.util.List;

public interface UserListener {
    void OnUserLoaded(UserFirebaseModel user, boolean hasFailed);
    void OnUserUpdated(boolean hasFailed);
    void OnUsersLoaded(List<Friend> users, boolean hasFailed);
    void OnUserExists(boolean exists, boolean hasFailed);
    void OnUserCreated(boolean hasFailed);
}
