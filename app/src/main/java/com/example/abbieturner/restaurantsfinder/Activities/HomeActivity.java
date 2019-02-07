package com.example.abbieturner.restaurantsfinder.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.abbieturner.restaurantsfinder.Adapters.EmptyRecyclerView;
import com.example.abbieturner.restaurantsfinder.Adapters.FavouriteAdapter;
import com.example.abbieturner.restaurantsfinder.Adapters.ModelConverter;
import com.example.abbieturner.restaurantsfinder.Adapters.PopularRestaurantsAdapter;
import com.example.abbieturner.restaurantsfinder.Data.Cuisine;
import com.example.abbieturner.restaurantsfinder.Data.CuisinesSingleton;
import com.example.abbieturner.restaurantsfinder.Data.Restaurant;
import com.example.abbieturner.restaurantsfinder.Database.AppDatabase;
import com.example.abbieturner.restaurantsfinder.FirebaseAccess.PopularRestaurants;
import com.example.abbieturner.restaurantsfinder.FirebaseModels.PopularRestaurant;
import com.example.abbieturner.restaurantsfinder.R;
import com.google.gson.Gson;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeActivity extends AppCompatActivity
                        implements
                                FavouriteAdapter.RestaurantItemClick,
                                NavigationView.OnNavigationItemSelectedListener,
                                PopularRestaurants.PopularRestaurantsListener,
                                PopularRestaurantsAdapter.RestaurantItemClick{

    @BindView(R.id.home_popular_recycler_view)
    EmptyRecyclerView popularRecyclerView;
    @BindView(R.id.home_favourites_recycler_view)
    EmptyRecyclerView favouritesRecyclerView;
    @BindView(R.id.btn_all_cuisines)
    ImageView allCuisines;
    @BindView(R.id.btn_manage_favourites)
    Button btnManageFavourites;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.autocomplete_cuisines)
    AutoCompleteTextView autoCompleteTextView;
    @BindView(R.id.btn_clear)
    Button btnClear;
    @BindView(R.id.pb_popular_restaurants)
    ProgressBar pbPopularRestaurants;



    private List<Restaurant> favoritesRestaurants;
    private FavouriteAdapter favouriteAdapter;
    private PopularRestaurantsAdapter popularAdapter;
    private LinearLayoutManager favouriteLayoutManager, popularLayoutManager;
    private ModelConverter converter;
    private AppDatabase database;
    private PopularRestaurants popularRestaurantsDataAccess;
    private String TAG_RESTAURANT_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_bar_home);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        database = AppDatabase.getInstance(this);
        converter = ModelConverter.getInstance();
        TAG_RESTAURANT_ID = getResources().getString(R.string.TAG_RESTAURANT_ID);

        popularRestaurantsDataAccess = new PopularRestaurants(this);

        setUpNavigationDrawer();

        favoritesRestaurants = getFavouriteRestaurants();

        setUpAutocomplete(CuisinesSingleton.getInstance().getCuisines());
        setUpPopularRecyclerView();
        setUpFavouritesRecyclerView();
        setUpOnClickListeners();
    }

    private void setUpNavigationDrawer(){
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }
    private void setUpOnClickListeners(){
        allCuisines.setOnClickListener(allCuisinesOnClickListener);
        btnClear.setOnClickListener(btnClearOnClickListener);
        btnManageFavourites.setOnClickListener(btnManageFavouritesOnClickListener);
    }
    private List<Restaurant> getFavouriteRestaurants(){
        return converter.convertToRestaurants(database.restaurantsDAO().getRestaurants());
    }
    private void setUpFavouritesRecyclerView(){
        favouriteLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        favouriteAdapter = new FavouriteAdapter(this, this, R.layout.favourite_restaurant_item);
        favouriteAdapter.setCuisineList(favoritesRestaurants);
        favouritesRecyclerView.setLayoutManager(favouriteLayoutManager);

        View favouritesEmptyView = findViewById(R.id.favourites_empty_view);
        favouritesRecyclerView.setEmptyView(favouritesEmptyView);
        favouritesRecyclerView.setAdapter(favouriteAdapter);
    }
    private void setUpPopularRecyclerView(){
        popularLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        popularAdapter = new PopularRestaurantsAdapter(this);
        popularAdapter.setList(new ArrayList<PopularRestaurant>());
        popularRecyclerView.setLayoutManager(popularLayoutManager);

        View popularEmptyView = findViewById(R.id.popular_empty_view);
        popularRecyclerView.setEmptyView(popularEmptyView);
        popularRecyclerView.setAdapter(popularAdapter);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    Log.d("focus", "touchevent");
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onRestaurantItemClick(Restaurant restaurant) {

        Gson gS = new Gson();
        String jsonRestaurant = gS.toJson(restaurant);

        Intent intent = new Intent(HomeActivity.this, RestaurantActivity.class);
        intent.putExtra(getResources().getString(R.string.TAG_RESTAURANT), jsonRestaurant);
        startActivity(intent);
    }

    private void setUpAutocomplete(List<Cuisine> cuisineList) {
        ArrayAdapter<Cuisine> adapter =
                new ArrayAdapter<Cuisine>(this, android.R.layout.simple_list_item_1, cuisineList);
        autoCompleteTextView.setAdapter(adapter);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Cuisine selected = (Cuisine) arg0.getAdapter().getItem(arg2);

                Intent intent = new Intent(HomeActivity.this, RestaurantsActivity.class);
                intent.putExtra("cuisine_id", selected.getCuisine_id());
                intent.putExtra(getResources().getString(R.string.TAG_CUISINE_NAME), selected.getCuisine_name());
                startActivity(intent);
            }
        });
    }

    private View.OnClickListener btnManageFavouritesOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(HomeActivity.this, FavouritesActivity.class);
            startActivity(intent);
        }
    };

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onResume(){
        super.onResume();

        favoritesRestaurants = getFavouriteRestaurants();
        favouriteAdapter.setCuisineList(favoritesRestaurants);

        getPopularRestaurants();
    }

    private void getPopularRestaurants(){
        pbPopularRestaurants.setVisibility(View.VISIBLE);
        popularRestaurantsDataAccess.getPopularRestaurants();
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
                                    "mailto","info@restaurantfinder.com", null));
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

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private View.OnClickListener allCuisinesOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(HomeActivity.this, CuisineActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener btnClearOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            autoCompleteTextView.setText("");
        }
    };

    @Override
    public void onRestaurantsLoaded(List<PopularRestaurant> list, boolean hasFailed) {
        pbPopularRestaurants.setVisibility(View.GONE);
        if(hasFailed){
            String s = null;
        }else{
            popularAdapter.setList(list);
        }
    }

    @Override
    public void onRestaurantItemClick(PopularRestaurant restaurant) {
        Intent intent = new Intent(HomeActivity.this, RestaurantActivity.class);

        intent.putExtra(TAG_RESTAURANT_ID, restaurant.getRestaurantId());
        startActivity(intent);
    }
}