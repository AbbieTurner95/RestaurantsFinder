package com.example.abbieturner.restaurantsfinder.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abbieturner.restaurantsfinder.API.API;
import com.example.abbieturner.restaurantsfinder.Adapters.CuisineJsonAdapter;
import com.example.abbieturner.restaurantsfinder.Adapters.ModelConverter;
import com.example.abbieturner.restaurantsfinder.Adapters.ReviewJsonAdapter;
import com.example.abbieturner.restaurantsfinder.Adapters.ReviewsAdapter;
import com.example.abbieturner.restaurantsfinder.Data.Cuisine;
import com.example.abbieturner.restaurantsfinder.Data.Restaurant;
import com.example.abbieturner.restaurantsfinder.Data.Review;
import com.example.abbieturner.restaurantsfinder.Data.Reviews;
import com.example.abbieturner.restaurantsfinder.Database.AppDatabase;
import com.example.abbieturner.restaurantsfinder.DatabaseModels.DatabaseRestaurant;
import com.example.abbieturner.restaurantsfinder.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.gson.GsonBuilder;

public class RestaurantActivity extends AppCompatActivity implements OnMapReadyCallback, ReviewsAdapter.ReviewItemClick, NavigationView.OnNavigationItemSelectedListener {


    private Gson gson;
    private String jsonRestaurant;
    private Restaurant restaurant;
    private ModelConverter converter;
    private AppDatabase database;
    @BindView(R.id.overall_rating)
    TextView overalRating;
    @BindView(R.id.address_tv_1)
    TextView address;
    @BindView(R.id.has_online_delivery)
    TextView hasOnlineDelivery;
    @BindView(R.id.btn_call)
    ImageView callButton;
    @BindView(R.id.btn_direction)
    ImageView directionButton;
    @BindView(R.id.btn_share)
    ImageView shareButton;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.scrollview_restaurant)
    ScrollView mainScrollView;
    @BindView(R.id.transparent_image)
    ImageView transparentImageView;
    @BindView(R.id.review_slider)
    RecyclerView recyclerView;
    @BindView(R.id.btn_menu)
    LinearLayout btnMenu;
    @BindView(R.id.btn_favourites)
    LinearLayout btnFavourites;
    @BindView(R.id.btn_web)
    LinearLayout btnWeb;
    @BindView(R.id.btn_favourite_icon)
    ImageView favouriteIcon;
    @BindView(R.id.restaurant_name)
    TextView restaurantName;

    private API.ZomatoApiCalls service;
    private ReviewsAdapter reviewsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_bar_rest);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        converter = ModelConverter.getInstance();
        database = AppDatabase.getInstance(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        reviewsAdapter = new ReviewsAdapter(this, this);
        recyclerView.setAdapter(reviewsAdapter);

        gson = new Gson();
        jsonRestaurant = getIntent().getStringExtra(getResources().getString(R.string.TAG_RESTAURANT));
        if (jsonRestaurant != null) {
            restaurant = gson.fromJson(jsonRestaurant, Restaurant.class); // Converts the JSON String to an Object
        }

        if(database.restaurantsDAO().getRestaurant(restaurant.getId()) != null){
            favouriteIcon.setImageResource(R.drawable.ic_favorite_white_24dp);
        }

        toolbar.setTitle(restaurant.getName());
        //this.setTitle(restaurant.getName());

        restaurantName.setText(restaurant.getName());

        overalRating.setText(restaurant.getUser_rating().getAggregate_rating());
        address.setText(restaurant.getLocation().getAddress());

        String onlineDelivery = restaurant.getHas_online_delivery() == 0 ? "NO" : "YES";
        hasOnlineDelivery.setText(onlineDelivery);

        callButton.setOnClickListener(callButtonOnClickListener);
        directionButton.setOnClickListener(directionButtonOnClickListener);
        shareButton.setOnClickListener(shareButtonOnClickListener);

        btnWeb.setOnClickListener(btnWebOnClickListener);
        btnMenu.setOnClickListener(btnMenuOnClickListener);
        btnFavourites.setOnClickListener(btnFavouritesOnClickListener);

        transparentImageView.setOnTouchListener(onTouchListener);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.restaurant_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng location = new LatLng(Double.parseDouble(restaurant.getLocation().getLatitude()), Double.parseDouble(restaurant.getLocation().getLongitude()));

        googleMap.addMarker(new MarkerOptions()
                .position(location)
                .title(restaurant.getLocation().getAddress()));
        float zoomLevel = (float) 15.0;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel));
    }

    private View.OnClickListener directionButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            directionButtonClicked();
        }
    };

    private View.OnClickListener callButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            callButtonClicked();
        }
    };

    private View.OnClickListener shareButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            shareButtonClicked();
        }
    };

    private void directionButtonClicked() {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=53.478908,-1.1863414&daddr=" + restaurant.getLocation().getLatitude() + "," + restaurant.getLocation().getLongitude() + ""));
        startActivity(intent);
    }

    private void callButtonClicked() {
        if (isPermissionGranted()) {
            call_action();
        }
    }

    private void call_action() {
        String phnum = "+447725887680";
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phnum));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(callIntent);
    }

    private boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {

                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    call_action();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void shareButtonClicked() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Hey check out this restaurant I found using the restaurant finder app! Its called - "
                + restaurant.getName() + "Its amazing check out the user rating its : " + restaurant.getUser_rating();
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Restaurant from Restaurant Finder!");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    // Disallow ScrollView to intercept touch events.
                    mainScrollView.requestDisallowInterceptTouchEvent(true);
                    // Disable touch on transparent view
                    return false;

                case MotionEvent.ACTION_UP:
                    // Allow ScrollView to intercept touch events.
                    mainScrollView.requestDisallowInterceptTouchEvent(false);
                    return true;

                case MotionEvent.ACTION_MOVE:
                    mainScrollView.requestDisallowInterceptTouchEvent(true);
                    return false;

                default:
                    return true;
            }
        }
    };

    private void fetchReviews() {
        service.getReviews(restaurant.getId())
                .enqueue(new Callback<Reviews>() {
                    @Override
                    public void onResponse(Call<Reviews> call, Response<Reviews> response) {
                        assert response.body() != null;
                        reviewsAdapter.setReviewsList(response.body().reviewsList);
                    }

                    @Override
                    public void onFailure(Call<Reviews> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
    }

    @Override
    public void onReviewItemClick(Review review) {
        Toast.makeText(this, "Review clicked", Toast.LENGTH_LONG).show();
//        Gson gS = new Gson();
//        String jsonRestaurant = gS.toJson(restaurant); // Converts the object to a JSON String
//
//        Intent i = new Intent(RestaurantsActivity.this, RestaurantActivity.class);
//        i.putExtra(getResources().getString(R.string.TAG_RESTAURANT), jsonRestaurant);
//        startActivity(i);
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

            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Hey check out this cool restaurant finder app!";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Restaurant Finder!");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));

        } else if (id == R.id.nav_contact) {

        } else if (id == R.id.nav_loginout) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private View.OnClickListener btnWebOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent browserIntent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(restaurant.getUrl()));
            startActivity(browserIntent2);
        }
    };
    private View.OnClickListener btnMenuOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(restaurant.getMenu_url()));
            startActivity(browserIntent);
        }
    };
    private View.OnClickListener btnFavouritesOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            toggleFavouriteRestaurant();
        }
    };

    private void toggleFavouriteRestaurant(){
        DatabaseRestaurant convertedRestaurant =  converter.convertToDatabaseRestaurant(restaurant);


        if(database.restaurantsDAO().getRestaurant(restaurant.getId()) != null){
            database.restaurantsDAO().deleteRestaurant(convertedRestaurant);
            Toast.makeText(RestaurantActivity.this, "Restaurant " + restaurant.getName() + " removed from favorite list.", Toast.LENGTH_LONG).show();
            favouriteIcon.setImageResource(R.drawable.ic_favorite_border_white_24dp);
        }else{
            database.restaurantsDAO().insertRestaurant(convertedRestaurant);
            Toast.makeText(RestaurantActivity.this, "Restaurant " + restaurant.getName() + " added to favorite list.", Toast.LENGTH_LONG).show();
            favouriteIcon.setImageResource(R.drawable.ic_favorite_white_24dp);
        }
    }
}