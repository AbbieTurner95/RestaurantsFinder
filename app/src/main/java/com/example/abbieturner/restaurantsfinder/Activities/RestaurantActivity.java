package com.example.abbieturner.restaurantsfinder.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abbieturner.restaurantsfinder.Data.Restaurant;
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

import com.google.android.gms.maps.SupportMapFragment;

public class RestaurantActivity extends AppCompatActivity implements OnMapReadyCallback {

    @BindView(R.id.review_slider)
    RecyclerView slider;

    private Gson gson;
    private String jsonRestaurant;
    private Restaurant restaurant;
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

    private ScrollView mainScrollView;
    private ImageView transparentImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        ButterKnife.bind(this);

        mainScrollView = (ScrollView) findViewById(R.id.scrollview_restaurant);
        transparentImageView = (ImageView) findViewById(R.id.transparent_image);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        slider.setLayoutManager(layoutManager);
        //needs adapter

        gson = new Gson();
        jsonRestaurant = getIntent().getStringExtra(getResources().getString(R.string.TAG_RESTAURANT));
        if(jsonRestaurant != null){
            restaurant = gson.fromJson(jsonRestaurant, Restaurant.class); // Converts the JSON String to an Object
        }

        this.setTitle(restaurant.getName());

        overalRating.setText(restaurant.getUser_rating().getAggregate_rating());
        address.setText(restaurant.getLocation().getAddress());

        String onlineDelivery = restaurant.getHas_online_delivery() == 0 ? "NO" : "YES";
        hasOnlineDelivery.setText(onlineDelivery);


        callButton.setOnClickListener(callButtonOnClickListener);
        directionButton.setOnClickListener(directionButtonOnClickListener);
        shareButton.setOnClickListener(shareButtonOnClickListener);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.action_menu:
                        //Toast.makeText(RestaurantActivity.this, restaurant.getMenu_url(), Toast.LENGTH_SHORT).show();
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(restaurant.getMenu_url()));
                        startActivity(browserIntent);
                        break;
                    case R.id.action_favourite:
                        Toast.makeText(RestaurantActivity.this, "Favourite Clicked!", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_web_page:
                        //Toast.makeText(RestaurantActivity.this, restaurant.getUrl(), Toast.LENGTH_SHORT).show();
                        Intent browserIntent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(restaurant.getUrl()));
                        startActivity(browserIntent2);
                        break;
                }
                return true;
            }
        });

        transparentImageView.setOnTouchListener(onTouchListener);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.restaurant_map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap){
        LatLng location = new LatLng(Double.parseDouble(restaurant.getLocation().getLatitude()), Double.parseDouble(restaurant.getLocation().getLongitude()));


        googleMap.addMarker(new MarkerOptions()
                .position(location)
                .title(restaurant.getLocation().getAddress()));
        float zoomLevel = (float) 15.0;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,zoomLevel));
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
        //Toast.makeText(RestaurantActivity.this, "Direction!", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=53.478908,-1.1863414&daddr="+ restaurant.getLocation().getLatitude() +","+ restaurant.getLocation().getLongitude()+""));
        startActivity(intent);
    }

    private void callButtonClicked() {
        //Toast.makeText(RestaurantActivity.this, "Call!", Toast.LENGTH_LONG).show();


        if(isPermissionGranted()){
            call_action();
        }



//        Intent intent = new Intent(Intent.ACTION_CALL);
//        intent.setData(Uri.parse("tel:+447725887680"));
//        this.startActivity(intent);
    }

    private void call_action(){
        String phnum = "+447725887680";
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phnum));
        startActivity(callIntent);
    }

    private  boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted");
                return true;
            } else {

                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted");
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

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void shareButtonClicked() {
        Toast.makeText(RestaurantActivity.this, "Share!", Toast.LENGTH_LONG).show();

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
}
