package com.example.abbieturner.restaurantsfinder.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abbieturner.restaurantsfinder.CalculateDistance;
import com.example.abbieturner.restaurantsfinder.Data.Restaurant;
import com.example.abbieturner.restaurantsfinder.Data.UsersLocation;
import com.example.abbieturner.restaurantsfinder.Database.AppDatabase;
import com.example.abbieturner.restaurantsfinder.DatabaseModels.DatabaseRestaurant;
import com.example.abbieturner.restaurantsfinder.R;

import java.util.ArrayList;
import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.RestaurantViewHolder> {
    private List<Restaurant> restaurantList;
    private final Context context;
    private final RestaurantItemClick listener;
    private int layout;
    private ModelConverter converter;
    private AppDatabase database;

    public FavouriteAdapter(Context context, RestaurantItemClick listener, int layout) {
        restaurantList = new ArrayList<>();
        this.context = context;
        this.listener = listener;
        this.layout = layout;
        this.converter = ModelConverter.getInstance();
        this.database = AppDatabase.getInstance(this.context);
    }

    public void setCuisineList(List<Restaurant> restaurantList) {
        this.restaurantList.clear();
        this.restaurantList.addAll(restaurantList);
        notifyDataSetChanged();
    }

    @Override
    public RestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(layout, parent, false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RestaurantViewHolder holder, int position) {
        final Restaurant restaurant = restaurantList.get(position);

        String title = restaurant.getName();
        holder.restaurantName.setText(title);

        String distance = CalculateDistance.getInstance().getRestaurantDistance(restaurant);
        holder.distanceTextView.setText(distance);

        holder.favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFavoriteRestaurant(restaurant);
            }
        });
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    public class RestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView restaurantName, distanceTextView;
        ImageView favourite;

        public RestaurantViewHolder(View view) {
            super(view);
            restaurantName = view.findViewById(R.id.favourite_restaurant_name);
            favourite = view.findViewById(R.id.btn_favourites);
            distanceTextView = view.findViewById(R.id.distance_txt);
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

    private void toggleFavoriteRestaurant(Restaurant restaurant) {

        DatabaseRestaurant convertedRestaurant = converter.convertToDatabaseRestaurant(restaurant);


        if (database.restaurantsDAO().getRestaurant(restaurant.getId()) != null) {
            database.restaurantsDAO().deleteRestaurant(convertedRestaurant);
            Toast.makeText(context, "Restaurant " + restaurant.getName() + " removed from favorite list.", Toast.LENGTH_LONG).show();
            restaurantList.remove(restaurant);
            notifyDataSetChanged();
        } else {
            database.restaurantsDAO().insertRestaurant(convertedRestaurant);
            Toast.makeText(context, "Restaurant " + restaurant.getName() + " added to favorite list.", Toast.LENGTH_LONG).show();
        }


        List<Restaurant> favoritesRestaurants = converter.convertToRestaurants(database.restaurantsDAO().getRestaurants());
    }

}
