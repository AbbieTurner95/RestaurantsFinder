package com.example.abbieturner.restaurantsfinder.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.abbieturner.restaurantsfinder.Adapters.FavouriteAdapter;
import com.example.abbieturner.restaurantsfinder.Adapters.ModelConverter;
import com.example.abbieturner.restaurantsfinder.Data.Restaurant;
import com.example.abbieturner.restaurantsfinder.Database.AppDatabase;
import com.example.abbieturner.restaurantsfinder.R;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavouritesActivity extends AppCompatActivity implements FavouriteAdapter.RestaurantItemClick{

    @BindView(R.id.favourites_restaurants_recycler_view)
    RecyclerView favouritesRestaurantsRecyclerView;

    private AppDatabase database;
    private ModelConverter converter;
    private LinearLayoutManager favouritesLayoutManager;
    private FavouriteAdapter favouritesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        ButterKnife.bind(this);

        database = AppDatabase.getInstance(this);
        converter = ModelConverter.getInstance();


        List<Restaurant> favoritesRestaurants = converter.convertToRestaurants(database.restaurantsDAO().getRestaurants());

        favouritesLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        int layout = R.layout.favourite_restaurant_vertical_item;
        favouritesAdapter = new FavouriteAdapter(this, this, layout);
        favouritesAdapter.setCuisineList(favoritesRestaurants);
        favouritesRestaurantsRecyclerView.setLayoutManager(favouritesLayoutManager);
        favouritesRestaurantsRecyclerView.setAdapter(favouritesAdapter);

        setTitle("Favourites Restaurants");
    }

    @Override
    public void onRestaurantItemClick(Restaurant restaurant) {

        Gson gS = new Gson();
        String jsonRestaurant = gS.toJson(restaurant); // Converts the object to a JSON String

        Intent intent = new Intent(FavouritesActivity.this, RestaurantActivity.class);
        intent.putExtra(getResources().getString(R.string.TAG_RESTAURANT), jsonRestaurant);
        startActivity(intent);
    }
}
