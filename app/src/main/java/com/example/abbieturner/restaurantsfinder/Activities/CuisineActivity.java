package com.example.abbieturner.restaurantsfinder.Activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.view.MenuItem;

import com.example.abbieturner.restaurantsfinder.API.API;
import com.example.abbieturner.restaurantsfinder.Adapters.CuisineAdapter;
import com.example.abbieturner.restaurantsfinder.Adapters.CuisineJsonAdapter;
import com.example.abbieturner.restaurantsfinder.Data.Cuisine;
import com.example.abbieturner.restaurantsfinder.Data.Cuisines;
import com.example.abbieturner.restaurantsfinder.Data.CuisinesSingleton;
import com.example.abbieturner.restaurantsfinder.R;

import com.example.abbieturner.restaurantsfinder.Data.UsersLocation;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CuisineActivity extends AppCompatActivity implements CuisineAdapter.CuisineItemClick {

    @BindView(R.id.adView)
    AdView mAdView;
    @BindView(R.id.cuisines_recycler_view)
    RecyclerView recyclerView;

    private CuisineAdapter cuisineAdapter;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuisines);
        ButterKnife.bind(this);

        this.setTitle(R.string.title_cuisines);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        cuisineAdapter = new CuisineAdapter(this, this);
        recyclerView.setAdapter(cuisineAdapter);

        cuisineAdapter.setCuisineList(CuisinesSingleton.getInstance().getCuisines());

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setEnterExitTransition(Intent intent) {
        getWindow().setExitTransition(new Fade().setDuration(1000));
        getWindow().setReenterTransition(new Fade().setDuration(1000));
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(CuisineActivity.this).toBundle());
    }

    @Override
    public void onCuisineItemClick(Cuisine cuisines) {
        int id = cuisines.getCuisine_id();
        String name = cuisines.getCuisine_name();


        Intent intent = new Intent(CuisineActivity.this, RestaurantsActivity.class);
        intent.putExtra("cuisine_id", id);
        intent.putExtra(getResources().getString(R.string.TAG_CUISINE_NAME), name);
        startActivity(intent);
    }
}