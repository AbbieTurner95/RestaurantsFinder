package com.example.abbieturner.restaurantsfinder.Activities;

import android.app.ActivityOptions;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.widget.Toast;

import com.example.abbieturner.restaurantsfinder.Adapters.CuisineAdapter;

import com.example.abbieturner.restaurantsfinder.Data.Cuisines;
import com.example.abbieturner.restaurantsfinder.R;
import com.example.abbieturner.restaurantsfinder.ViewModels.CuisineViewModel;
import com.example.abbieturner.restaurantsfinder.Data.Cuisine;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CuisineActivity extends AppCompatActivity implements CuisineAdapter.CuisineItemClick{

    @BindView(R.id.cuisines_recycler_view)
    RecyclerView recyclerView;


    private CuisineAdapter cuisineAdapter;
    private final String TAG = "CUISINE_ID";
    private final String TAG_NAME = "CUISINE_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuisines);
        ButterKnife.bind(this);


        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        cuisineAdapter = new CuisineAdapter(this, this);
        recyclerView.setAdapter(cuisineAdapter);

        //fetchCuisines();
        cuisineAdapter.setCuisineList(addMockData());
    }


    public void fetchCuisines() {
        CuisineViewModel cuisineViewModel = ViewModelProviders.of(this).get(CuisineViewModel.class);
        cuisineViewModel.getLiveData().observe(this, new Observer<Cuisines>() {
            @Override
            public void onChanged(@Nullable Cuisines cuisines) {
                cuisineAdapter.setCuisineList(cuisines.getCuisinesList());
            }
        });
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
        //Toast.makeText(this, name, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(CuisineActivity.this, RestaurantsActivity.class);
        intent.putExtra(TAG, id);
        intent.putExtra(TAG_NAME, name);
        startActivity(intent);
    }

    private List<Cuisine> addMockData(){
        List<Cuisine> myList = new ArrayList<>();

        myList.add(new Cuisine(1, "Indian"));
        myList.add(new Cuisine(2, "Greek"));
        myList.add(new Cuisine(3, "Chinese"));
        myList.add(new Cuisine(4, "African"));
        myList.add(new Cuisine(5, "British"));
        myList.add(new Cuisine(6, "Italian"));
        myList.add(new Cuisine(7, "Japanese"));
        myList.add(new Cuisine(8, "American"));
        myList.add(new Cuisine(9, "Spanish"));
        myList.add(new Cuisine(10, "Mexican"));
        myList.add(new Cuisine(11, "Korean"));
        myList.add(new Cuisine(12, "French"));
        myList.add(new Cuisine(13, "Caribbean"));
        myList.add(new Cuisine(14, "East Europe"));
        myList.add(new Cuisine(15, "Irish"));


        return myList;
    }
}

