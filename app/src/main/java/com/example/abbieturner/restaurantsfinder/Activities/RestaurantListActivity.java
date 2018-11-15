package com.example.abbieturner.restaurantsfinder.Activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Fade;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.abbieturner.restaurantsfinder.R;

public class RestaurantListActivity extends AppCompatActivity {

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
        fetchRestaurantsList();
    }


    public void fetchRestaurantsList(){




    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)                    //Transaction from activity to activity
    private void setEnterExitTransition(Intent intent) {
        getWindow().setExitTransition(new Fade().setDuration(1000));
        getWindow().setReenterTransition(new Fade().setDuration(1000));
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(RestaurantListActivity.this).toBundle());
    }
}
