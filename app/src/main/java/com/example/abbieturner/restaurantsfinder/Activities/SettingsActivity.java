package com.example.abbieturner.restaurantsfinder.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abbieturner.restaurantsfinder.Dialogs.GetLocationDialog;
import com.example.abbieturner.restaurantsfinder.R;
import com.example.abbieturner.restaurantsfinder.Singletons.DeviceLocation;
import com.example.abbieturner.restaurantsfinder.Singletons.LocationSharedPreferences;
import com.example.abbieturner.restaurantsfinder.Utils.SharedPref;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SettingsActivity extends BaseActivity {

    private FirebaseAuth mAuth;
    private GetLocationDialog locationDialog;
    private Gson gson;
    private SharedPreferences sharedPreferences;
    private LocationSharedPreferences locationSharedPreferences;
    private String SHARED_PREFERENCES_DEFAULT_LOCATION;
    private String location_shared_preferences_name;
    private SharedPref sharedPref;

    @BindView(R.id.btn_manage_default_location)
    TextView btnManageDefaultLocation;
    @BindView(R.id.default_location_status)
    TextView defaultLocationStatus;
    @BindView(R.id.lang_spinner)
    Spinner lang_spinner;
    @BindView(R.id.btn_submit)
    Button btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        sharedPref = new SharedPref(this);

        ActionBar actionBar = this.getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setNewInstances();
        setListeners();
    }

    public void languageSetting() {
        String lang = lang_spinner.getSelectedItem().toString();

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences lang_pref = getSharedPreferences("language", MODE_PRIVATE);
        SharedPreferences.Editor editor = lang_pref.edit();
        editor.putString("languageToLoad", lang);
        editor.apply();
    }


    @Override
    public void onResume() {
        super.onResume();

        if (locationSharedPreferences.userHasLocationsSet()) {
            defaultLocationStatus.setText("location set");
        } else {
            defaultLocationStatus.setText("location not set");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void setListeners() {
        btnManageDefaultLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUserLoggedIn()) {
                    locationDialog.showDialog();
                } else {
                    Toast.makeText(SettingsActivity.this, "Log In Required", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setNewInstances() {
        location_shared_preferences_name = getResources().getString(R.string.location_shared_preferences_name);
        mAuth = FirebaseAuth.getInstance();
        locationDialog = new GetLocationDialog(this, true);
        DeviceLocation locationSingleton = DeviceLocation.getInstance();
        gson = new Gson();
        sharedPreferences = this.getSharedPreferences(location_shared_preferences_name, Context.MODE_PRIVATE);
        locationSharedPreferences = LocationSharedPreferences.getInstance();
        SHARED_PREFERENCES_DEFAULT_LOCATION = getResources().getString(R.string.SHARED_PREFERENCES_DEFAULT_LOCATION);


        final String[] stringArray = getResources().getStringArray(R.array.languages_settings);


        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, stringArray);

        lang_spinner.setAdapter(adapter);
        lang_spinner.setSelection(adapter.getPosition(getLang()));


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (lang_spinner.getSelectedItemPosition()) {
                    case 0:
                        sharedPref.setLang("en");
                        finish();
                        startActivity(new Intent(getApplicationContext(), CuisineActivity.class));
                        break;
                    case 1:
                        sharedPref.setLang("fr");
                        finish();
                        startActivity(new Intent(getApplicationContext(), CuisineActivity.class));
                        break;
                    case 2:
                        sharedPref.setLang("de");
                        finish();
                        startActivity(new Intent(getApplicationContext(), CuisineActivity.class));
                        break;
                    case 3:
                        sharedPref.setLang("it");
                        finish();
                        startActivity(new Intent(getApplicationContext(), CuisineActivity.class));
                        break;
                    case 4:
                        sharedPref.setLang("es");
                        finish();
                        startActivity(new Intent(getApplicationContext(), CuisineActivity.class));
                        break;
                }
            }
        });
    }


    private String getLang() {
        switch (sharedPref.getLang()) {
            case "en":
                return "English";
            case "fr":
                return "French";
            case "de":
                return "German";
            case "it":
                return "Italian";
            case "es":
                return "Spanish";
            default:
                return "";
        }
    }

    public void locationSetFromUser(LatLng location) {
        locationSharedPreferences.setLocation(location);

        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        if (locationSharedPreferences.userHasLocationsSet()) {
            String json = gson.toJson(locationSharedPreferences.getUsersLocation());
            prefsEditor.putString(getLocationPreferencesKey(), json);
            prefsEditor.commit();
        }
    }

    private boolean isUserLoggedIn() {
        return mAuth.getCurrentUser() != null;
    }

    private String getLocationPreferencesKey() {
        return mAuth.getUid() + SHARED_PREFERENCES_DEFAULT_LOCATION;
    }
}