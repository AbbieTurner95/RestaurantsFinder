package com.example.abbieturner.restaurantsfinder.Data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserReviews {

    private int reviews_count, reviews_start, reviews_shown;

    @SerializedName("Respond to reviews via Zomato Dashboard")
    private String _$RespondToReviewsViaZomatoDashboard198;
    private List<UserReviewsData> user_reviews;

    public int getReviews_count() {
        return reviews_count;
    }

    public void setReviews_count(int reviews_count) {
        this.reviews_count = reviews_count;
    }

    public int getReviews_start() {
        return reviews_start;
    }

    public void setReviews_start(int reviews_start) {
        this.reviews_start = reviews_start;
    }

    public int getReviews_shown() {
        return reviews_shown;
    }

    public void setReviews_shown(int reviews_shown) {
        this.reviews_shown = reviews_shown;
    }

    public String get_$RespondToReviewsViaZomatoDashboard198() {
        return _$RespondToReviewsViaZomatoDashboard198;
    }

    public void set_$RespondToReviewsViaZomatoDashboard198(String _$RespondToReviewsViaZomatoDashboard198) {
        this._$RespondToReviewsViaZomatoDashboard198 = _$RespondToReviewsViaZomatoDashboard198;
    }

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
            private String review_time_friendly;
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

            public String getReview_time_friendly() {
                return review_time_friendly;
            }

            public void setReview_time_friendly(String review_time_friendly) {
                this.review_time_friendly = review_time_friendly;
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
                private String foodie_level;
                private int foodie_level_num;
                private String foodie_color;
                private String profile_url;
                private String profile_image;
                private String profile_deeplink;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getFoodie_level() {
                    return foodie_level;
                }

                public void setFoodie_level(String foodie_level) {
                    this.foodie_level = foodie_level;
                }

                public int getFoodie_level_num() {
                    return foodie_level_num;
                }

                public void setFoodie_level_num(int foodie_level_num) {
                    this.foodie_level_num = foodie_level_num;
                }

                public String getFoodie_color() {
                    return foodie_color;
                }

                public void setFoodie_color(String foodie_color) {
                    this.foodie_color = foodie_color;
                }

                public String getProfile_url() {
                    return profile_url;
                }

                public void setProfile_url(String profile_url) {
                    this.profile_url = profile_url;
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