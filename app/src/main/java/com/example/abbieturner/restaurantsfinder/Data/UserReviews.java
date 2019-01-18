package com.example.abbieturner.restaurantsfinder.Data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserReviews {
    @SerializedName("Respond to reviews via Zomato Dashboard")
    private List<UserReviewsData> user_reviews;


    public List<UserReviewsData> getUser_reviews() {
        return user_reviews;
    }

    public void setUser_reviews(List<UserReviewsData> user_reviews) {
        this.user_reviews = user_reviews;
    }

    public static class UserReviewsData {

        private ReviewData review;

        public ReviewData getReview() {
            return review;
        }

        public void setReview(ReviewData review) {
            this.review = review;
        }

        public static class ReviewData {
            private int rating;
            private String review_text;
            private int id;
            private String rating_color;
            private String rating_text;
            private int timestamp;
            private int likes;
            private UserData user;
            private int comments_count;

            public int getRating() {
                return rating;
            }

            public void setRating(int rating) {
                this.rating = rating;
            }

            public String getReview_text() {
                return review_text;
            }

            public void setReview_text(String review_text) {
                this.review_text = review_text;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getRating_color() {
                return rating_color;
            }

            public void setRating_color(String rating_color) {
                this.rating_color = rating_color;
            }

            public String getRating_text() {
                return rating_text;
            }

            public void setRating_text(String rating_text) {
                this.rating_text = rating_text;
            }

            public int getTimestamp() {
                return timestamp;
            }

            public void setTimestamp(int timestamp) {
                this.timestamp = timestamp;
            }

            public int getLikes() {
                return likes;
            }

            public void setLikes(int likes) {
                this.likes = likes;
            }

            public UserData getUser() {
                return user;
            }

            public void setUser(UserData user) {
                this.user = user;
            }

            public int getComments_count() {
                return comments_count;
            }

            public void setComments_count(int comments_count) {
                this.comments_count = comments_count;
            }

            public static class UserData {

                private String name;
                private String profile_image;
                private String profile_deeplink;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getProfile_image() {
                    return profile_image;
                }

                public void setProfile_image(String profile_image) {
                    this.profile_image = profile_image;
                }

                public String getProfile_deeplink() {
                    return profile_deeplink;
                }

                public void setProfile_deeplink(String profile_deeplink) {
                    this.profile_deeplink = profile_deeplink;
                }
            }
        }
    }
}

