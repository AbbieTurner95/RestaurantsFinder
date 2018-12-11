package com.example.abbieturner.restaurantsfinder.Activities;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

public class FavouritesActivity extends AppCompatActivity implements FavouriteAdapter.RestaurantItemClick, NavigationView.OnNavigationItemSelectedListener{

    @BindView(R.id.favourites_restaurants_recycler_view)
    RecyclerView favouritesRestaurantsRecyclerView;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private AppDatabase database;
    private ModelConverter converter;
    private LinearLayoutManager favouritesLayoutManager;
    private FavouriteAdapter favouritesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drawer_fav);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        database = AppDatabase.getInstance(this);
        converter = ModelConverter.getInstance();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

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


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_fave) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_contact) {

        } else if (id == R.id.nav_loginout) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}