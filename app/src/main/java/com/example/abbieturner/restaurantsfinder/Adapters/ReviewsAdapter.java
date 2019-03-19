package com.example.abbieturner.restaurantsfinder.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abbieturner.restaurantsfinder.Data.ReviewFirebase;
import com.example.abbieturner.restaurantsfinder.Data.ReviewModel;
import com.example.abbieturner.restaurantsfinder.Data.UserReviews;
import com.example.abbieturner.restaurantsfinder.R;

import java.util.ArrayList;
import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {

    private List<ReviewModel> reviewsList;
    private final Context context;
    private final ReviewItemClick listener;
    private TextView reviewCounter;

    public ReviewsAdapter(Context context, ReviewItemClick listener, TextView reviewCounter) {
        reviewsList = new ArrayList<>();
        this.context = context;
        this.listener = listener;
        this.reviewCounter = reviewCounter;
    }

    public void setReviews(List<ReviewFirebase> firebaseReviews, List<UserReviews.UserReviewsData> zomatoReviews) {
        reviewsList.clear();

        if (firebaseReviews != null && firebaseReviews.size() > 0) {
            for (ReviewFirebase review : firebaseReviews) {
                reviewsList.add(new ReviewModel(review));
            }
        }

        if (zomatoReviews != null && zomatoReviews.size() > 0) {
            for (UserReviews.UserReviewsData review : zomatoReviews) {
                reviewsList.add(new ReviewModel(review.getReview()));
            }
        }

        if(reviewCounter != null){
            reviewCounter.setText(Integer.toString(reviewsList.size()));
        }

        notifyDataSetChanged();
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.review_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        ReviewModel review = reviewsList.get(position);
        String title = null;

        if (review.isFirebaseReview()) {
            title = review.getFirebaseReview().getReview();
            boolean hasPictureUrl = review.getFirebaseReview().hasPictureUrl();
            if (hasPictureUrl) {
                holder.picture.setVisibility(View.VISIBLE);
            }
        } else {
            title = review.getReview().getReview_text();
        }

        holder.reviewMsg.setText(title);
    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView reviewMsg;
        ImageView picture;

        public ReviewViewHolder(View view) {
            super(view);
            reviewMsg = view.findViewById(R.id.review_msg);
            picture = view.findViewById(R.id.btn_picture);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onReviewItemClick(reviewsList.get(getAdapterPosition()));
        }
    }

    public interface ReviewItemClick {
        void onReviewItemClick(ReviewModel review);
    }
}