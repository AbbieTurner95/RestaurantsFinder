package com.example.abbieturner.restaurantsfinder.FirebaseAccess;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.example.abbieturner.restaurantsfinder.FirebaseAccess.Listeners.RestaurantListener;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class Restaurant {
    private FirebaseDatabase database;
    private DatabaseReference reviewsRef;
    private RestaurantListener callback;
    private StorageReference sRef;
    private FirebaseStorage storage;
    private DatabaseReference geofireRef;
    private GeoFire geoFire;

    public Restaurant(RestaurantListener callback){
        this.callback = callback;
        database = FirebaseDatabase.getInstance();
        reviewsRef = database.getReference().child("restaurants");
        storage = FirebaseStorage.getInstance();
        sRef = storage.getReference();

        geofireRef = database.getReference().child("restaurantsGeoFire");
        geoFire = new GeoFire(geofireRef);
    }

    public void createRestaurant(com.example.abbieturner.restaurantsfinder.FirebaseModels.Restaurant restaurant){
        if(restaurant.hasPicture()){
            uploadPicture(restaurant);
        }else{
            uploadRestaurant(restaurant);
        }
    }

    private void uploadPicture(final com.example.abbieturner.restaurantsfinder.FirebaseModels.Restaurant restaurant){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        restaurant.getPicture().compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();

        final UploadTask uploadTask = sRef.child("restaurantImages").child(restaurant.getId() + ".jpg").putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

                callback.onRestaurantCreated(true);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                sRef
                        .child("restaurantImages")
                        .child(restaurant.getId() + ".jpg")
                        .getDownloadUrl()
                        .addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull com.google.android.gms.tasks.Task task) {
                                if(task.isSuccessful()){
                                    restaurant.setPictureUrl(task.getResult().toString());

                                    uploadRestaurant(restaurant);
                                }else{
                                    callback.onRestaurantCreated(true);
                                }
                            }
                        });
            }
        });
    }

    private void uploadRestaurant(final com.example.abbieturner.restaurantsfinder.FirebaseModels.Restaurant restaurant){
        reviewsRef.child(restaurant.getId()).setValue(createRestaurantHashMap(restaurant)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    addRestaurantToGeoFire(restaurant);
                }else{
                    callback.onRestaurantCreated(true);
                }
            }
        });
    }

    private void addRestaurantToGeoFire(com.example.abbieturner.restaurantsfinder.FirebaseModels.Restaurant restaurant){
        GeoLocation location = new GeoLocation(restaurant.getLat(), restaurant.getLng());

        geoFire.setLocation(restaurant.getId(), location , new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                if (error != null) {
                    System.err.println("There was an error saving the location to GeoFire: " + error);
                    callback.onRestaurantCreated(true);
                } else {
                    System.out.println("Location saved on server successfully!");
                    callback.onRestaurantCreated(false);
                }
            }
        });
    }

    private HashMap createRestaurantHashMap(com.example.abbieturner.restaurantsfinder.FirebaseModels.Restaurant restaurant){
        HashMap hm = new HashMap();

        hm.put("id", restaurant.getId());
        hm.put("name", restaurant.getName());
        hm.put("address", restaurant.getAddress());
        hm.put("lng", restaurant.getLng());
        hm.put("lat", restaurant.getLat());
        hm.put("phone", restaurant.getPhone());
        hm.put("rating", 0.0);
        hm.put("delivery", restaurant.getDelivery());
        hm.put("menu", restaurant.getMenu());
        hm.put("web", restaurant.getWeb());
        hm.put("pictureUrl", restaurant.getPictureUrl());

        return hm;
    }
}
