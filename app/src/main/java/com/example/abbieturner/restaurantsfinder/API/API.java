package com.example.abbieturner.restaurantsfinder.API;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

import com.example.abbieturner.restaurantsfinder.Data.Cuisines;
import com.example.abbieturner.restaurantsfinder.Data.Restaurants;
import com.example.abbieturner.restaurantsfinder.Data.Reviews;
import com.example.abbieturner.restaurantsfinder.Data.UserReviews;

public class API {
    public interface ZomatoApiCalls {

        @Headers("user-key: b48385ca8e173d7176550e050eae5fe9")
        @GET("api/v2.1/search")
        Call<Restaurants> getRestaurants(@Query("entity_id") String entity_id,
                                         @Query("entity_type") String entity_type,
                                         @Query("start") String start,
                                         @Query("count") String count,
                                         @Query("lat") String lat,
                                         @Query("lon") String lon,
                                         @Query("cuisines") int cuisine,
                                         @Query("sort") String sort,
                                         @Query("order") String order);


        @Headers("user-key: b48385ca8e173d7176550e050eae5fe9")
        @GET("api/v2.1/cuisines")
        Call<Cuisines> getCuisineId(@Query("city_id") String city_id,
                                    @Query("lat") String lat,
                                    @Query("lon") String lon);

        @Headers("user-key: b48385ca8e173d7176550e050eae5fe9")
        @GET("api/v2.1/reviews")
        Call<UserReviews> getReviews(@Query("res_id") String res_id);


    }
}
