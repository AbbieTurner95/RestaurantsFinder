package com.example.abbieturner.restaurantsfinder.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abbieturner.restaurantsfinder.Data.Restaurant;
import com.example.abbieturner.restaurantsfinder.Data.UsersLocation;
import com.example.abbieturner.restaurantsfinder.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.RestaurantsViewHolder>{

    private List<Restaurant> restaurantsList;
    private final Context context;
    private final RestaurantItemClick listener;
    DecimalFormat df = new DecimalFormat("#.00");


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
        final Restaurant restaurant = restaurantsList.get(position);

        String title = restaurant.getName();

        holder.restaurantName.setText(title);
        holder.distance.setText(getRestaurantDistance(restaurant));

        holder.favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFavoriteRestaurant(restaurant);
            }
        });


    }

    @Override
    public int getItemCount(){
        return restaurantsList.size();
    }

    public class RestaurantsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView restaurantName, distance;
        ImageView favorites;

        public RestaurantsViewHolder(View view){
            super(view);
            restaurantName = view.findViewById(R.id.restaurant_name);
            favorites = view.findViewById(R.id.restaurant_favorite_btn);
            distance = view.findViewById(R.id.restaurant_distance);
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

    private void toggleFavoriteRestaurant(Restaurant restaurant){
        Toast.makeText(context, "Add " + restaurant.getName() + " to favorite list.", Toast.LENGTH_LONG).show(); //TODO save restaurant in phone
    }

    private String getRestaurantDistance(Restaurant restaurant){
        return "Distance: " + Double.toString(calcDistance(restaurant)) + " miles";
    }

    private double calcDistance(Restaurant restaurant){
        double distance = UsersLocation.getDistance(restaurant.getLocation().getLat(), restaurant.getLocation().getLongt());

        return Math.round(distance*100.0)/100.0;
    }
}
