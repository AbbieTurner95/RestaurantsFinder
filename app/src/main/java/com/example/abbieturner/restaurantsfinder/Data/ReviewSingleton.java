package com.example.abbieturner.restaurantsfinder.Data;

public class ReviewSingleton {
    private static ReviewSingleton theSingleton = null;
    private ReviewFirebase review;

    private ReviewSingleton() {
        review = new ReviewFirebase();
    }

    public static ReviewSingleton getInstance() {
        if (theSingleton == null) {
            theSingleton = new ReviewSingleton();
        }
        return theSingleton;
    }

    public ReviewFirebase getReview() {
        return this.review;
    }

    public void clearReview() {
        this.review = new ReviewFirebase();
    }
}
