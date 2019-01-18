package com.example.abbieturner.restaurantsfinder.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.abbieturner.restaurantsfinder.Data.UserReviews;
import com.example.abbieturner.restaurantsfinder.R;

import java.util.ArrayList;
import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {
    private List<UserReviews.UserReviewsData> reviewsList;
    private final Context context;
    private final ReviewItemClick listener;

    public ReviewsAdapter(Context context, ReviewItemClick listener) {
        reviewsList = new ArrayList<>();
        this.context = context;
        this.listener = listener;
    }

    public void setReviewsList(List<UserReviews.UserReviewsData> reviewsList) {
        this.reviewsList.clear();

        if(reviewsList == null){
            this.reviewsList.addAll(new ArrayList<UserReviews.UserReviewsData>());
        }else{
            this.reviewsList.addAll(reviewsList);
        }

        notifyDataSetChanged();
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cuisine_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        UserReviews.UserReviewsData.ReviewData review = reviewsList.get(position).getReview();

        String title = review.getReview_text();
        holder.cuisineTitle.setText(title);
    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView cuisineTitle;

        public ReviewViewHolder(View view) {
            super(view);
            cuisineTitle = view.findViewById(R.id.cuisine_title);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onReviewItemClick(reviewsList.get(getAdapterPosition()));
        }
    }

    public interface ReviewItemClick {
        void onReviewItemClick(UserReviews.UserReviewsData review);
    }
}