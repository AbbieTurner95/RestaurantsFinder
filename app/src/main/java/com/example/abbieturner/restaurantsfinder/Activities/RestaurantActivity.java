package com.example.abbieturner.restaurantsfinder.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.abbieturner.restaurantsfinder.API.API;
import com.example.abbieturner.restaurantsfinder.Adapters.ReviewsAdapter;
import com.example.abbieturner.restaurantsfinder.Data.Restaurant;
import com.example.abbieturner.restaurantsfinder.Data.ReviewFirebase;
import com.example.abbieturner.restaurantsfinder.Data.ReviewModel;
import com.example.abbieturner.restaurantsfinder.Data.ReviewSingleton;
import com.example.abbieturner.restaurantsfinder.Data.UserReviews;
import com.example.abbieturner.restaurantsfinder.Dialogs.ReviewPictureDialog;
import com.example.abbieturner.restaurantsfinder.FirebaseAccess.Review;
import com.example.abbieturner.restaurantsfinder.FirebaseAccess.Reviews;
import com.example.abbieturner.restaurantsfinder.Fragments.RestaurantInfo;
import com.example.abbieturner.restaurantsfinder.Fragments.RestaurantMap;
import com.example.abbieturner.restaurantsfinder.Fragments.RestaurantReviews;
import com.example.abbieturner.restaurantsfinder.Interfaces.ISendRestaurant;
import com.example.abbieturner.restaurantsfinder.R;

import com.example.abbieturner.restaurantsfinder.Adapters.RestaurantPagerAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestaurantActivity extends AppCompatActivity
        implements  ReviewsAdapter.ReviewItemClick,
        Review.ReviewListener,
        Reviews.ReviewsListener
{

    private String jsonRestaurant, restaurantId;
    private String TAG_RESTAURANT_ID, TAG_RESTAURANT, ZOOMATO_BASE_URL;
    private Restaurant restaurant;
    private Gson gson;
    private API.ZomatoApiCalls service;
    private RestaurantInfo restaurantInfoFragment;
    private RestaurantMap restaurantMapFragment;
    private RestaurantReviews restaurantReviewsFragment;
    private ISendRestaurant restaurantInfoInterface, restaurantMapInterface;
    private Retrofit retrofit;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private Review reviewDataAccess;
    private Reviews reviewsDataAccess;
    private ReviewPictureDialog pictureDialog;

    private List<ReviewFirebase> firebaseReviews;
    private List<UserReviews.UserReviewsData> zomatoReviews;
    private boolean isFirebaseReviewLoaded, isZomatoReviewLoaded;

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        ButterKnife.bind(this);

        toolbar.setTitle("");
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
        isZomatoReviewLoaded = false;
        isFirebaseReviewLoaded = false;
        fetchReviews();
        getFirebaseReviews();
    }

    public void getFirebaseReviews(){
        reviewsDataAccess.getReviews(restaurant.getId());
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
                        isZomatoReviewLoaded = true;
                        zomatoReviews.clear();
                        zomatoReviews.addAll(response.body().getUser_reviews());
                        setReviews();

                    }

                    @Override
                    public void onFailure(Call<UserReviews> call, Throwable t) {
                        isZomatoReviewLoaded = true;
                        setReviews();
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
        firebaseReviews = new ArrayList<>();
        zomatoReviews = new ArrayList<>();

        reviewDataAccess = new Review(this);
        reviewsDataAccess = new Reviews(this);
        pictureDialog = new ReviewPictureDialog(this);
        restaurantInfoFragment = new RestaurantInfo();
        restaurantInfoInterface = (ISendRestaurant)restaurantInfoFragment;
        restaurantMapFragment = new RestaurantMap();
        restaurantMapInterface = (ISendRestaurant) restaurantMapFragment;
        restaurantReviewsFragment = new RestaurantReviews();


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
        //getSupportActionBar().setTitle(restaurant.getName());
        toolbar.setTitle(restaurant.getName());

        restaurantInfoInterface.sendRestaurant(restaurant);
    }

    @Override
    public void onReviewItemClick(ReviewModel review) {
        if(review.isFirebaseReview() && review.getFirebaseReview().hasPictureUrl()){
            pictureDialog.showDialog(review.getFirebaseReview());
        }else{
            Toast.makeText(this, "This review does not have any picture.", Toast.LENGTH_LONG).show();
        }
    }

    public void openCamera(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            ReviewSingleton.getInstance().getReview().setPicture(imageBitmap);

            notifyReviewDialog();
        }
    }

    private void notifyReviewDialog(){
        restaurantReviewsFragment.pictureLoaded();
    }

    public void createReview(){
        reviewDataAccess.createReview(ReviewSingleton.getInstance().getReview(), restaurant.getId());
    }

    @Override
    public void onReviewCreated(boolean hasFailed) {
        if(hasFailed){
            Toast.makeText(this, "Failed to create review. Please try again later.", Toast.LENGTH_LONG).show();
            restaurantReviewsFragment.hideDialogsProgressBar();
        }else{
            Toast.makeText(this, "Review created.", Toast.LENGTH_LONG).show();
            restaurantReviewsFragment.hideDialog();
            getFirebaseReviews();
        }
    }

    @Override
    public void onReviewsLoaded(List<ReviewFirebase> reviews, boolean hasFailed) {
        isFirebaseReviewLoaded = true;
        if(hasFailed){
            Toast.makeText(this, "Failed to load reviews", Toast.LENGTH_LONG).show();
            isFirebaseReviewLoaded = true;
            setReviews();
        }else{
            firebaseReviews.clear();
            firebaseReviews.addAll(reviews);
            setReviews();

        }
    }

    private void setReviews(){
        if(isFirebaseReviewLoaded && isZomatoReviewLoaded){
            restaurantReviewsFragment.setReviews(firebaseReviews, zomatoReviews);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(RestaurantActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
