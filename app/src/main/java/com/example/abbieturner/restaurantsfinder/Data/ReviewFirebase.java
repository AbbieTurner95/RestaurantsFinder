package com.example.abbieturner.restaurantsfinder.Data;

import android.graphics.Bitmap;

import java.util.UUID;

public class ReviewFirebase {
    private String id, pictureUrl, review;
    private int rating;
    private Bitmap picture;

    public ReviewFirebase() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public String getPictureUrl() {
            return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public boolean hasPicture() {
        return picture != null;
    }

    public boolean hasPictureUrl() {
        return pictureUrl != null && !pictureUrl.isEmpty();
    }
}
