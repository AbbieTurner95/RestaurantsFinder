package com.example.abbieturner.restaurantsfinder.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.abbieturner.restaurantsfinder.FirebaseModels.RecommendedRestaurant;
import com.example.abbieturner.restaurantsfinder.PicassoLoader;
import com.example.abbieturner.restaurantsfinder.R;

import java.util.ArrayList;
import java.util.List;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.views.AvatarView;

public class RecommendedAdapter extends RecyclerView.Adapter<RecommendedAdapter.RecommendRestaurantViewHolder>{
    private List<RecommendedRestaurant> restaurantsList;
    private final RecommendRestaurantClick listener;

    public RecommendedAdapter(RecommendRestaurantClick listener) {
        this.restaurantsList = new ArrayList<>();
        this.listener = listener;
    }

    public void setList(List<RecommendedRestaurant> restaurantsList) {
        if(restaurantsList != null){
            this.restaurantsList.clear();
            this.restaurantsList.addAll(restaurantsList);
            notifyDataSetChanged();
        }
    }

    @Override
    public RecommendRestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommended_restaurant_item, parent, false);
        return new RecommendRestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecommendRestaurantViewHolder holder, int position) {
        RecommendedRestaurant restaurant = restaurantsList.get(position);

        holder.name.setText(restaurant.getRestaurantName());
        holder.imageLoader = new PicassoLoader();
        holder.imageLoader.loadImage(holder.avatarView, restaurant.getPictureUrl(), restaurant.getRestaurantName());
    }

    @Override
    public int getItemCount() {
        return restaurantsList.size();
    }


    public class RecommendRestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        AvatarView avatarView;
        IImageLoader imageLoader;


        public RecommendRestaurantViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.restaurant_name);
            avatarView = view.findViewById(R.id.avatar_view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onRecommendRestaurantClick(restaurantsList.get(getAdapterPosition()));
        }
    }

    public interface RecommendRestaurantClick {
        void onRecommendRestaurantClick(RecommendedRestaurant restaurant);
    }
}
