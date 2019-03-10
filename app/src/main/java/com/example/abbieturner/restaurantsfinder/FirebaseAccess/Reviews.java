package com.example.abbieturner.restaurantsfinder.FirebaseAccess;

import android.support.annotation.NonNull;

import com.example.abbieturner.restaurantsfinder.Data.ReviewFirebase;
import com.example.abbieturner.restaurantsfinder.FirebaseModels.UserReview;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class Reviews {
    private FirebaseDatabase database;
    private DatabaseReference reviewsRef, userReviewsRef;
    private Reviews.ReviewsListener callback;
    private StorageReference sRef;
    private FirebaseStorage storage;

    public Reviews(ReviewsListener callback) {
        this.callback = callback;
        database = FirebaseDatabase.getInstance();
        reviewsRef = database.getReference().child("reviews");
        storage = FirebaseStorage.getInstance();
        sRef = storage.getReference();
        userReviewsRef = database.getReference().child("userReviews");
    }

    public interface ReviewsListener {
        void onReviewsLoaded(List<ReviewFirebase> reviews, boolean hasFailed);
        void onUserReviewsLoaded(List<UserReview> userReviews, boolean hasFailed);
    }

    public void getReviews(String restaurantId) {
        final DatabaseReference ref = reviewsRef.child(restaurantId);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<ReviewFirebase> list = new ArrayList<>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ReviewFirebase review = postSnapshot.getValue(ReviewFirebase.class);
                    list.add(review);
                }
                ref.removeEventListener(this);
                callback.onReviewsLoaded(list, false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onReviewsLoaded(null, true);
            }
        });
    }

    public void getUserReviews(String userId){
        final DatabaseReference ref = userReviewsRef.child(userId);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<UserReview> list = new ArrayList<>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    UserReview review = postSnapshot.getValue(UserReview.class);
                    list.add(review);
                }

                ref.removeEventListener(this);
                callback.onUserReviewsLoaded(list, false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onUserReviewsLoaded(null, true);
                ref.removeEventListener(this);
            }
        });
    }
}
