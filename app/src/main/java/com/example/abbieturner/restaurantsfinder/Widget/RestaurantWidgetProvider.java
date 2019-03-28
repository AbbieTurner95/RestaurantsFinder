package com.example.abbieturner.restaurantsfinder.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.abbieturner.restaurantsfinder.Activities.CuisineActivity;
import com.example.abbieturner.restaurantsfinder.Activities.LogInActivity;
import com.example.abbieturner.restaurantsfinder.R;
import com.example.abbieturner.restaurantsfinder.Utils.SharedPref;

public class RestaurantWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        SharedPref sharedPref;
        Intent intent;
        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.restaurant_widget_provider);

        sharedPref = new SharedPref(context);


        if (sharedPref.isUserLogged()) {
            intent = new Intent(context, CuisineActivity.class);
        } else {
            intent = new Intent(context, LogInActivity.class);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        views.setOnClickPendingIntent(R.id.restaurant_icon_widget, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}