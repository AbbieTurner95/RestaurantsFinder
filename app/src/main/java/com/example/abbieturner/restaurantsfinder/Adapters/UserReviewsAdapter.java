package com.example.abbieturner.restaurantsfinder.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.abbieturner.restaurantsfinder.R;

import com.example.abbieturner.restaurantsfinder.FirebaseModels.UserReview;

import java.util.ArrayList;
import java.util.List;

public class UserReviewsAdapter extends RecyclerView.Adapter<UserReviewsAdapter.UserReviewViewHolder>{
    private List<UserReview> reviewsList;
    private final Context context;
    private final UserReviewItemClick listener;

    public UserReviewsAdapter(Context context, UserReviewItemClick listener) {
        reviewsList = new ArrayList<>();
        this.context = context;
        this.listener = listener;
    }

    public void setReviews(List<UserReview> firebaseReviews) {
        if(firebaseReviews != null){
            reviewsList.clear();
            reviewsList.addAll(firebaseReviews);
            notifyDataSetChanged();
        }
    }

    @Override
    public UserReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_review_item, parent, false);
        return new UserReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserReviewViewHolder holder, int position) {
        UserReview review = reviewsList.get(position);

        holder.reviewMsg.setText(review.getReview());

        if(review.hasPictureUrl()){
            holder.picture.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

    public class UserReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView reviewMsg;
        ImageView picture;

        public UserReviewViewHolder(View view) {
            super(view);
            reviewMsg = view.findViewById(R.id.review_msg);
            picture = view.findViewById(R.id.btn_picture);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onUserReviewItemClick(reviewsList.get(getAdapterPosition()));
        }
    }

    public interface UserReviewItemClick {
        void onUserReviewItemClick(UserReview review);
    }
}
