package com.example.abbieturner.restaurantsfinder.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abbieturner.restaurantsfinder.Activities.RestaurantActivity;
import com.example.abbieturner.restaurantsfinder.Adapters.ModelConverter;
import com.example.abbieturner.restaurantsfinder.Data.RestaurantModel;
import com.example.abbieturner.restaurantsfinder.Database.AppDatabase;
import com.example.abbieturner.restaurantsfinder.DatabaseModels.DatabaseRestaurant;
import com.example.abbieturner.restaurantsfinder.Dialogs.RecommendRestaurantDialog;
import com.example.abbieturner.restaurantsfinder.FirebaseAccess.PopularRestaurants;
import com.example.abbieturner.restaurantsfinder.FirebaseModels.PopularRestaurant;
import com.example.abbieturner.restaurantsfinder.Interfaces.ISendRestaurant;
import com.example.abbieturner.restaurantsfinder.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class RestaurantInfo extends Fragment implements ISendRestaurant, PopularRestaurants.PopularRestaurantsListener {

    private RestaurantModel restaurant;
    private TextView tvAddress, tvRating, tvHasOnlineDelivery, tvPhone, tvStepFreeAccess, tvAccessibleToilets, tvVegan, tvVegetarian, tvGlutenFree, tvDairyFree;
    private View view;
    private ImageView btnDirection, btnPhone, btnShare, btnFavourites, btnMenu, btnWeb, btnRecommend;
    private AppDatabase database;
    private ModelConverter converter;
    private PopularRestaurants popularRestaurantDataAccess;
    private String[] PERMISSION = {
            Manifest.permission.CALL_PHONE
    };
    private RecommendRestaurantDialog recommendedDialog;
    private FirebaseAuth mAuth;

    public RestaurantInfo() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_restaurant_info, container, false);

        tvAddress = view.findViewById(R.id.tv_address);
        tvRating = view.findViewById(R.id.tv_rating);
        tvHasOnlineDelivery = view.findViewById(R.id.tv_has_online_delivery);
        tvPhone = view.findViewById(R.id.tv_phone);
        btnDirection = view.findViewById(R.id.btn_direction);
        btnPhone = view.findViewById(R.id.btn_phone);
        btnShare = view.findViewById(R.id.btn_share);
        btnFavourites = view.findViewById(R.id.btn_favourites);
        btnMenu = view.findViewById(R.id.btn_menu);
        btnWeb = view.findViewById(R.id.btn_web);
        btnRecommend = view.findViewById(R.id.btn_recommend);
        tvStepFreeAccess = view.findViewById(R.id.tv_step_free_access);
        tvAccessibleToilets = view.findViewById(R.id.tv_accessible_toilets);
        tvVegan = view.findViewById(R.id.tv_vegan);
        tvVegetarian = view.findViewById(R.id.tv_vegetarian);
        tvGlutenFree = view.findViewById(R.id.tv_gluten_free);
        tvDairyFree = view.findViewById(R.id.tv_dairy_free);
        database = AppDatabase.getInstance(getActivity());
        converter = ModelConverter.getInstance();
        popularRestaurantDataAccess = new PopularRestaurants(this);
        mAuth = FirebaseAuth.getInstance();
        recommendedDialog = new RecommendRestaurantDialog(getContext(), mAuth.getUid());


        setListeners();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((RestaurantActivity) getActivity()).restaurantInfoLoaded();
    }


    private void setListeners() {
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
        btnRecommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUserLoggedIn()) {
                    openRecommendDialog();
                } else {
                    Toast.makeText(getActivity(), "Login reqired", Toast.LENGTH_LONG).show();
                }
            }
        });
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (restaurant.isMenuSet()) {
                    Intent menuIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(restaurant.getMenuUrl()));
                    startActivity(menuIntent);
                } else {
                    Toast.makeText(getActivity(), "Menu not set", Toast.LENGTH_LONG).show();
                }
            }
        });
        btnWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (restaurant.isWebUrlSet()) {
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(restaurant.getWebUrl()));
                    startActivity(webIntent);
                } else {
                    Toast.makeText(getActivity(), "Web address not set", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean isUserLoggedIn() {
        return mAuth.getCurrentUser() != null;
    }

    private void displayRestaurantData() {
        if (restaurant.isFirebaseRestaurant()) {
            tvRating.setText(restaurant.getFirebaseRestaurant().getRating().toString());
            tvAddress.setText(restaurant.getFirebaseRestaurant().getAddress());
            tvPhone.setText("No number");
            String onlineDelivery = !restaurant.getFirebaseRestaurant().getDelivery() ? "Delivery not available" : "Delivery available";
            tvHasOnlineDelivery.setText(onlineDelivery);

            if (database.restaurantsDAO().getRestaurant(restaurant.getFirebaseRestaurant().getId()) != null) {
                btnFavourites.setImageResource(R.drawable.ic_favorite_black_24dp);
            } else {
                btnFavourites.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            }
        } else {
            tvRating.setText(restaurant.getZomatoRestaurant().getUser_rating().getAggregate_rating());
            tvAddress.setText(restaurant.getZomatoRestaurant().getLocation().getAddress());
            tvPhone.setText("No number");
            String onlineDelivery = restaurant.getZomatoRestaurant().getHas_online_delivery() == 0 ? "Delivery not available" : "Delivery available";
            tvHasOnlineDelivery.setText(onlineDelivery);

            if (database.restaurantsDAO().getRestaurant(restaurant.getZomatoRestaurant().getId()) != null) {
                btnFavourites.setImageResource(R.drawable.ic_favorite_black_24dp);
            } else {
                btnFavourites.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            }
        }


        tvStepFreeAccess.setText(restaurant.getStepFreeAccess());
        tvAccessibleToilets.setText(restaurant.getAccessibleToilets());
        tvVegan.setText(restaurant.getVegan());
        tvVegetarian.setText(restaurant.getVegetarian());
        tvGlutenFree.setText(restaurant.getGlutenFree());
        tvDairyFree.setText(restaurant.getDairyFree());

    }

    private void shareButtonClicked() {
        if (restaurant.isFirebaseRestaurant()) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Hey check out this restaurant I found using the restaurant finder app! Its called - "
                    + restaurant.getFirebaseRestaurant().getName() + "Its amazing check out the user rating its : " + restaurant.getFirebaseRestaurant().getRating();
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Restaurant from Restaurant Finder!");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        } else {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Hey check out this restaurant I found using the restaurant finder app! Its called - "
                    + restaurant.getZomatoRestaurant().getName() + "Its amazing check out the user rating its : " + restaurant.getZomatoRestaurant().getUser_rating().getAggregate_rating();
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Restaurant from Restaurant Finder!");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }

    }

    private void directionButtonClicked() {
        if (restaurant.isFirebaseRestaurant()) {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?saddr=53.478908,-1.1863414&daddr=" +
                            restaurant.getFirebaseRestaurant().getLat() + "," +
                            restaurant.getFirebaseRestaurant().getLng() + ""));
            startActivity(intent);
        } else {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?saddr=53.478908,-1.1863414&daddr=" +
                            restaurant.getZomatoRestaurant().getLocation().getLatitude() + "," +
                            restaurant.getZomatoRestaurant().getLocation().getLongitude() + ""));
            startActivity(intent);
        }
    }

    private void callButtonClicked() {
        String phoneNumber = restaurant.getPhoneNumber();

        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            if (checkPermission(Manifest.permission.CALL_PHONE)) {
                String dial = "tel:" + phoneNumber;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            } else {
                requestCallPermission();
            }
        } else {
            Toast.makeText(getContext(), "Phone number not set", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(getContext(), permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCallPermission() {
        ActivityCompat.requestPermissions(getActivity(), PERMISSION, 1);
    }

    private void toggleFavouriteRestaurant() {
        if (restaurant.isFirebaseRestaurant()) {
            DatabaseRestaurant convertedRestaurant = converter.convertToDatabaseRestaurant(restaurant.getFirebaseRestaurant());

            if (database.restaurantsDAO().getRestaurant(restaurant.getFirebaseRestaurant().getId()) != null) {
                popularRestaurantDataAccess.removePopularRestaurant(convertedRestaurant.getId());
                database.restaurantsDAO().deleteRestaurant(convertedRestaurant);
                Toast.makeText(getActivity(), "Restaurant " + restaurant.getFirebaseRestaurant().getName() + " removed from favorite list.", Toast.LENGTH_LONG).show();
                btnFavourites.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            } else {
                popularRestaurantDataAccess.upsertPopularRestaurant(convertedRestaurant.getId(), convertedRestaurant.getName(), convertedRestaurant.getPhotos_url());
                database.restaurantsDAO().insertRestaurant(convertedRestaurant);
                Toast.makeText(getActivity(), "Restaurant " + restaurant.getFirebaseRestaurant().getName() + " added to favorite list.", Toast.LENGTH_LONG).show();
                btnFavourites.setImageResource(R.drawable.ic_favorite_black_24dp);
            }
        } else {
            DatabaseRestaurant convertedRestaurant = converter.convertToDatabaseRestaurant(restaurant.getZomatoRestaurant());

            if (database.restaurantsDAO().getRestaurant(restaurant.getZomatoRestaurant().getId()) != null) {
                popularRestaurantDataAccess.removePopularRestaurant(convertedRestaurant.getId());
                database.restaurantsDAO().deleteRestaurant(convertedRestaurant);
                Toast.makeText(getActivity(), "Restaurant " + restaurant.getZomatoRestaurant().getName() + " removed from favorite list.", Toast.LENGTH_LONG).show();
                btnFavourites.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            } else {
                popularRestaurantDataAccess.upsertPopularRestaurant(convertedRestaurant.getId(), convertedRestaurant.getName(), convertedRestaurant.getPhotos_url());
                database.restaurantsDAO().insertRestaurant(convertedRestaurant);
                Toast.makeText(getActivity(), "Restaurant " + restaurant.getZomatoRestaurant().getName() + " added to favorite list.", Toast.LENGTH_LONG).show();
                btnFavourites.setImageResource(R.drawable.ic_favorite_black_24dp);
            }
        }

    }

    @Override
    public void onRestaurantsLoaded(List<PopularRestaurant> list, boolean hasFailed) {

    }

    @Override
    public void onRestaurantUpdated() {

    }

    @Override
    public void sendRestaurant(RestaurantModel restaurant) {
        this.restaurant = restaurant;
        displayRestaurantData();
    }

    private void openRecommendDialog() {
        recommendedDialog.showDialog(restaurant);
    }

    private void hideRecommendDialog() {
        recommendedDialog.hideDialog();
    }
}
