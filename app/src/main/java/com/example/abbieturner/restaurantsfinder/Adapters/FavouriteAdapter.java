package com.example.abbieturner.restaurantsfinder.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abbieturner.restaurantsfinder.CalculateDistance;
import com.example.abbieturner.restaurantsfinder.Data.Restaurant;
import com.example.abbieturner.restaurantsfinder.Database.AppDatabase;
import com.example.abbieturner.restaurantsfinder.DatabaseModels.DatabaseRestaurant;
import com.example.abbieturner.restaurantsfinder.FirebaseAccess.PopularRestaurants;
import com.example.abbieturner.restaurantsfinder.FirebaseModels.PopularRestaurant;
import com.example.abbieturner.restaurantsfinder.PicassoLoader;
import com.example.abbieturner.restaurantsfinder.R;

import java.util.ArrayList;
import java.util.List;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.views.AvatarView;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.RestaurantViewHolder> implements PopularRestaurants.PopularRestaurantsListener {
    private List<Restaurant> restaurantList;
    private final Context context;
    private final RestaurantItemClick listener;
    private int layout;
    private ModelConverter converter;
    private AppDatabase database;
    private PopularRestaurants popularRestaurantsDataAccess;

    public FavouriteAdapter(Context context, RestaurantItemClick listener, int layout) {
        restaurantList = new ArrayList<>();
        this.context = context;
        this.listener = listener;
        this.layout = layout;
        this.converter = ModelConverter.getInstance();
        this.database = AppDatabase.getInstance(this.context);
        this.popularRestaurantsDataAccess = new PopularRestaurants(this);
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

        String distance = CalculateDistance.getInstance().getRestaurantDistance(
                Double.parseDouble(restaurant.getLocation().getLatitude()),
                Double.parseDouble(restaurant.getLocation().getLongitude()));
        holder.distanceTextView.setText(distance);

        holder.favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFavoriteRestaurant(restaurant);
            }
        });

        holder.imageLoader = new PicassoLoader();
        holder.imageLoader.loadImage(holder.avatarView, restaurant.getPhotos_url(), restaurant.getName());
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    @Override
    public void onRestaurantsLoaded(List<PopularRestaurant> list, boolean hasFailed) {
        if (hasFailed) {
        } else {
        }
    }

    @Override
    public void onRestaurantUpdated() {
    }

    public class RestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView restaurantName, distanceTextView;
        ImageView favourite;
        AvatarView avatarView;
        IImageLoader imageLoader;

        public RestaurantViewHolder(View view) {
            super(view);
            restaurantName = view.findViewById(R.id.favourite_restaurant_name);
            favourite = view.findViewById(R.id.btn_favourites);
            distanceTextView = view.findViewById(R.id.distance_txt);
            avatarView = view.findViewById(R.id.avatar_view);
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
            popularRestaurantsDataAccess.removePopularRestaurant(restaurant.getId());
            database.restaurantsDAO().deleteRestaurant(convertedRestaurant);
            Toast.makeText(context, "Restaurant " + restaurant.getName() + " removed from favorite list.", Toast.LENGTH_LONG).show();
            restaurantList.remove(restaurant);
            notifyDataSetChanged();
        } else {
            popularRestaurantsDataAccess.upsertPopularRestaurant(restaurant.getId(), restaurant.getName(), restaurant.getPhotos_url());
            database.restaurantsDAO().insertRestaurant(convertedRestaurant);
            Toast.makeText(context, "Restaurant " + restaurant.getName() + " added to favorite list.", Toast.LENGTH_LONG).show();
        }
        List<Restaurant> favoritesRestaurants = converter.convertToRestaurants(database.restaurantsDAO().getRestaurants());
    }
}