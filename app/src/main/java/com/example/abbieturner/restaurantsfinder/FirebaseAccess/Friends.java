package com.example.abbieturner.restaurantsfinder.FirebaseAccess;

import android.support.annotation.NonNull;

import com.example.abbieturner.restaurantsfinder.FirebaseAccess.Listeners.FriendsListener;
import com.example.abbieturner.restaurantsfinder.FirebaseModels.Friend;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Friends {
    private FirebaseDatabase database;
    private DatabaseReference friendsRef;
    private FriendsListener callback;

    public Friends(FriendsListener callback) {
        this.callback = callback;
        database = FirebaseDatabase.getInstance();
        friendsRef = database.getReference().child("friends");
    }

    public void getFriends(String userId){
        final DatabaseReference ref = friendsRef.child(userId);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Friend> list = new ArrayList<>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Friend friend = postSnapshot.getValue(Friend.class);
                    list.add(friend);
                }
                ref.removeEventListener(this);
                callback.onGetFriendsCompleted(list, false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onGetFriendsCompleted(null, true);
            }
        });
    }
}
