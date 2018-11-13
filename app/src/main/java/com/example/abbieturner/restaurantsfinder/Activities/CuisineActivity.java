package com.example.abbieturner.restaurantsfinder.Activities;

import android.app.ActivityOptions;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;

import com.example.abbieturner.restaurantsfinder.Adapters.CuisineAdapter;
import com.example.abbieturner.restaurantsfinder.Data.Cuisine;
import com.example.abbieturner.restaurantsfinder.Data.Cuisines;
import com.example.abbieturner.restaurantsfinder.R;
import com.example.abbieturner.restaurantsfinder.ViewModels.CuisineViewModel;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;



import butterknife.BindView;
import butterknife.ButterKnife;


public class CuisineActivity extends AppCompatActivity implements CuisineAdapter.CuisineItemClick {

        @BindView(R.id.adView)
        AdView mAdView;
        @BindView(R.id.cuisines_recycler_view)
        RecyclerView recyclerView;

        private CuisineAdapter cuisineAdapter;
        private String TAG = "CUISINE_ID";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_cuisines);
            ButterKnife.bind(this);

            GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
            recyclerView.setLayoutManager(layoutManager);
            cuisineAdapter = new CuisineAdapter(this, this);
            recyclerView.setAdapter(cuisineAdapter);

            fetchCuisines();

            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build();
            mAdView.loadAd(adRequest);
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

      /*  @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.favorites_menu:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                     //   setEnterExitTransition(new Intent(CuisineActivity.this, FavoritesActivity.class));
                    }
                   // startActivity(new Intent(this, FavoritesActivity.class));
                    return true;
                case R.id.logout:
                    AuthUI.getInstance()
                            .signOut(this)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Intent intent = new Intent(CuisineActivity.this, LogInActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                default:
                    return super.onOptionsItemSelected(item);
            }
        } */

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        private void setEnterExitTransition(Intent intent) {
            getWindow().setExitTransition(new Fade().setDuration(1000));
            getWindow().setReenterTransition(new Fade().setDuration(1000));
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(CuisineActivity.this).toBundle());
        }

        @Override
        public void onCuisineItemClick(Cuisine cuisines) {
            int id = cuisines.getCuisine_id();

          //  Intent intent = new Intent(CuisineActivity.this, RestaurantsListActivity.class);
          //  intent.putExtra(TAG, id);
          //  startActivity(intent);
        }
    }