package com.example.abbieturner.restaurantsfinder.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abbieturner.restaurantsfinder.CalculateDistance;
import com.example.abbieturner.restaurantsfinder.CustomFilter;
import com.example.abbieturner.restaurantsfinder.Data.Restaurant;
import com.example.abbieturner.restaurantsfinder.Data.UsersLocation;
import com.example.abbieturner.restaurantsfinder.Database.AppDatabase;
import com.example.abbieturner.restaurantsfinder.DatabaseModels.DatabaseRestaurant;
import com.example.abbieturner.restaurantsfinder.FirebaseAccess.PopularRestaurants;
import com.example.abbieturner.restaurantsfinder.FirebaseModels.PopularRestaurant;
import com.example.abbieturner.restaurantsfinder.R;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.RestaurantsViewHolder> implements Serializable, Filterable, PopularRestaurants.PopularRestaurantsListener {

    private List<Restaurant> restaurantsList, filterList;
    private CustomFilter filter;
    private final Context context;
    private final RestaurantItemClick listener;
    DecimalFormat df = new DecimalFormat("#.00");
    private PopularRestaurants popularRestaurantsDataAccess;

    private ModelConverter converter;


    private AppDatabase database;


    public RestaurantsAdapter(Context context, RestaurantItemClick listener) {
        restaurantsList = new ArrayList<>();
        filterList = new ArrayList<>();

        this.context = context;
        this.database = AppDatabase.getInstance(this.context);
        this.listener = listener;
        this.converter = ModelConverter.getInstance();

        this.popularRestaurantsDataAccess = new PopularRestaurants(this);
    }


    public void setRestaurantsList(List<Restaurant> restaurantsList) {
        this.restaurantsList.clear();
        this.restaurantsList.addAll(restaurantsList);

        this.filterList.clear();
        this.filterList.addAll(restaurantsList);
        notifyDataSetChanged();
    }

    public void setRestaurants(List<Restaurant> restaurantsList) {
        this.restaurantsList.clear();
        this.restaurantsList.addAll(restaurantsList);

        notifyDataSetChanged();

    }


    @Override
    public RestaurantsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.restaurant_item, parent, false);
        return new RestaurantsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RestaurantsViewHolder holder, int position) {
        final Restaurant restaurant = restaurantsList.get(position);

        if(database.restaurantsDAO().getRestaurant(restaurant.getId()) != null){
            holder.favorites.setImageResource(R.drawable.ic_favorite_black_24dp);
        }

        String title = restaurant.getName();

        holder.restaurantName.setText(title);
        String distance = CalculateDistance.getInstance().getRestaurantDistance(restaurant);
        holder.distance.setText(distance);

        holder.favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFavoriteRestaurant(restaurant, holder);
            }
        });

    }

    @Override
    public int getItemCount() {
        return restaurantsList.size();
    }

    @Override
    public Filter getFilter() {
        if(filter == null){
            filter = new CustomFilter(filterList, this);
        }

        return filter;
    }

    @Override
    public void onRestaurantsLoaded(List<PopularRestaurant> list, boolean hasFailed) {
        if(hasFailed){

        }else{

        }
    }

    public class RestaurantsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView restaurantName, distance;
        ImageView favorites;

        public RestaurantsViewHolder(View view) {
            super(view);
            restaurantName = view.findViewById(R.id.restaurant_name);
            favorites = view.findViewById(R.id.restaurant_favorite_btn);
            distance = view.findViewById(R.id.restaurant_distance);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onRestaurantItemClick(restaurantsList.get(getAdapterPosition()));
        }
    }

    public interface RestaurantItemClick {
        void onRestaurantItemClick(Restaurant restaurant);
    }

    private void toggleFavoriteRestaurant(Restaurant restaurant, RestaurantsViewHolder holder) {

        DatabaseRestaurant convertedRestaurant = converter.convertToDatabaseRestaurant(restaurant);

        if(database.restaurantsDAO().getRestaurant(restaurant.getId()) != null){
            popularRestaurantsDataAccess.removePopularRestaurant(restaurant.getId());
            database.restaurantsDAO().deleteRestaurant(convertedRestaurant);
            Toast.makeText(context, "Restaurant " + restaurant.getName() + " removed from favorite list.", Toast.LENGTH_LONG).show();
            holder.favorites.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }else{
            popularRestaurantsDataAccess.upsertPopularRestaurant(restaurant.getId());
            database.restaurantsDAO().insertRestaurant(convertedRestaurant);
            Toast.makeText(context, "Restaurant " + restaurant.getName() + " added to favorite list.", Toast.LENGTH_LONG).show();
            holder.favorites.setImageResource(R.drawable.ic_favorite_black_24dp);
        }




    }


}
