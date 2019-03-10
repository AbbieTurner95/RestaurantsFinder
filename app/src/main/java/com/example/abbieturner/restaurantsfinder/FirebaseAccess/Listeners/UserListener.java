package com.example.abbieturner.restaurantsfinder.FirebaseAccess.Listeners;

import com.example.abbieturner.restaurantsfinder.FirebaseModels.UserFirebaseModel;

public interface UserListener {
    void OnUserLoaded(UserFirebaseModel user, boolean hasFailed);
    void OnUserUpdated(boolean hasFailed);
}
