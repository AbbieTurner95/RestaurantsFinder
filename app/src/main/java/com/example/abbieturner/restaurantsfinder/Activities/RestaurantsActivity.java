package com.example.abbieturner.restaurantsfinder.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
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
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.abbieturner.restaurantsfinder.API.API;
import com.example.abbieturner.restaurantsfinder.Adapters.RestaurantJsonAdapter;
import com.example.abbieturner.restaurantsfinder.Adapters.RestaurantsAdapter;
import com.example.abbieturner.restaurantsfinder.Data.Restaurant;
import com.example.abbieturner.restaurantsfinder.Data.RestaurantModel;
import com.example.abbieturner.restaurantsfinder.Data.Restaurants;
import com.example.abbieturner.restaurantsfinder.Dialogs.RestaurantsFilterDialog;
import com.example.abbieturner.restaurantsfinder.FirebaseAccess.Listeners.RestaurantsListener;
import com.example.abbieturner.restaurantsfinder.R;
import com.example.abbieturner.restaurantsfinder.Singletons.DeviceLocation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RestaurantsActivity extends AppCompatActivity implements
        RestaurantsAdapter.RestaurantItemClick,
        NavigationView.OnNavigationItemSelectedListener,
        RestaurantsListener {

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.restaurants_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.pb_restaurants_loading)
    ProgressBar pbRestaurantsLoading;

    private String name;
    private int cuisineID;
    private RestaurantsAdapter restaurantsAdapter;
    private API.ZomatoApiCalls service;
    private String TAG_RESTAURANT_ID, TAG_IS_FIREBASE_RESTAURANT;
    private DeviceLocation locationSingleton;
    private com.example.abbieturner.restaurantsfinder.FirebaseAccess.Restaurants restaurantsDataAccess;
    private boolean zoomatoRestaurantsLoaded, firebaseRestaurantsLoaded;
    private List<Restaurant> zoomatoRestaurants;
    private List<com.example.abbieturner.restaurantsfinder.FirebaseModels.Restaurant> firebaseRestaurants;
    private String cuisineName;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_bar_rests);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();

        setSupportActionBar(toolbar);
        setUpNavigationDrawer();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        firebaseRestaurants = new ArrayList<>();
        zoomatoRestaurants = new ArrayList<>();
        TAG_RESTAURANT_ID = getResources().getString(R.string.TAG_RESTAURANT_ID);
        TAG_IS_FIREBASE_RESTAURANT = getResources().getString(R.string.TAG_IS_FIREBASE_RESTAURANT);

        navigationView.setNavigationItemSelectedListener(this);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        restaurantsAdapter = new RestaurantsAdapter(this, this);
        recyclerView.setAdapter(restaurantsAdapter);

        locationSingleton = DeviceLocation.getInstance();
        restaurantsDataAccess = new com.example.abbieturner.restaurantsfinder.FirebaseAccess.Restaurants(this);

        Intent intent = getIntent();

        if (intent != null) {
            cuisineID = intent.getIntExtra("cuisine_id", cuisineID);
            name = intent.getStringExtra(getResources().getString(R.string.TAG_CUISINE_NAME));

            toolbar.setTitle(name);
        } else {
            Log.e("ERROR INTENT", "Intent is null!");
        }

//        toolbar.inflateMenu(R.menu.menu_restaurants);
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



        getRestaurants();

    }

    private void getRestaurants(){
        pbRestaurantsLoading.setVisibility(View.VISIBLE);
        zoomatoRestaurantsLoaded = false;
        firebaseRestaurantsLoaded = false;
        fetchRestaurants();

        if(locationSingleton.isLocationSet()){
            restaurantsDataAccess.getRestaurants(name);
        }
    }

    private void fetchRestaurants() {

        service.getRestaurants("5000","1", "20",
                String.valueOf(locationSingleton.getLocation().latitude),
                String.valueOf(locationSingleton.getLocation().longitude),
                cuisineID, "rating", "asc")
                .enqueue(new Callback<Restaurants>() {
                    @Override
                    public void onResponse(Call<Restaurants> call, Response<Restaurants> response) {
                        zoomatoRestaurants.addAll(response.body().restaurantsList);
                        zoomatoRestaurantsLoaded = true;
                        setRestaurants();
                    }

                    @Override
                    public void onFailure(Call<Restaurants> call, Throwable t) {
                        t.printStackTrace();
                        zoomatoRestaurantsLoaded = true;
                        Toast.makeText(RestaurantsActivity.this, "Failed to load Zoomato restaurants!", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void setRestaurants(){
        if(zoomatoRestaurantsLoaded && firebaseRestaurantsLoaded){
            pbRestaurantsLoading.setVisibility(View.GONE);
            restaurantsAdapter.setRestaurantsList(zoomatoRestaurants, firebaseRestaurants);
        }
    }

    @Override
    public void onRestaurantItemClick(RestaurantModel restaurant) {
        Intent intent = new Intent(RestaurantsActivity.this, RestaurantActivity.class);

        if(restaurant.isFirebaseRestaurant()){
            intent.putExtra(TAG_RESTAURANT_ID, restaurant.getFirebaseRestaurant().getId());
            intent.putExtra(TAG_IS_FIREBASE_RESTAURANT, restaurant.isFirebaseRestaurant());
        }else{
            intent.putExtra(TAG_RESTAURANT_ID, restaurant.getZomatoRestaurant().getId());
            intent.putExtra(TAG_IS_FIREBASE_RESTAURANT, restaurant.isFirebaseRestaurant());
        }


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

        getMenuInflater().inflate(R.menu.menu_restaurants, menu);
        return true;
    }

    private void setUpNavigationDrawer(){
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_filter) {
            openFilterDialog();
            return true;
        }

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
            Intent intent = new Intent(this, FavouritesActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Hey check out this cool restaurant finder app!";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Restaurant Finder!");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));

        } else if (id == R.id.nav_contact) {
            new LovelyStandardDialog(this, LovelyStandardDialog.ButtonLayout.VERTICAL)
                    .setTopColorRes(R.color.design_default_color_primary)
                    .setButtonsColorRes(R.color.white)
                    .setIcon(R.drawable.phone_black_24dp)
                    .setTitle("Select a contact method.")
                    .setMessage("How do you wish to contact us?")
                    .setPositiveButton("Email", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                    "mailto", "info@restaurantfinder.com", null));
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                            emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                            startActivity(Intent.createChooser(emailIntent, "Send us an Email"));

                        }
                    })
                    .setNegativeButton("Phone Us", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:01145627382"));
                            startActivity(intent);
                        }
                    })
                    .show();
        } else if (id == R.id.nav_loginout) {
            if(mAuth != null){
                mAuth.signOut();
            } else {
                Toast.makeText(this, "Not Logged In.", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.action_settings) {
            Intent intent = new Intent(RestaurantsActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openFilterDialog() {
        RestaurantsFilterDialog dialog = new RestaurantsFilterDialog();
        Bundle args = new Bundle();
        args.putSerializable("key", restaurantsAdapter);
        dialog.setArguments(args);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        dialog.show(ft, RestaurantsFilterDialog.TAG);


    }


    @Override
    public void onRestaurantsLoaded(boolean hasFailed, List<com.example.abbieturner.restaurantsfinder.FirebaseModels.Restaurant> restaurants) {
        firebaseRestaurantsLoaded = true;
        firebaseRestaurants.clear();
        if(hasFailed){

        }else{
            firebaseRestaurants.addAll(restaurants);
            setRestaurants();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAuth.signOut();
    }
}