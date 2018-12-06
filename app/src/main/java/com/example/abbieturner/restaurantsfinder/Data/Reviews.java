package com.example.abbieturner.restaurantsfinder.Data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Reviews {
    @SerializedName("reviews")
    public List<Review> reviewsList;

    public Reviews(List<Review> reviewsList) {
        this.reviewsList = reviewsList;
    }

    public List<Review> getReviewsList() {
        return reviewsList;
    }

    public void setReviewsList(List<Review> reviewsList) {
        this.reviewsList = reviewsList;
    }
}
