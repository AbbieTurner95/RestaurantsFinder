package com.example.abbieturner.restaurantsfinder.Data;

public class UserRating {

        public String aggregate_rating;
        public String rating_text;
        public String rating_color;
        public String votes;

        public String getAggregate_rating() {
            return aggregate_rating;
        }

        public void setAggregate_rating(String aggregate_rating) {
            this.aggregate_rating = aggregate_rating;
        }

        public String getRating_text() {
            return rating_text;
        }

        public void setRating_text(String rating_text) {
            this.rating_text = rating_text;
        }

        public String getVotes() {
            return votes;
        }

        public void setVotes(String votes) {
            this.votes = votes;
        }

        public String getRating_color() {
            return rating_color;
        }

        public void setRating_color(String rating_color) {
            this.rating_color = rating_color;
        }
    }

