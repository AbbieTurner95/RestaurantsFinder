package com.example.abbieturner.restaurantsfinder.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.abbieturner.restaurantsfinder.PicassoLoader;
import com.example.abbieturner.restaurantsfinder.R;
import com.example.abbieturner.restaurantsfinder.Data.Cuisine;

import java.util.ArrayList;
import java.util.List;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.views.AvatarView;

public class CuisineAdapter extends RecyclerView.Adapter<CuisineAdapter.CuisineViewHolder> {

    private List<Cuisine> cuisineList;
    private final Context context;
    private final CuisineItemClick listener;

    public CuisineAdapter(Context context, CuisineItemClick listener) {
        cuisineList = new ArrayList<>();
        this.context = context;
        this.listener = listener;
    }

    public void setCuisineList(List<Cuisine> cuisineList) {
        this.cuisineList.clear();
        this.cuisineList.addAll(cuisineList);
        notifyDataSetChanged();
    }

    @Override
    public CuisineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cuisine_item, parent, false);
        return new CuisineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CuisineViewHolder holder, int position) {
        Cuisine cuisine = cuisineList.get(position);

        String title = cuisine.getCuisine_name();
        holder.cuisineTitle.setText(title);

        holder.imageLoader = new PicassoLoader();
        holder.imageLoader.loadImage(holder.avatarView, "sgsd", title);
    }

    @Override
    public int getItemCount() {
        return cuisineList.size();
    }

    public class CuisineViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView cuisineTitle;
        AvatarView avatarView;
        IImageLoader imageLoader;

        public CuisineViewHolder(View view) {
            super(view);
            cuisineTitle = view.findViewById(R.id.cuisine_title);
            avatarView = view.findViewById(R.id.avatar_view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onCuisineItemClick(cuisineList.get(getAdapterPosition()));
        }
    }

    public interface CuisineItemClick {
        void onCuisineItemClick(Cuisine cuisines);
    }
}
