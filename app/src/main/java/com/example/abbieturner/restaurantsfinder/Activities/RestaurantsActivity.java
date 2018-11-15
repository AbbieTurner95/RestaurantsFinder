package com.example.abbieturner.restaurantsfinder.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.abbieturner.restaurantsfinder.R;

public class RestaurantsActivity extends AppCompatActivity {

    private Intent intent;
    private String name, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);

        intent = getIntent();

        if(intent != null){
            id = intent.getStringExtra("CUISINE_ID");
            name = intent.getStringExtra("CUISINE_NAME");
        } else {
            Log.e("ERROR INTENT", "Intent null");
        }

        this.setTitle(name);
    }
}
