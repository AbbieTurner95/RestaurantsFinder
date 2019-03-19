package com.example.abbieturner.restaurantsfinder.FirebaseAccess;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.example.abbieturner.restaurantsfinder.FirebaseAccess.Listeners.UserListener;
import com.example.abbieturner.restaurantsfinder.FirebaseModels.Friend;
import com.example.abbieturner.restaurantsfinder.FirebaseModels.UserFirebaseModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class User {
    private FirebaseDatabase database;
    private DatabaseReference usersRef;
    private UserListener callback;
    private StorageReference sRef;
    private FirebaseStorage storage;
    private FirebaseAuth mAuth;

    public User(UserListener callback) {
        this.callback = callback;
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference().child("users");
        storage = FirebaseStorage.getInstance();
        sRef = storage.getReference().child("usersProfilePictures");
        mAuth = FirebaseAuth.getInstance();
    }

    public void getUsers(final String searchTerm){
        final Query query = usersRef.orderByChild("email").startAt(searchTerm);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Friend> users = new ArrayList<>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Friend user = postSnapshot.getValue(Friend.class);
                    user.setUserId(postSnapshot.getKey());
                    if(user.getEmail().startsWith(searchTerm)){
                        users.add(user);
                    }
                }
                query.removeEventListener(this);
                callback.OnUsersLoaded(users, false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.OnUsersLoaded(null, true);
            }
        });
    }

    public void editUser(UserFirebaseModel user){
        if(user.hasPicture()){
            uploadPicture(user);
        }else{
            uploadUser(user);
        }

    }

    private void uploadPicture(final UserFirebaseModel user){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        user.getPicture().compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();

        final UploadTask uploadTask = sRef.child("usersPhotos").child(user.getId() + ".jpg").putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

                callback.OnUserUpdated(true);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                sRef
                        .child("usersPhotos")
                        .child(user.getId() + ".jpg")
                        .getDownloadUrl()
                        .addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull com.google.android.gms.tasks.Task task) {
                                if (task.isSuccessful()) {
                                    user.setPictureUrl(task.getResult().toString());

                                    uploadUser(user);
                                } else {
                                    callback.OnUserUpdated(true);
                                }
                            }
                        });
            }
        });
    }
    private void uploadUser(UserFirebaseModel user){
        usersRef.child(user.getId())
                .setValue(createUserHash(user))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            callback.OnUserUpdated(false);
                        }else{
                            callback.OnUserUpdated(true);
                        }
                    }
                });
    }

    public void getUserById(String userId){
        final DatabaseReference u = usersRef.child(userId);
        u.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserFirebaseModel user = dataSnapshot.getValue(UserFirebaseModel.class);

                callback.OnUserLoaded(user, false);

                u.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.OnUserLoaded(null, true);
                u.removeEventListener(this);
            }
        });
    }

    public void createProfileIfDoesNotExist(final String userId){
        final DatabaseReference u = usersRef.child(userId);
        u.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserFirebaseModel user = dataSnapshot.getValue(UserFirebaseModel.class);

                if(user == null){
                    createProfile(userId);
                }

                u.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void createProfile(String userId){
        usersRef
                .child(userId)
                .setValue(createUserHash(new UserFirebaseModel(userId)))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
//                if(task.isSuccessful()){
//
//                }else{
//
//                }
            }
        });
    }

    private void updateProfile(UserFirebaseModel userFirebaseModel){

    }

    private HashMap createUserHash(UserFirebaseModel userFirebaseModel){
        HashMap hm = new HashMap();

        hm.put("id", userFirebaseModel.getId());
        hm.put("name", userFirebaseModel.getName());
        hm.put("memberSince", userFirebaseModel.getMemberSince());
        hm.put("numberOfReviews", userFirebaseModel.getNumberOfReviews());
        hm.put("pictureUrl", userFirebaseModel.getPictureUrl());
        hm.put("email", mAuth.getCurrentUser().getEmail());

        return hm;
    }
}
