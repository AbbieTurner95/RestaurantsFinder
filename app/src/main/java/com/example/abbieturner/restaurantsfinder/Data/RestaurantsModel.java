package com.example.abbieturner.restaurantsfinder.Data;

import java.util.List;

public class RestaurantsModel {

    private int results_found;
    private int results_start;
    private int results_shown;
    private List<RestaurantsData> restaurants;

    public int getResults_found() {
        return results_found;
    }

    public void setResults_found(int results_found) {
        this.results_found = results_found;
    }

    public int getResults_start() {
        return results_start;
    }

    public void setResults_start(int results_start) {
        this.results_start = results_start;
    }

    public int getResults_shown() {
        return results_shown;
    }

    public void setResults_shown(int results_shown) {
        this.results_shown = results_shown;
    }

    public List<RestaurantsData> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<RestaurantsData> restaurants) {
        this.restaurants = restaurants;
    }










    public static class RestaurantsData {

        private RestaurantData restaurant;

        public RestaurantData getRestaurant() {
            return restaurant;
        }

        public void setRestaurant(RestaurantData restaurant) {
            this.restaurant = restaurant;
        }

        public static class RestaurantData {
            private String id;
            private String name;
            private String url;
            private LocationData location;
            private String cuisines;
            private int average_cost_for_two;
            private int price_range;
            private String currency;
            private UserRatingData user_rating;
            private String photos_url;
            private String menu_url;
            private String featured_image;
            private int has_online_delivery;


            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public LocationData getLocation() {
                return location;
            }

            public void setLocation(LocationData location) {
                this.location = location;
            }

            public String getCuisines() {
                return cuisines;
            }

            public void setCuisines(String cuisines) {
                this.cuisines = cuisines;
            }

            public int getAverage_cost_for_two() {
                return average_cost_for_two;
            }

            public void setAverage_cost_for_two(int average_cost_for_two) {
                this.average_cost_for_two = average_cost_for_two;
            }

            public int getPrice_range() {
                return price_range;
            }

            public void setPrice_range(int price_range) {
                this.price_range = price_range;
            }

            public String getCurrency() {
                return currency;
            }

            public void setCurrency(String currency) {
                this.currency = currency;
            }

            public UserRatingData getUser_rating() {
                return user_rating;
            }

            public void setUser_rating(UserRatingData user_rating) {
                this.user_rating = user_rating;
            }

            public String getPhotos_url() {
                return photos_url;
            }

            public void setPhotos_url(String photos_url) {
                this.photos_url = photos_url;
            }

            public String getMenu_url() {
                return menu_url;
            }

            public void setMenu_url(String menu_url) {
                this.menu_url = menu_url;
            }

            public String getFeatured_image() {
                return featured_image;
            }

            public void setFeatured_image(String featured_image) {
                this.featured_image = featured_image;
            }

            public int getHas_online_delivery() {
                return has_online_delivery;
            }

            public void setHas_online_delivery(int has_online_delivery) {
                this.has_online_delivery = has_online_delivery;
            }











            public static class LocationData {

                private String address;
                private String locality;
                private String city;
                private int city_id;
                private String latitude;
                private String longitude;
                private String zipcode;
                private int country_id;
                private String locality_verbose;

                public String getAddress() {
                    return address;
                }

                public void setAddress(String address) {
                    this.address = address;
                }

                public String getLocality() {
                    return locality;
                }

                public void setLocality(String locality) {
                    this.locality = locality;
                }

                public String getCity() {
                    return city;
                }

                public void setCity(String city) {
                    this.city = city;
                }

                public int getCity_id() {
                    return city_id;
                }

                public void setCity_id(int city_id) {
                    this.city_id = city_id;
                }

                public String getLatitude() {
                    return latitude;
                }

                public void setLatitude(String latitude) {
                    this.latitude = latitude;
                }

                public String getLongitude() {
                    return longitude;
                }

                public void setLongitude(String longitude) {
                    this.longitude = longitude;
                }

                public String getZipcode() {
                    return zipcode;
                }

                public void setZipcode(String zipcode) {
                    this.zipcode = zipcode;
                }

                public int getCountry_id() {
                    return country_id;
                }

                public void setCountry_id(int country_id) {
                    this.country_id = country_id;
                }

                public String getLocality_verbose() {
                    return locality_verbose;
                }

                public void setLocality_verbose(String locality_verbose) {
                    this.locality_verbose = locality_verbose;
                }
            }













            public static class UserRatingData {

                private String aggregate_rating;
                private String rating_text;
                private String rating_color;
                private String votes;

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

                public String getRating_color() {
                    return rating_color;
                }

                public void setRating_color(String rating_color) {
                    this.rating_color = rating_color;
                }

                public String getVotes() {
                    return votes;
                }

                public void setVotes(String votes) {
                    this.votes = votes;
                }
            }
        }
    }
}
