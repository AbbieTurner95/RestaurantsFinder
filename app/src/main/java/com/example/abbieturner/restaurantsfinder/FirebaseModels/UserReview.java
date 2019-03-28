package com.example.abbieturner.restaurantsfinder.FirebaseModels;

import android.graphics.Bitmap;

public class UserReview {
    private String restaurantId;
    private String pictureUrl;
    private String restaurantName;
    private String review;
    private String reviewId;
    private String userId;
    private Bitmap picture;

    public UserReview() {

    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean hasPictureUrl() {
        return pictureUrl != null && !pictureUrl.isEmpty();
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public boolean hasPicture() {
        return picture != null;
    }
}
