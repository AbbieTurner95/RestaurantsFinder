package com.example.abbieturner.restaurantsfinder.Data;

public class ReviewModel {
    private boolean isFirebaseReview;
    private ReviewFirebase reviewF;
    private UserReviews.UserReviewsData.ReviewData review;

    public ReviewModel(UserReviews.UserReviewsData.ReviewData review){
        this.review = review;
        this.isFirebaseReview = false;
    }

    public ReviewModel(ReviewFirebase review){
        this.reviewF = review;
        this.isFirebaseReview = true;
    }

    public ReviewFirebase getFirebaseReview(){
        return reviewF;
    }

    public UserReviews.UserReviewsData.ReviewData getReview(){
        return review;
    }

    public boolean isFirebaseReview(){
        return isFirebaseReview;
    }
}
