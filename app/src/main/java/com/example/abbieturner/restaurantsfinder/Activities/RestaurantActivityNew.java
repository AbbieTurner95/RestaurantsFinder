package com.example.abbieturner.restaurantsfinder.Activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.abbieturner.restaurantsfinder.API.API;
import com.example.abbieturner.restaurantsfinder.Adapters.ReviewsAdapter;
import com.example.abbieturner.restaurantsfinder.Data.Restaurant;
import com.example.abbieturner.restaurantsfinder.Data.UserReviews;
import com.example.abbieturner.restaurantsfinder.Fragments.RestaurantInfo;
import com.example.abbieturner.restaurantsfinder.Fragments.RestaurantMap;
import com.example.abbieturner.restaurantsfinder.Fragments.RestaurantReviews;
import com.example.abbieturner.restaurantsfinder.Interfaces.ISendRestaurant;
import com.example.abbieturner.restaurantsfinder.Interfaces.ISendReviews;
import com.example.abbieturner.restaurantsfinder.R;

import com.example.abbieturner.restaurantsfinder.Adapters.RestaurantPagerAdapter;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestaurantActivityNew extends AppCompatActivity implements ReviewsAdapter.ReviewItemClick {

    private String jsonRestaurant, restaurantId;
    private String TAG_RESTAURANT_ID, TAG_RESTAURANT, ZOOMATO_BASE_URL;
    private Restaurant restaurant;
    private Gson gson;
    private API.ZomatoApiCalls service;
    private RestaurantInfo restaurantInfoFragment;
    private RestaurantMap restaurantMapFragment;
    private RestaurantReviews restaurantReviewsFragment;
    private ISendRestaurant restaurantInfoInterface, restaurantMapInterface;
    private ISendReviews restaurantReviewsInterface;
    private Retrofit retrofit;

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_new);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_menu_white_24dp));

        setTags();
        initialiseNewInstances();

        getStringsExtra();
        setupViewPager();
    }


    public void restaurantInfoLoaded(){
        if(isRestaurantId()){
            getRestaurantById();
        }else if(isRestaurantJson()){
            restaurant = gson.fromJson(jsonRestaurant, Restaurant.class); // Converts the JSON String to an Object
            displayRestaurantData();
        }else{
            // Error.....
        }
    }

    public void restaurantReviewsCreated(){
        fetchReviews();
        //getFirebaseReviews();
    }

    public void getFirebaseReviews(){

    }

    public void restaurantMapReady(){
        restaurantMapInterface.sendRestaurant(restaurant);
    }

    private void fetchReviews() {
        service.getReviews(restaurant.getId())
                .enqueue(new Callback<UserReviews>() {
                    @Override
                    public void onResponse(Call<UserReviews> call, Response<UserReviews> response) {
                        assert response.body() != null;
                        restaurantReviewsInterface.sendReviews(response.body().getUser_reviews());
                    }

                    @Override
                    public void onFailure(Call<UserReviews> call, Throwable t) {
                    }
                });
    }

    private void setupViewPager() {
        RestaurantPagerAdapter adapter = new RestaurantPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(restaurantInfoFragment, "Info");
        adapter.addFragment(restaurantMapFragment, "Map");
        adapter.addFragment(restaurantReviewsFragment, "Reviews");
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
    }
    private void setTags(){
        TAG_RESTAURANT = getResources().getString(R.string.TAG_RESTAURANT);
        TAG_RESTAURANT_ID = getResources().getString(R.string.TAG_RESTAURANT_ID);
        ZOOMATO_BASE_URL = getResources().getString(R.string.BASE_URL_API);
    }
    private void getStringsExtra(){
        jsonRestaurant = getIntent().getStringExtra(TAG_RESTAURANT);
        restaurantId = getIntent().getStringExtra(TAG_RESTAURANT_ID);
    }
    private boolean isRestaurantId(){
        return restaurantId != null;
    }
    private boolean isRestaurantJson(){
        return jsonRestaurant != null;
    }
    private void initialiseNewInstances(){
        gson = new Gson();
        restaurantInfoFragment = new RestaurantInfo();
        restaurantInfoInterface = (ISendRestaurant)restaurantInfoFragment;
        restaurantMapFragment = new RestaurantMap();
        restaurantMapInterface = (ISendRestaurant) restaurantMapFragment;
        restaurantReviewsFragment = new RestaurantReviews();
        restaurantReviewsInterface = (ISendReviews)restaurantReviewsFragment;


        retrofit = new Retrofit.Builder()
                .baseUrl(ZOOMATO_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(API.ZomatoApiCalls.class);
    }
    private void getRestaurantById(){
        service.getRestaurant(restaurantId)
                .enqueue(new Callback<Restaurant>() {
                    @Override
                    public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                        restaurant = response.body();

                        if(restaurant != null){
                            displayRestaurantData();
                        }
                    }

                    @Override
                    public void onFailure(Call<Restaurant> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
    }

    private void displayRestaurantData(){
        toolbar.setTitle(restaurant.getName());

        restaurantInfoInterface.sendRestaurant(restaurant);
    }

    @Override
    public void onReviewItemClick(UserReviews.UserReviewsData review) {

    }
}
