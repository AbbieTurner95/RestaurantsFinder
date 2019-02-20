package com.example.abbieturner.restaurantsfinder.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.abbieturner.restaurantsfinder.Activities.RestaurantActivityNew;
import com.example.abbieturner.restaurantsfinder.Adapters.EmptyRecyclerView;
import com.example.abbieturner.restaurantsfinder.Adapters.ReviewsAdapter;
import com.example.abbieturner.restaurantsfinder.Data.ReviewFirebase;
import com.example.abbieturner.restaurantsfinder.Data.UserReviews;
import com.example.abbieturner.restaurantsfinder.Dialogs.ReviewDialog;
import com.example.abbieturner.restaurantsfinder.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantReviews extends Fragment{

    private ReviewsAdapter reviewsAdapter;
    private LinearLayoutManager reviewsLayoutManager;
    private ReviewDialog reviewDialog;

    @BindView(R.id.pb_reviews)
    ProgressBar pbReviews;
    @BindView(R.id.reviews_recycler_view)
    EmptyRecyclerView reviewsRecyclerView;
    @BindView(R.id.reviews_empty_view)
    LinearLayout reviewsEmptyView;
    @BindView(R.id.tv_review_count)
    TextView reviewCount;
    @BindView(R.id.btn_add_review)
    TextView btnAddReview;

    public RestaurantReviews(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_reviews, container, false);

        ButterKnife.bind(this, view);

        reviewDialog = new ReviewDialog(getActivity());
        reviewsLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        reviewsRecyclerView.setLayoutManager(reviewsLayoutManager);
        reviewsAdapter = new ReviewsAdapter(getActivity(), (RestaurantActivityNew)getActivity(), reviewCount);
        View reviewsEmptyView = view.findViewById(R.id.reviews_empty_view);
        reviewsRecyclerView.setEmptyView(reviewsEmptyView);
        reviewsRecyclerView.setAdapter(reviewsAdapter);

        btnAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reviewDialog.showDialog();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pbReviews.setVisibility(View.VISIBLE);
        ((RestaurantActivityNew)getActivity()).restaurantReviewsCreated();
    }

    public void pictureLoaded(){
        reviewDialog.pictureLoaded();
    }

    public void hideDialog(){
        reviewDialog.hideDialog();
    }

    public void hideDialogsProgressBar(){
        reviewDialog.hideProgressBar();
    }

    public void setReviews(List<ReviewFirebase> firebaseReviews, List<UserReviews.UserReviewsData> zomatoReviews){
        pbReviews.setVisibility(View.GONE);
        reviewsAdapter.setReviews(firebaseReviews, zomatoReviews);
    }
}
