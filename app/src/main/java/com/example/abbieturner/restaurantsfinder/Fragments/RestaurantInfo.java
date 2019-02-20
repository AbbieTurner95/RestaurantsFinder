package com.example.abbieturner.restaurantsfinder.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abbieturner.restaurantsfinder.Activities.RestaurantActivity;
import com.example.abbieturner.restaurantsfinder.Adapters.ModelConverter;
import com.example.abbieturner.restaurantsfinder.Data.Restaurant;
import com.example.abbieturner.restaurantsfinder.Database.AppDatabase;
import com.example.abbieturner.restaurantsfinder.DatabaseModels.DatabaseRestaurant;
import com.example.abbieturner.restaurantsfinder.FirebaseAccess.PopularRestaurants;
import com.example.abbieturner.restaurantsfinder.FirebaseModels.PopularRestaurant;
import com.example.abbieturner.restaurantsfinder.Interfaces.ISendRestaurant;
import com.example.abbieturner.restaurantsfinder.R;

import java.util.List;

public class RestaurantInfo extends Fragment implements ISendRestaurant, PopularRestaurants.PopularRestaurantsListener {

    private Restaurant restaurant;
    private TextView tvAddress, tvRating, tvHasOnlineDelivery, tvPhone;
    private View view;
    private ImageView btnDirection, btnPhone, btnShare, btnFavourites, btnMenu, btnWeb;
    private AppDatabase database;
    private ModelConverter converter;
    private PopularRestaurants popularRestaurantDataAccess;

    public RestaurantInfo(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_restaurant_info, container, false);

        tvAddress = (TextView) view.findViewById(R.id.tv_address);
        tvRating = (TextView) view.findViewById(R.id.tv_rating);
        tvHasOnlineDelivery = (TextView) view.findViewById(R.id.tv_has_online_delivery);
        tvPhone = (TextView) view.findViewById(R.id.tv_phone);
        btnDirection = (ImageView) view.findViewById(R.id.btn_direction);
        btnPhone = (ImageView) view.findViewById(R.id.btn_phone);
        btnShare = (ImageView) view.findViewById(R.id.btn_share);
        btnFavourites = (ImageView) view.findViewById(R.id.btn_favourites);
        btnMenu = (ImageView) view.findViewById(R.id.btn_menu);
        btnWeb = (ImageView) view.findViewById(R.id.btn_web);
        database = AppDatabase.getInstance(getActivity());
        converter = ModelConverter.getInstance();
        popularRestaurantDataAccess = new PopularRestaurants(this);

        setListeners();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((RestaurantActivity)getActivity()).restaurantInfoLoaded();
    }

    @Override
    public void sendRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
        displayRestaurantData();
    }

    private void setListeners(){
        btnDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                directionButtonClicked();
            }
        });
        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callButtonClicked();
            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareButtonClicked();
            }
        });
        btnFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFavouriteRestaurant();
            }
        });
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(restaurant.getMenu_url()));
                startActivity(browserIntent);
            }
        });
        btnWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(restaurant.getUrl()));
                startActivity(browserIntent2);
            }
        });
    }

    private void displayRestaurantData(){
        tvRating.setText(restaurant.getUser_rating().getAggregate_rating());
        tvAddress.setText(restaurant.getLocation().getAddress());
        tvPhone.setText("No number");
        String onlineDelivery = restaurant.getHas_online_delivery() == 0 ? "Delivery not available" : "Delivery available";
        tvHasOnlineDelivery.setText(onlineDelivery);

        if(database.restaurantsDAO().getRestaurant(restaurant.getId()) != null){
            btnFavourites.setImageResource(R.drawable.ic_favorite_black_24dp);
        }else{
            btnFavourites.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
    }

    private void shareButtonClicked() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Hey check out this restaurant I found using the restaurant finder app! Its called - "
                + restaurant.getName() + "Its amazing check out the user rating its : " + restaurant.getUser_rating().getAggregate_rating();
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Restaurant from Restaurant Finder!");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
    private void directionButtonClicked() {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=53.478908,-1.1863414&daddr=" + restaurant.getLocation().getLatitude() + "," + restaurant.getLocation().getLongitude() + ""));
        startActivity(intent);
    }

    private void callButtonClicked() {
//        if (isPermissionGranted()) {
//            call_action();
//        }
    }

    private void toggleFavouriteRestaurant(){
        DatabaseRestaurant convertedRestaurant =  converter.convertToDatabaseRestaurant(restaurant);

        if(database.restaurantsDAO().getRestaurant(restaurant.getId()) != null){
            popularRestaurantDataAccess.removePopularRestaurant(convertedRestaurant.getId());
            database.restaurantsDAO().deleteRestaurant(convertedRestaurant);
            Toast.makeText(getActivity(), "Restaurant " + restaurant.getName() + " removed from favorite list.", Toast.LENGTH_LONG).show();
            btnFavourites.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }else{
            popularRestaurantDataAccess.upsertPopularRestaurant(convertedRestaurant.getId(), convertedRestaurant.getName());
            database.restaurantsDAO().insertRestaurant(convertedRestaurant);
            Toast.makeText(getActivity(), "Restaurant " + restaurant.getName() + " added to favorite list.", Toast.LENGTH_LONG).show();
            btnFavourites.setImageResource(R.drawable.ic_favorite_black_24dp);
        }
    }

    @Override
    public void onRestaurantsLoaded(List<PopularRestaurant> list, boolean hasFailed) {

    }

    @Override
    public void onRestaurantUpdated() {

    }
}
