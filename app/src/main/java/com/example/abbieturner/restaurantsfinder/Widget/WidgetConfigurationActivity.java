package com.example.abbieturner.restaurantsfinder.Widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.RemoteViews;

import com.example.abbieturner.restaurantsfinder.Adapters.FavouriteAdapter;
import com.example.abbieturner.restaurantsfinder.Data.Restaurant;
import com.example.abbieturner.restaurantsfinder.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WidgetConfigurationActivity extends AppCompatActivity implements FavouriteAdapter.RestaurantItemClick {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.widget_recyclerview)
    RecyclerView recyclerView;

    int appwidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private AppWidgetManager widgetManager;
    private RemoteViews views;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widgetconfig);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.app_name));
            toolbar.setTitleTextColor(Color.WHITE);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        widgetManager = AppWidgetManager.getInstance(this);
        views = new RemoteViews(this.getPackageName(), R.layout.restaurant_widget);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            appwidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (appwidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
    }

    @Override
    public void onRestaurantItemClick(Restaurant restaurant) {
        views.setTextViewText(R.id.name_rest, restaurant.getName());
        views.setTextViewText(R.id.address_rest, restaurant.getLocation().getAddress());

        widgetManager.updateAppWidget(appwidgetId, views);
        Intent outcome = new Intent();
        outcome.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appwidgetId);
        setResult(RESULT_OK, outcome);
        finish();
    }
}