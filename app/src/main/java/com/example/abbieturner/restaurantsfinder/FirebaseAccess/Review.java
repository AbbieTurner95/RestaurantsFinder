package com.example.abbieturner.restaurantsfinder.FirebaseAccess;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.example.abbieturner.restaurantsfinder.Data.ReviewFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class Review {
    private FirebaseDatabase database;
    private DatabaseReference reviewsRef;
    private ReviewListener callback;
    private StorageReference sRef;
    private FirebaseStorage storage;

    public Review(ReviewListener callback) {
        this.callback = callback;
        database = FirebaseDatabase.getInstance();
        reviewsRef = database.getReference().child("reviews");
        storage = FirebaseStorage.getInstance();
        sRef = storage.getReference();
    }

    public interface ReviewListener {
        void onReviewCreated(boolean hasFailed);
    }

    public void createReview(ReviewFirebase newReview, String restaurantId) {
        if (newReview.hasPicture()) {
            uploadPicture(newReview, restaurantId);
        } else {
            uploadReview(newReview, restaurantId);
        }
    }

    private void uploadPicture(final ReviewFirebase newReview, final String restaurantId) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        newReview.getPicture().compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();

        final UploadTask uploadTask = sRef.child("reviewImages").child(newReview.getId() + ".jpg").putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

                callback.onReviewCreated(true);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                sRef
                        .child("reviewImages")
                        .child(newReview.getId() + ".jpg")
                        .getDownloadUrl()
                        .addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull com.google.android.gms.tasks.Task task) {
                                if (task.isSuccessful()) {
                                    newReview.setPictureUrl(task.getResult().toString());

                                    uploadReview(newReview, restaurantId);
                                } else {
                                    callback.onReviewCreated(true);
                                }
                            }
                        });
            }
        });
    }

    private void uploadReview(ReviewFirebase newReview, String restaurantId) {
        DatabaseReference ref = reviewsRef.child(restaurantId);
        ref.child(newReview.getId()).setValue(createHashMap(newReview)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    callback.onReviewCreated(false);
                } else {
                    callback.onReviewCreated(true);
                }
            }
        });
    }

    private HashMap createHashMap(ReviewFirebase newReview) {
        HashMap hm = new HashMap();

        hm.put("id", newReview.getId());
        hm.put("rating", newReview.getRating());
        hm.put("pictureUrl", newReview.getPictureUrl());
        hm.put("review", newReview.getReview());

        return hm;
    }
}
