package com.example.abbieturner.restaurantsfinder.Data;

public class UsersLocation {
    private static double lat = 53.479207; //TODO change for real lat
    private static double lng = -1.186169; //TODO change for real lng

    public static double getLat(){
        return lat;
    }
    public static double getLng(){
        return lng;
    }
    public static void setLat(double newLat){
        lat = newLat;
    }
    public static void setLng(double newLng){
        lng = newLng;
    }


    public static double getDistance(double lat2, double lon2) {
        // unit M - miles, K - kilometres, N - nautical
        final String unit = "M";
        if ((lat == lat2) && (lng == lon2)) {
            return 0;
        }
        else {
            double theta = lng - lon2;
            double dist = Math.sin(Math.toRadians(lat)) * Math.sin(Math.toRadians(lat2)) +
                    Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            if (unit == "K") {
                dist = dist * 1.609344;
            } else if (unit == "N") {
                dist = dist * 0.8684;
            }
            return (dist);
        }
    }
}
