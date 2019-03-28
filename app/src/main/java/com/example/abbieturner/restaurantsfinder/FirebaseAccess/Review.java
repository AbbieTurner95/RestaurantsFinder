package com.example.abbieturner.restaurantsfinder.FirebaseAccess;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.example.abbieturner.restaurantsfinder.Data.ReviewFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class Review {
    private FirebaseDatabase database;
    private DatabaseReference reviewsRef, userReviewsRef;
    private ReviewListener callback;
    private StorageReference sRef;
    private FirebaseStorage storage;
    private FirebaseAuth mAuth;

    public Review(ReviewListener callback) {
        this.callback = callback;
        database = FirebaseDatabase.getInstance();
        reviewsRef = database.getReference().child("reviews");
        userReviewsRef = database.getReference().child("userReviews");
        storage = FirebaseStorage.getInstance();
        sRef = storage.getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    public interface ReviewListener {
        void onReviewCreated(boolean hasFailed);
    }

    public void createReview(ReviewFirebase newReview, String restaurantId, String restaurantName) {
        if (newReview.hasPicture()) {
            uploadPicture(newReview, restaurantId, restaurantName);
        } else {
            uploadReview(newReview, restaurantId, restaurantName);
        }
    }

    private void uploadPicture(final ReviewFirebase newReview, final String restaurantId, final String restaurantName) {
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

                                    uploadReview(newReview, restaurantId, restaurantName);
                                } else {
                                    callback.onReviewCreated(true);
                                }
                            }
                        });
            }
        });
    }

    private void uploadReview(final ReviewFirebase newReview, final String restaurantId, final String restaurantName) {
        DatabaseReference ref = reviewsRef.child(restaurantId);
        ref.child(newReview.getId()).setValue(createHashMap(newReview)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    createUserReview(newReview, restaurantId, restaurantName);

                } else {
                    callback.onReviewCreated(true);
                }
            }
        });
    }

    private void createUserReview(ReviewFirebase review, String restaurantId, String restaurantName) {
        if (mAuth != null) {
            String userId = mAuth.getUid();

            if (userId != null && !userId.isEmpty()) {
                userReviewsRef.child(userId).child(review.getId()).setValue(createUserReviewHashMap(review, restaurantId, restaurantName));
            }
        }

        callback.onReviewCreated(false);
    }


    private HashMap createHashMap(ReviewFirebase newReview) {
        HashMap hm = new HashMap();

        hm.put("id", newReview.getId());
        hm.put("rating", newReview.getRating());
        hm.put("pictureUrl", newReview.getPictureUrl());
        hm.put("review", newReview.getReview());

        return hm;
    }

    private HashMap createUserReviewHashMap(ReviewFirebase newReview, String restaurantId, String restaurantName) {
        HashMap hm = new HashMap();

        hm.put("reviewId", newReview.getId());
        hm.put("review", newReview.getReview());
        hm.put("restaurantId", restaurantId);
        hm.put("restaurantName", restaurantName);
        hm.put("userId", mAuth.getUid());
        hm.put("pictureUrl", newReview.getPictureUrl());

        return hm;
    }
}
