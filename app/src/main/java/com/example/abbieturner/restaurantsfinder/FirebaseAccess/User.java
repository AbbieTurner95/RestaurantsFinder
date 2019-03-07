package com.example.abbieturner.restaurantsfinder.FirebaseAccess;

import android.support.annotation.NonNull;

import com.example.abbieturner.restaurantsfinder.FirebaseAccess.Listeners.UserListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class User {
    private FirebaseDatabase database;
    private DatabaseReference usersRef;
    private UserListener callback;
    private StorageReference sRef;
    private FirebaseStorage storage;

    public User(UserListener callback) {
        this.callback = callback;
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference().child("users");
        storage = FirebaseStorage.getInstance();
        sRef = storage.getReference().child("usersProfilePictures");
    }

    public void createProfileIfDoesNotExist(String userId){
        usersRef.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void createProfile(String userId){

    }

    private void updateProfile(com.example.abbieturner.restaurantsfinder.FirebaseModels.User user){

    }

    private HashMap createUserHash(com.example.abbieturner.restaurantsfinder.FirebaseModels.User user){
        HashMap hm = new HashMap();

        hm.put("id", user.getId());
        hm.put("name", user.getName());
        //hm.put("memberSince", );
        hm.put("numberOfReviews", user.getNumberOfReviews());
        hm.put("pictureUrl", user.getPictureUrl());

        return hm;
    }
}
