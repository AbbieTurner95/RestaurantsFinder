package com.example.abbieturner.restaurantsfinder.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abbieturner.restaurantsfinder.Dialogs.CloseCreateRestaurantDialog;
import com.example.abbieturner.restaurantsfinder.FirebaseAccess.Listeners.RestaurantListener;
import com.example.abbieturner.restaurantsfinder.FirebaseModels.Restaurant;
import com.example.abbieturner.restaurantsfinder.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateRestaurantActivity extends AppCompatActivity implements
        OnMapReadyCallback, RestaurantListener{

    private CloseCreateRestaurantDialog closeConfirmationDialog;
    private Restaurant newRestaurant;
    private GoogleMap mMap;
    private MarkerOptions markerOptions;
    private final float ZOOM_LEVEL = 11;
    private com.example.abbieturner.restaurantsfinder.FirebaseAccess.Restaurant restaurantDataAccess;
    private ProgressDialog progressDialog;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private FirebaseAuth mAuth;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.transparent_image)
    ImageView transparentImageView;
    @BindView(R.id.main_scroll_view)
    ScrollView mainScrollView;

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_web_url)
    TextView tvWebUrl;
    @BindView(R.id.tv_menu_url)
    TextView tvMenuUrl;
    @BindView(R.id.checkBox_delivery)
    CheckBox checkBoxDelivery;

    @BindView(R.id.checkBox_step_free_access)
    CheckBox checkBoxStepFreeAccess;
    @BindView(R.id.checkBox_accessible_toilets)
    CheckBox checkBoxAccessibleToilets;
    @BindView(R.id.checkBox_vegan)
    CheckBox checkBoxVegan;
    @BindView(R.id.checkBox_vegetarian)
    CheckBox checkBoxVegetarian;
    @BindView(R.id.checkBox_gluten_free)
    CheckBox checkBoxGlutenFree;
    @BindView(R.id.checkBox_dairy_free)
    CheckBox checkBoxDairyFree;

    @BindView(R.id.btn_take_photo)
    LinearLayout btnTakePhoto;
    @BindView(R.id.image_view_photo)
    ImageView imgPhoto;
    @BindView(R.id.rl_photo_holder)
    RelativeLayout rlPhotoHolder;
    @BindView(R.id.btn_remove_photo)
    ImageView btnRemovePhoto;
    @BindView(R.id.cuisine_spinner)
    Spinner cuisineSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_restaurant);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        setUpSpinner();
        setUpListeners();
        createNewInstances();
        setUpToolbar();
        setUpMap();
    }

    private void setUpSpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.cuisines_list, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        cuisineSpinner.setAdapter(adapter);
    }

    private void setUpListeners(){
        checkBoxDelivery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                newRestaurant.setDelivery(isChecked);
            }
        });

        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });
        btnRemovePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newRestaurant.setPicture(null);
                imgPhoto.setImageResource(android.R.color.transparent);
                rlPhotoHolder.setVisibility(View.GONE);
                btnTakePhoto.setVisibility(View.VISIBLE);
            }
        });
        cuisineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCuisine = parent.getItemAtPosition(position).toString();

                newRestaurant.setCuisine(selectedCuisine);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        checkBoxStepFreeAccess.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                newRestaurant.setStepsFreeAccess(isChecked);
            }
        });

        checkBoxAccessibleToilets.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                newRestaurant.setAccessibleToilets(isChecked);
            }
        });

        checkBoxVegan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                newRestaurant.setVegan(isChecked);
            }
        });

        checkBoxVegetarian.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                newRestaurant.setVegetarian(isChecked);
            }
        });

        checkBoxGlutenFree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                newRestaurant.setGlutenFree(isChecked);
            }
        });

        checkBoxDairyFree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                newRestaurant.setDairyFree(isChecked);
            }
        });
    }

    private void createNewInstances(){
        closeConfirmationDialog = new CloseCreateRestaurantDialog(this);
        newRestaurant = new Restaurant();
        transparentImageView.setOnTouchListener(onTouchListener);
        restaurantDataAccess = new com.example.abbieturner.restaurantsfinder.FirebaseAccess.Restaurant(this);
        progressDialog = new ProgressDialog(CreateRestaurantActivity.this);
    }

    private void setUpMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(CreateRestaurantActivity.this);
    }

    private void setUpToolbar(){
        setSupportActionBar(toolbar);
        toolbar.setTitle("Create Restaurant");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCloseConformationDialog();
            }
        });
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_restaurant_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_confirm) {
            handleSave();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        openCloseConformationDialog();
    }

    private void openCloseConformationDialog(){
        closeConfirmationDialog.showDialog();
    }

    private void handleSave(){
        getDataFromUI();
        if(requiredDataSet()){
            showProgressDialog();
            restaurantDataAccess.createRestaurant(newRestaurant);
        }else{
            setErrors();
        }
    }

    private void setErrors(){
        if(!isNameSet()){
            tvName.setError("Name is required!");
        }

        if(!isAddressSet()){
            tvAddress.setError("Address is required!");
        }

        if(!isLocationSet()){
            Toast.makeText(CreateRestaurantActivity.this, "Map location is required", Toast.LENGTH_LONG).show();
        }

        if(!isCuisineSet()){
            setSpinnerError(cuisineSpinner, "Cuisine is required!");
        }
    }

    private void setSpinnerError(Spinner spinner, String error){
        View selectedView = spinner.getSelectedView();
        if (selectedView != null && selectedView instanceof TextView) {
            spinner.requestFocus();
            TextView selectedTextView = (TextView) selectedView;
            selectedTextView.setError(error); // any name of the error will do
            selectedTextView.setTextColor(Color.RED); //text color in which you want your error message to be displayed
            selectedTextView.setText(error); // actual error message
            spinner.performClick(); // to open the spinner list if error is found.
        }
    }

    private boolean requiredDataSet(){
        return isNameSet() && isLocationSet() && isAddressSet() && isCuisineSet();
    }

    private boolean isCuisineSet(){
        return newRestaurant.getCuisine() != null && !newRestaurant.getCuisine().isEmpty();
    }

    private boolean isLocationSet(){
        return newRestaurant.getLat() != null && newRestaurant.getLng() != null;
    }

    private boolean isNameSet(){
        return !tvName.getText().toString().isEmpty();
    }

    private boolean isAddressSet(){
        return !tvAddress.getText().toString().isEmpty();
    }


    private void getDataFromUI(){
        newRestaurant.setName(tvName.getText().toString());
        newRestaurant.setAddress(tvAddress.getText().toString());
        newRestaurant.setPhone(tvPhone.getText().toString());
        newRestaurant.setMenu(tvMenuUrl.getText().toString());
        newRestaurant.setWeb(tvWebUrl.getText().toString());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        markerOptions = new MarkerOptions();

        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Toast.makeText(CreateRestaurantActivity.this, "Latitude and Longitude set",  Toast.LENGTH_SHORT).show();

                mMap.clear();

                markerOptions.position(latLng);
                mMap.addMarker(markerOptions);

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_LEVEL));

                newRestaurant.setLat(latLng.latitude);
                newRestaurant.setLng(latLng.longitude);
            }
        });
    }

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    // Disallow ScrollView to intercept touch events.
                    mainScrollView.requestDisallowInterceptTouchEvent(true);

                    // Disable touch on transparent view
                    return false;

                case MotionEvent.ACTION_UP:
                    // Allow ScrollView to intercept touch events.
                    mainScrollView.requestDisallowInterceptTouchEvent(false);
                    return true;

                case MotionEvent.ACTION_MOVE:
                    mainScrollView.requestDisallowInterceptTouchEvent(true);
                    return false;

                default:
                    return true;
            }
        }
    };

    @Override
    public void onRestaurantCreated(boolean hasFailed) {
        hideProgressDialog();
        if(hasFailed){
            Toast.makeText(CreateRestaurantActivity.this, "Something went wrong. Please try again later.", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(CreateRestaurantActivity.this, "Restaurant created.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onRestaurantLoaded(Restaurant restaurant, boolean hasFailed) {

    }

    private void showProgressDialog(){
        progressDialog.setTitle("Creating Restaurant");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    private void hideProgressDialog(){
        progressDialog.hide();
    }

    public void openCamera(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            newRestaurant.setPicture(imageBitmap);
            imgPhoto.setImageBitmap(imageBitmap);
            rlPhotoHolder.setVisibility(View.VISIBLE);
            btnTakePhoto.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
