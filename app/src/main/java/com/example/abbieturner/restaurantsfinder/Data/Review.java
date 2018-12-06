package com.example.abbieturner.restaurantsfinder.Data;

public class Review {
    public String rating;
    public String review_text;
    public String id;

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReview_text() {
        return review_text;
    }

    public void setReview_text(String review_text) {
        this.review_text = review_text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRating_color() {
        return rating_color;
    }

    public void setRating_color(String rating_color) {
        this.rating_color = rating_color;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String rating_color;
    public String timestamp;
}
