package com.example.abbieturner.restaurantsfinder.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.abbieturner.restaurantsfinder.R;

public class RestaurantsActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);

        intent = getIntent();

        String id = intent.getStringExtra("CUISINE_ID");
        String name = intent.getStringExtra("CUISINE_NAME");


        this.setTitle(name);
    }
}
