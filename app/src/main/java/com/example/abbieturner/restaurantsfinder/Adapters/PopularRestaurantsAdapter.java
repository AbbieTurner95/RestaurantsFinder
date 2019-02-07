package com.example.abbieturner.restaurantsfinder.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.abbieturner.restaurantsfinder.FirebaseModels.PopularRestaurant;
import com.example.abbieturner.restaurantsfinder.R;

import java.util.ArrayList;
import java.util.List;

public class PopularRestaurantsAdapter extends RecyclerView.Adapter<PopularRestaurantsAdapter.PopularRestaurantViewHolder>{
    private List<PopularRestaurant> popularRestaurantsList;
    private final RestaurantItemClick listener;

    public PopularRestaurantsAdapter(RestaurantItemClick listener) {
        popularRestaurantsList = new ArrayList<>();
        this.listener = listener;
    }

    public void setList(List<PopularRestaurant> popularRestaurantsList) {
        this.popularRestaurantsList.clear();
        this.popularRestaurantsList.addAll(popularRestaurantsList);
        notifyDataSetChanged();
    }

    @Override
    public PopularRestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_item, parent, false);
        return new PopularRestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PopularRestaurantViewHolder holder, int position) {
        PopularRestaurant restaurant = popularRestaurantsList.get(position);

        holder.name.setText(restaurant.getName());

    }

    @Override
    public int getItemCount() {
        return popularRestaurantsList.size();
    }


    public class PopularRestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;


        public PopularRestaurantViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.restaurant_name);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onRestaurantItemClick(popularRestaurantsList.get(getAdapterPosition()));
        }
    }

    public interface RestaurantItemClick {
        void onRestaurantItemClick(PopularRestaurant review);
    }

}
