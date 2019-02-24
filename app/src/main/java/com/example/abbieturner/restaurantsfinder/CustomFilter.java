package com.example.abbieturner.restaurantsfinder;

import android.widget.Filter;

import com.example.abbieturner.restaurantsfinder.Adapters.RestaurantsAdapter;
import com.example.abbieturner.restaurantsfinder.Data.FilterModel;
import com.example.abbieturner.restaurantsfinder.Data.Restaurant;
import com.example.abbieturner.restaurantsfinder.Data.RestaurantModel;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class CustomFilter extends Filter {

    private RestaurantsAdapter adapter;
    private List<RestaurantModel> filterList;
    private FilterModel model;

    public CustomFilter(List<RestaurantModel> filterList, RestaurantsAdapter adapter){
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

            List<RestaurantModel> filteredRestaurans = new ArrayList<>();

            for (int i=0;i<filterList.size();i++)
            {
                RestaurantModel currentRestaurant = filterList.get(i);

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
    private boolean containsName(RestaurantModel restaurant, CharSequence constraint){
        if(restaurant.isFirebaseRestaurant()){
            return restaurant.getFirebaseRestaurant().getName().toUpperCase().contains(constraint);
        }else{
            return restaurant.getZomatoRestaurant().getName().toUpperCase().contains(constraint);
        }

    }
    private boolean fitsDistance(RestaurantModel restaurant)
    {
        Double distance = null;

        if(restaurant.isFirebaseRestaurant()){
            distance = CalculateDistance.getInstance().calcDistance(restaurant.getFirebaseRestaurant().getLat(), restaurant.getFirebaseRestaurant().getLng());
        }else{
            distance = CalculateDistance.getInstance().calcDistance(
                    Double.parseDouble(restaurant.getZomatoRestaurant().getLocation().getLatitude()),
                    Double.parseDouble(restaurant.getZomatoRestaurant().getLocation().getLongitude()));
        }

        return model.getDistance() == 0 || distance.intValue() <= model.getDistance();
    }
    private boolean fitsRating(RestaurantModel restaurant){
        String ratingInString = null;

        if(restaurant.isFirebaseRestaurant()){
            ratingInString = restaurant.getFirebaseRestaurant().getRating().toString();
        }else{
            ratingInString = restaurant.getZomatoRestaurant().getUser_rating().getAggregate_rating();
        }

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

        adapter.setRestaurants((List<RestaurantModel>) results.values);
    }

}
