package com.example.abbieturner.restaurantsfinder.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.abbieturner.restaurantsfinder.API.API;
import com.example.abbieturner.restaurantsfinder.Adapters.CuisineJsonAdapter;
import com.example.abbieturner.restaurantsfinder.Adapters.RestaurantJsonAdapter;
import com.example.abbieturner.restaurantsfinder.Adapters.RestaurantsAdapter;
import com.example.abbieturner.restaurantsfinder.Data.Cuisine;
import com.example.abbieturner.restaurantsfinder.Data.Restaurant;
import com.example.abbieturner.restaurantsfinder.Data.Restaurants;
import com.example.abbieturner.restaurantsfinder.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RestaurantsActivity extends AppCompatActivity implements RestaurantsAdapter.RestaurantItemClick {

    private String name;
    private int cuisineID;
    RecyclerView recyclerView;
    private RestaurantsAdapter restaurantsAdapter;
    private API.ZomatoApiCalls service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);

        recyclerView = findViewById(R.id.restaurants_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        restaurantsAdapter = new RestaurantsAdapter(this, this);
        recyclerView.setAdapter(restaurantsAdapter);

        Intent intent = getIntent();

        if(intent != null){
            cuisineID = intent.getIntExtra("cuisine_id", cuisineID);
            name = intent.getStringExtra(getResources().getString(R.string.TAG_CUISINE_NAME));
        } else {
            Log.e("ERROR INTENT", "Intent is null!");
        }

        this.setTitle(name);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Cuisine.class, new RestaurantJsonAdapter())
                .create();

        final String BASE_URL = getResources().getString(R.string.BASE_URL_API);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        service = retrofit.create(API.ZomatoApiCalls.class);

        fetchRestaurants();
    }

    private void fetchRestaurants(){

        service.getRestaurants("332", "city", "1", "20",
                "53.382882", "-1.470300", "rating", cuisineID, "asc")
                .enqueue(new Callback<Restaurants>() {
                    @Override
                    public void onResponse(Call<Restaurants> call, Response<Restaurants> response) {
                       //null?
                        restaurantsAdapter.setRestaurantsList(response.body().restaurantList);
                    }

                    @Override
                    public void onFailure(Call<Restaurants> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
        }

    @Override
    public void onRestaurantItemClick(Restaurant restaurant){
        Toast.makeText(this, restaurant.getName(), Toast.LENGTH_LONG).show();
    }
}
