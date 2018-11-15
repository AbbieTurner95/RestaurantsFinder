package com.example.abbieturner.restaurantsfinder.Activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.abbieturner.restaurantsfinder.Adapters.RestaurantsAdapter;
import com.example.abbieturner.restaurantsfinder.Data.Restaurant;
import com.example.abbieturner.restaurantsfinder.R;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantsActivity extends AppCompatActivity implements RestaurantsAdapter.RestaurantItemClick {




    private Intent intent;
    private String name, id;
    RecyclerView recyclerView;
    private RestaurantsAdapter restaurantsAdapter;
    private LinearLayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);

        recyclerView = findViewById(R.id.restaurants_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        restaurantsAdapter = new RestaurantsAdapter(this, this);
        recyclerView.setAdapter(restaurantsAdapter);


        intent = getIntent();

        if(intent != null){
            id = intent.getStringExtra(getResources().getString(R.string.TAG_CUISINE_ID));
            name = intent.getStringExtra(getResources().getString(R.string.TAG_CUISINE_NAME));
        } else {
            Log.e("ERROR INTENT", "Intent null");
        }

        this.setTitle(name);



        restaurantsAdapter.setRestaurantsList(setMockData());
    }

    @Override
    public void onRestaurantItemClick(Restaurant restaurant){


        Toast.makeText(this, restaurant.getName(),
                Toast.LENGTH_LONG).show();
    }

    private List<Restaurant> setMockData(){
        List<Restaurant> myList = new ArrayList<>();

        myList.add(new Restaurant("1", "Restaurant 1"));
        myList.add(new Restaurant("2", "Restaurant 2"));
        myList.add(new Restaurant("3", "Restaurant 3"));
        myList.add(new Restaurant("4", "Restaurant 4"));
        myList.add(new Restaurant("5", "Restaurant 5"));


        return myList;
    }

}
