package com.example.abbieturner.restaurantsfinder.Singletons;

import java.util.Calendar;

public class DateCreated {
    private static DateCreated theSingleton = null;

    public static DateCreated getInstance() {
        if (theSingleton == null) {
            theSingleton = new DateCreated();
        }
        return theSingleton;
    }

    public String GetDateCreated() {
        Calendar c = Calendar.getInstance();
        String dateCreated = c.get(Calendar.DAY_OF_MONTH) + " " + getFullMonth(c.get(Calendar.MONTH)) + ", " + c.get(Calendar.YEAR);

        return dateCreated;
    }

    private String getFullMonth(int month) {
        String[] list = new String[]{"January",
                "February",
                "March",
                "April",
                "May",
                "June",
                "July",
                "August",
                "September",
                "October",
                "November",
                "December"};

        return list[month];
    }
}