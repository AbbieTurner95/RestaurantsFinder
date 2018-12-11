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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.abbieturner.restaurantsfinder.API.API;
import com.example.abbieturner.restaurantsfinder.Adapters.RestaurantJsonAdapter;
import com.example.abbieturner.restaurantsfinder.Adapters.RestaurantsAdapter;
import com.example.abbieturner.restaurantsfinder.Data.Restaurant;
import com.example.abbieturner.restaurantsfinder.Data.Restaurants;
import com.example.abbieturner.restaurantsfinder.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RestaurantsActivity extends AppCompatActivity implements RestaurantsAdapter.RestaurantItemClick, NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.restaurants_recycler_view)
    RecyclerView recyclerView;

    private String name;
    private int cuisineID;
    private RestaurantsAdapter restaurantsAdapter;
    private API.ZomatoApiCalls service;
    //private static OkHttpClient.Builder builder = new OkHttpClient.Builder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_bar_rests);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        restaurantsAdapter = new RestaurantsAdapter(this, this);
        recyclerView.setAdapter(restaurantsAdapter);

        Intent intent = getIntent();

        if (intent != null) {
            cuisineID = intent.getIntExtra("cuisine_id", cuisineID);
            name = intent.getStringExtra(getResources().getString(R.string.TAG_CUISINE_NAME));
        } else {
            Log.e("ERROR INTENT", "Intent is null!");
        }

        this.setTitle(name);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Restaurant.class, new RestaurantJsonAdapter())
                .create();

        final String BASE_URL = getResources().getString(R.string.BASE_URL_API);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        service = retrofit.create(API.ZomatoApiCalls.class);

        fetchRestaurants();
    }

    private void fetchRestaurants() {

        service.getRestaurants("332", "city", "1", "20",
                "53.382882", "-1.470300", cuisineID, "rating", "asc")
                .enqueue(new Callback<Restaurants>() {
                    @Override
                    public void onResponse(Call<Restaurants> call, Response<Restaurants> response) {
                        restaurantsAdapter.setRestaurantsList(response.body().restaurantsList);
                    }

                    @Override
                    public void onFailure(Call<Restaurants> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
    }

    @Override
    public void onRestaurantItemClick(Restaurant restaurant) {
        Gson gS = new Gson();
        String jsonRestaurant = gS.toJson(restaurant);
        Intent i = new Intent(RestaurantsActivity.this, RestaurantActivity.class);
        i.putExtra(getResources().getString(R.string.TAG_RESTAURANT), jsonRestaurant);
        startActivity(i);
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