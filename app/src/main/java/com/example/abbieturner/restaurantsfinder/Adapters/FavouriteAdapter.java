package com.example.abbieturner.restaurantsfinder.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.abbieturner.restaurantsfinder.Data.Restaurant;
import com.example.abbieturner.restaurantsfinder.R;

import java.util.ArrayList;
import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.RestaurantViewHolder>{
    private List<Restaurant> restaurantList;
    private final Context context;
    private final RestaurantItemClick listener;

    public FavouriteAdapter(Context context, RestaurantItemClick listener) {
        restaurantList = new ArrayList<>();
        this.context = context;
        this.listener = listener;
    }

    public void setCuisineList(List<Restaurant> restaurantList) {
        this.restaurantList.clear();
        this.restaurantList.addAll(restaurantList);
        notifyDataSetChanged();
    }

    @Override
    public RestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.favourite_restaurant_item, parent, false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RestaurantViewHolder holder, int position) {
        Restaurant restaurant = restaurantList.get(position);

        String title = restaurant.getName();
        holder.restaurantName.setText(title);
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    public class RestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView restaurantName;

        public RestaurantViewHolder(View view) {
            super(view);
            restaurantName = view.findViewById(R.id.favourite_restaurant_name);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onRestaurantItemClick(restaurantList.get(getAdapterPosition()));
        }
    }

    public interface RestaurantItemClick {
        void onRestaurantItemClick(Restaurant cuisines);
    }
}
