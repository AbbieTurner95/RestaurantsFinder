package com.example.abbieturner.restaurantsfinder.FirebaseAccess.Listeners;

import com.example.abbieturner.restaurantsfinder.FirebaseModels.Friend;

import java.util.List;

public interface FriendsListener {
    void onGetFriendsCompleted(List<Friend> friends, boolean hasFailed);

    void onCreateFriendCompletes(boolean hasFailed);

    void onRemoveFriendCompleted(String friendId, boolean hasFailed);
}
