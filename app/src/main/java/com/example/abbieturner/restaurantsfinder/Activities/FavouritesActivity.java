package com.example.abbieturner.restaurantsfinder.Activities;

import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.abbieturner.restaurantsfinder.Adapters.FavouriteAdapter;
import com.example.abbieturner.restaurantsfinder.Adapters.ModelConverter;
import com.example.abbieturner.restaurantsfinder.Data.Restaurant;
import com.example.abbieturner.restaurantsfinder.Database.AppDatabase;
import com.example.abbieturner.restaurantsfinder.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FavouritesActivity extends AppCompatActivity implements FavouriteAdapter.RestaurantItemClick, NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.favourites_restaurants_recycler_view)
    RecyclerView favouritesRestaurantsRecyclerView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drawer_fav);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setUpNavigationDrawer();
        mAuth = FirebaseAuth.getInstance();

        AppDatabase database = AppDatabase.getInstance(this);
        ModelConverter converter = ModelConverter.getInstance();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        List<Restaurant> favoritesRestaurants = converter.convertToRestaurants(database.restaurantsDAO().getRestaurants());

        LinearLayoutManager favouritesLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        int layout = R.layout.favourite_restaurant_vertical_item;
        FavouriteAdapter favouritesAdapter = new FavouriteAdapter(this, this, layout);
        favouritesAdapter.setCuisineList(favoritesRestaurants);
        favouritesRestaurantsRecyclerView.setLayoutManager(favouritesLayoutManager);
        favouritesRestaurantsRecyclerView.setAdapter(favouritesAdapter);

        setTitle("Favourites Restaurants");
    }

    @Override
    public void onRestaurantItemClick(Restaurant restaurant) {
        Gson gS = new Gson();
        String jsonRestaurant = gS.toJson(restaurant);
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

    private void setUpNavigationDrawer(){
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
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
            if(mAuth != null){
                mAuth.signOut();
            } else {
                Toast.makeText(this, "Not Logged In.", Toast.LENGTH_SHORT).show();
            }
        }else if (id == R.id.action_settings) {
            Intent intent = new Intent(FavouritesActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAuth.signOut();
    }
}