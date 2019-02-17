package com.example.abbieturner.restaurantsfinder.Interfaces;

import com.example.abbieturner.restaurantsfinder.Data.UserReviews;

import java.util.List;

public interface ISendReviews {
    void sendReviews(List<UserReviews.UserReviewsData> reviews);
}
