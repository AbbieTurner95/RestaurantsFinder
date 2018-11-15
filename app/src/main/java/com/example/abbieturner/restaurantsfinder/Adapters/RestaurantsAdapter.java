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

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.RestaurantsViewHolder>{

    private List<Restaurant> restaurantsList;
    private final Context context;
    private final RestaurantItemClick listener;

    public RestaurantsAdapter(Context context, RestaurantItemClick listener){
        restaurantsList = new ArrayList<>();
        this.context = context;
        this.listener = listener;
    }

    public void setRestaurantsList(List<Restaurant> restaurantsList){
        this.restaurantsList.clear();
        this.restaurantsList.addAll(restaurantsList);
        notifyDataSetChanged();
    }

    @Override
    public RestaurantsViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.restaurant_item, parent, false);
        return new RestaurantsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RestaurantsViewHolder holder, int position){
        Restaurant restaurant = restaurantsList.get(position);

        String title = restaurant.getName();
        holder.restaurantName.setText(title);
    }

    @Override
    public int getItemCount(){
        return restaurantsList.size();
    }

    public class RestaurantsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView restaurantName;

        public RestaurantsViewHolder(View view){
            super(view);
            restaurantName = view.findViewById(R.id.restaurant_name);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            listener.onRestaurantItemClick(restaurantsList.get(getAdapterPosition()));
        }
    }

    public interface RestaurantItemClick{
        void onRestaurantItemClick(Restaurant restaurant);
    }
}
