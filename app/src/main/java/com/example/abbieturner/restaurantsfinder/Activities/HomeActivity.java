package com.example.abbieturner.restaurantsfinder.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
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
import android.view.ViewStub;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abbieturner.restaurantsfinder.API.API;
import com.example.abbieturner.restaurantsfinder.Adapters.CuisineJsonAdapter;
import com.example.abbieturner.restaurantsfinder.Adapters.FavouriteAdapter;
import com.example.abbieturner.restaurantsfinder.Adapters.ModelConverter;
import com.example.abbieturner.restaurantsfinder.Data.Cuisine;
import com.example.abbieturner.restaurantsfinder.Data.Cuisines;
import com.example.abbieturner.restaurantsfinder.Data.CuisinesSingleton;
import com.example.abbieturner.restaurantsfinder.Data.Restaurant;
import com.example.abbieturner.restaurantsfinder.Database.AppDatabase;
import com.example.abbieturner.restaurantsfinder.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity implements FavouriteAdapter.RestaurantItemClick, NavigationView.OnNavigationItemSelectedListener{

    @BindView(R.id.home_popular_recycler_view)
    RecyclerView popularRecyclerView;
    @BindView(R.id.home_favourites_recycler_view)
    RecyclerView favouritesRecyclerView;
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

    private LinearLayoutManager popularLayoutManager;
    private LinearLayoutManager favouriteLayoutManager;
    private FavouriteAdapter favouriteAdapter;
    private AppDatabase database;
    private ModelConverter converter;
    private AutoCompleteTextView autoCompleteTextView;
    private Button btnClear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_bar_home);
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

        favouritesRecyclerView = (RecyclerView)findViewById(R.id.home_favourites_recycler_view);
        autoCompleteTextView = findViewById(R.id.autocomplete_cuisines);

        setUpAutocomplete(CuisinesSingleton.getInstance().getCuisines());

        favouriteLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        int layout = R.layout.favourite_restaurant_item;
        favouriteAdapter = new FavouriteAdapter(this, this, layout);
        favouriteAdapter.setCuisineList(favoritesRestaurants);
        favouritesRecyclerView.setLayoutManager(favouriteLayoutManager);
        favouritesRecyclerView.setAdapter(favouriteAdapter);

        allCuisines = (ImageView) findViewById(R.id.btn_all_cuisines);
        allCuisines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CuisineActivity.class);
                startActivity(intent);
            }
        });

        btnClear = findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompleteTextView.setText("");
            }
        });

        btnManageFavourites.setOnClickListener(btnManageFavouritesOnClickListener);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
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
        String jsonRestaurant = gS.toJson(restaurant); // Converts the object to a JSON String

        Intent intent = new Intent(HomeActivity.this, RestaurantActivity.class);
        intent.putExtra(getResources().getString(R.string.TAG_RESTAURANT), jsonRestaurant);
        startActivity(intent);
    }

    private void setUpAutocomplete(List<Cuisine> cuisineList){
        //Cuisine[] cousineArray = new Cuisine[]{new Cuisine(1, "Cuisine1"), new Cuisine(2, "Cuisine2")};

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


