package com.example.abbieturner.restaurantsfinder;

import android.widget.Filter;

import com.example.abbieturner.restaurantsfinder.Adapters.RestaurantsAdapter;
import com.example.abbieturner.restaurantsfinder.Data.FilterModel;
import com.example.abbieturner.restaurantsfinder.Data.Restaurant;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
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

        if(isSearchSet(constraint) || isDistanceSet() || isRatingSet()){
            constraint=constraint.toString().toUpperCase();

            List<Restaurant> filteredRestaurans = new ArrayList<>();

            for (int i=0;i<filterList.size();i++)
            {
                Restaurant currentRestaurant = filterList.get(i);

                if(containsName(currentRestaurant, constraint) && fitsDistance(currentRestaurant) && fitsRating(currentRestaurant))
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

    private boolean isRatingSet(){
        return model.getRating() > 0;
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
    private boolean fitsRating(Restaurant restaurant){
        String ratingInString = restaurant.getUser_rating().getAggregate_rating();
        return stringToDouble(ratingInString) > model.getRating();
    }

    private double stringToDouble(String number){
        DecimalFormat df = new DecimalFormat();
        DecimalFormatSymbols sfs = new DecimalFormatSymbols();
        sfs.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(sfs);
        try{
            double d = df.parse(number).doubleValue();
            return d;
        }catch(ParseException e){
            return 0.0;
        }
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
