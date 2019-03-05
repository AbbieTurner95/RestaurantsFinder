package com.example.abbieturner.restaurantsfinder.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abbieturner.restaurantsfinder.Activities.HomeActivity;
import com.example.abbieturner.restaurantsfinder.Activities.SettingsActivity;
import com.example.abbieturner.restaurantsfinder.R;
import com.example.abbieturner.restaurantsfinder.Singletons.DeviceLocation;
import com.example.abbieturner.restaurantsfinder.Singletons.LocationSharedPreferences;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GetLocationDialog implements OnMapReadyCallback {
    private AlertDialog dialog;
    private Context context;
    private LayoutInflater inflater;
    private GoogleMap mMap;
    private DeviceLocation locationSingleton;
    private LatLng location;
    private boolean isSettingActivity;
    private LocationSharedPreferences locationSharedPreferences;

    public GetLocationDialog(Context context, boolean isSettingActivity) {
        this.isSettingActivity = isSettingActivity;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        locationSingleton = DeviceLocation.getInstance();

        createDialog();
    }

    private void createDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        View mView = inflater.inflate(R.layout.dialog_get_location, null);

        locationSharedPreferences = LocationSharedPreferences.getInstance();

        setUpMap();

        ImageView close = mView.findViewById(R.id.btn_close_dialog);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideDialog();
            }
        });

        TextView btnCancel = mView.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideDialog();
            }
        });

        mBuilder.setView(mView);
        dialog = mBuilder.create();

        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.9f;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
    }

    private void setUpMap() {
        if (isSettingActivity) {
            SupportMapFragment mapFragment = (SupportMapFragment) ((SettingsActivity) context).getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        } else {
            SupportMapFragment mapFragment = (SupportMapFragment) ((HomeActivity) context).getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
    }

    public void showDialog() {
        dialog.show();
    }

    public void hideDialog() {
        dialog.hide();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (locationSharedPreferences.userHasLocationsSet()) {
            addMarker();
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                location = latLng;
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title("My location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        });
    }

    private void addMarker() {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(locationSharedPreferences.getLocation()).title("My location"));
    }
}
