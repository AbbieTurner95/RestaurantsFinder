package com.example.abbieturner.restaurantsfinder;

import android.widget.Filter;

import com.example.abbieturner.restaurantsfinder.Adapters.RestaurantsAdapter;
import com.example.abbieturner.restaurantsfinder.Data.FilterModel;
import com.example.abbieturner.restaurantsfinder.Data.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class CustomFilter extends Filter {

    private RestaurantsAdapter adapter;
    private List<Restaurant> filterList;
    private FilterModel model;

    public CustomFilter(List<Restaurant> filterList, RestaurantsAdapter adapter){
        this.adapter=adapter;
        this.filterList=filterList;
        model = FilterModel.getInstance();
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();

        constraint = model.getSearch();

        if(isSearchSet(constraint) || isDistanceSet()){
            constraint=constraint.toString().toUpperCase();

            List<Restaurant> filteredRestaurans = new ArrayList<>();

            for (int i=0;i<filterList.size();i++)
            {
                Restaurant currentRestaurant = filterList.get(i);

                if(containsName(currentRestaurant, constraint) && fitsDistance(currentRestaurant))
                {
                    //ADD RESTAURANT TO FILTERED
                    filteredRestaurans.add(filterList.get(i));
                }
            }

            results.count=filteredRestaurans.size();
            results.values=filteredRestaurans;
        }else{
            results.count=filterList.size();
            results.values=filterList;
        }

        return results;
    }

    private boolean isDistanceSet(){
        return (model.getDistance() > 0);
    }
    private boolean isSearchSet(CharSequence constraint){
        return (constraint != null && constraint.length() > 0);
    }

    private boolean containsName(Restaurant restaurant, CharSequence constraint){
        return restaurant.getName().toUpperCase().contains(constraint);
    }

    private boolean fitsDistance(Restaurant restaurant)
    {
        Double distance = CalculateDistance.getInstance().calcDistance(restaurant);

        return model.getDistance() == 0 || distance.intValue() <= model.getDistance();
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
//        adapter.players= (List<Restaurant>) results.values;
//
//        //REFRESH
//        adapter.notifyDataSetChanged();

        adapter.setRestaurants((List<Restaurant>) results.values);
    }

}
