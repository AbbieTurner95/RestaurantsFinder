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
import com.example.abbieturner.restaurantsfinder.Data.RestaurantModel;
import com.example.abbieturner.restaurantsfinder.Database.AppDatabase;
import com.example.abbieturner.restaurantsfinder.DatabaseModels.DatabaseRestaurant;
import com.example.abbieturner.restaurantsfinder.FirebaseAccess.PopularRestaurants;
import com.example.abbieturner.restaurantsfinder.FirebaseModels.PopularRestaurant;
import com.example.abbieturner.restaurantsfinder.R;
import com.example.abbieturner.restaurantsfinder.PicassoLoader;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.views.AvatarView;

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.RestaurantsViewHolder> implements Serializable, Filterable, PopularRestaurants.PopularRestaurantsListener {

    private List<RestaurantModel> restaurantsList, filterList;
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


    public void setRestaurantsList(List<Restaurant> zomatoRestaurants, List<com.example.abbieturner.restaurantsfinder.FirebaseModels.Restaurant> firebaseRestaurants) {
        restaurantsList.clear();
        filterList.clear();

        if (firebaseRestaurants != null && firebaseRestaurants.size() > 0) {
            for (com.example.abbieturner.restaurantsfinder.FirebaseModels.Restaurant restaurant : firebaseRestaurants) {
                restaurantsList.add(new RestaurantModel(restaurant));
                filterList.add(new RestaurantModel(restaurant));
            }
        }

        if (zomatoRestaurants != null && zomatoRestaurants.size() > 0) {
            for (Restaurant r : zomatoRestaurants) {
                restaurantsList.add(new RestaurantModel(r));
                filterList.add(new RestaurantModel(r));
            }
        }

        notifyDataSetChanged();
    }

    public void setRestaurants(List<RestaurantModel> restaurantsList) {
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
        final RestaurantModel restaurant = restaurantsList.get(position);

        if (restaurant.isFirebaseRestaurant()) {
            com.example.abbieturner.restaurantsfinder.FirebaseModels.Restaurant fr = restaurant.getFirebaseRestaurant();

            if (database.restaurantsDAO().getRestaurant(fr.getId()) != null) {
                holder.favorites.setImageResource(R.drawable.ic_favorite_black_24dp);
            }

            String title = fr.getName();
            holder.restaurantName.setText(title);

            String distance = CalculateDistance.getInstance().getRestaurantDistance(fr.getLat(), fr.getLng());
            holder.distance.setText(distance);

            String rating = fr.getRating().toString();
            holder.rating.setText("Rating: " + rating);

            holder.imageLoader = new PicassoLoader();
            holder.imageLoader.loadImage(holder.avatarView, restaurant.getFirebaseRestaurant().getPictureUrl(), restaurant.getFirebaseRestaurant().getName());

            holder.favorites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleFavoriteRestaurant(restaurant, holder);
                }
            });
        } else {
            final Restaurant zr = restaurant.getZomatoRestaurant();

            if (database.restaurantsDAO().getRestaurant(zr.getId()) != null) {
                holder.favorites.setImageResource(R.drawable.ic_favorite_black_24dp);
            }

            String title = zr.getName();
            holder.restaurantName.setText(title);

            String distance = CalculateDistance.getInstance().getRestaurantDistance(
                    Double.parseDouble(zr.getLocation().getLatitude()),
                    Double.parseDouble(zr.getLocation().getLongitude()));
            holder.distance.setText(distance);

            String rating = zr.getUser_rating().getAggregate_rating();
            holder.rating.setText("Rating: " + rating);

            holder.imageLoader = new PicassoLoader();
            holder.imageLoader.loadImage(holder.avatarView, "sgsd", restaurant.getZomatoRestaurant().getName());

            holder.favorites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleFavoriteRestaurant(restaurant, holder);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return restaurantsList.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new CustomFilter(filterList, this);
        }

        return filter;
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

    public class RestaurantsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView restaurantName, distance, rating;
        ImageView favorites;
        AvatarView avatarView;
        IImageLoader imageLoader;

        public RestaurantsViewHolder(View view) {
            super(view);
            restaurantName = view.findViewById(R.id.restaurant_name);
            favorites = view.findViewById(R.id.restaurant_favorite_btn);
            distance = view.findViewById(R.id.restaurant_distance);
            rating = view.findViewById(R.id.restaurant_rating);
            avatarView = view.findViewById(R.id.avatar_view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onRestaurantItemClick(restaurantsList.get(getAdapterPosition()));
        }
    }

    public interface RestaurantItemClick {
        void onRestaurantItemClick(RestaurantModel restaurant);
    }

    private void toggleFavoriteRestaurant(RestaurantModel restaurant, RestaurantsViewHolder holder) {

        if (restaurant.isFirebaseRestaurant()) {
            DatabaseRestaurant convertedRestaurant = converter.convertToDatabaseRestaurant(restaurant.getFirebaseRestaurant());

            if (database.restaurantsDAO().getRestaurant(restaurant.getId()) != null) {
                popularRestaurantsDataAccess.removePopularRestaurant(convertedRestaurant.getId());
                database.restaurantsDAO().deleteRestaurant(convertedRestaurant);
                Toast.makeText(context, "Restaurant " + restaurant.getName() + " removed from favorite list.", Toast.LENGTH_LONG).show();
                holder.favorites.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            } else {
                popularRestaurantsDataAccess.upsertPopularRestaurant(convertedRestaurant.getId(), convertedRestaurant.getName(), convertedRestaurant.getPhotos_url());
                database.restaurantsDAO().insertRestaurant(convertedRestaurant);
                Toast.makeText(context, "Restaurant " + restaurant.getName() + " added to favorite list.", Toast.LENGTH_LONG).show();
                holder.favorites.setImageResource(R.drawable.ic_favorite_black_24dp);
            }
        } else {
            DatabaseRestaurant convertedRestaurant = converter.convertToDatabaseRestaurant(restaurant.getZomatoRestaurant());

            if (database.restaurantsDAO().getRestaurant(restaurant.getId()) != null) {
                popularRestaurantsDataAccess.removePopularRestaurant(restaurant.getId());
                database.restaurantsDAO().deleteRestaurant(convertedRestaurant);
                Toast.makeText(context, "Restaurant " + restaurant.getName() + " removed from favorite list.", Toast.LENGTH_LONG).show();
                holder.favorites.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            } else {
                popularRestaurantsDataAccess.upsertPopularRestaurant(convertedRestaurant.getId(), convertedRestaurant.getName(), convertedRestaurant.getPhotos_url());
                database.restaurantsDAO().insertRestaurant(convertedRestaurant);
                Toast.makeText(context, "Restaurant " + restaurant.getName() + " added to favorite list.", Toast.LENGTH_LONG).show();
                holder.favorites.setImageResource(R.drawable.ic_favorite_black_24dp);
            }
        }
    }
}