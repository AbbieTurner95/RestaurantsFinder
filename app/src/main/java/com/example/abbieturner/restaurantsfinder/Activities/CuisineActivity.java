package com.example.abbieturner.restaurantsfinder.Activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.abbieturner.restaurantsfinder.Adapters.CuisineAdapter;
import com.example.abbieturner.restaurantsfinder.Data.Cuisine;
import com.example.abbieturner.restaurantsfinder.Data.CuisinesSingleton;
import com.example.abbieturner.restaurantsfinder.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CuisineActivity extends BaseActivity implements CuisineAdapter.CuisineItemClick, NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.adView)
    AdView mAdView;
    @BindView(R.id.cuisines_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private CuisineAdapter cuisineAdapter;
    private LinearLayoutManager layoutManager;
    private String TAG_USER_ID;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drawer_layout_cuisines);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        setUpNavigationDrawer();
        mAuth = FirebaseAuth.getInstance();
        this.setTitle(R.string.title_cuisines);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        TAG_USER_ID = getResources().getString(R.string.TAG_USER_ID);
        currentUser = mAuth.getCurrentUser();

        navigationView.setNavigationItemSelectedListener(this);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        cuisineAdapter = new CuisineAdapter(this, this);
        recyclerView.setAdapter(cuisineAdapter);
        cuisineAdapter.setCuisineList(CuisinesSingleton.getInstance().getCuisines());

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setEnterExitTransition(Intent intent) {
        getWindow().setExitTransition(new Fade().setDuration(1000));
        getWindow().setReenterTransition(new Fade().setDuration(1000));
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(CuisineActivity.this).toBundle());
    }

    @Override
    public void onCuisineItemClick(Cuisine cuisines) {
        int id = cuisines.getCuisine_id();
        String name = cuisines.getCuisine_name();

        Intent intent = new Intent(CuisineActivity.this, RestaurantsActivity.class);
        intent.putExtra("cuisine_id", id);
        intent.putExtra(getResources().getString(R.string.TAG_CUISINE_NAME), name);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void setUpNavigationDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_fave) {
            Intent intent = new Intent(this, FavouritesActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Hey check out this cool restaurant finder app!";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Restaurant Finder!");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));

        } else if (id == R.id.nav_contact) {
            new LovelyStandardDialog(this, LovelyStandardDialog.ButtonLayout.VERTICAL)
                    .setTopColorRes(R.color.design_default_color_primary)
                    .setButtonsColorRes(R.color.white)
                    .setIcon(R.drawable.phone_black_24dp)
                    .setTitle("Select a contact method.")
                    .setMessage("How do you wish to contact us?")
                    .setButtonsColor(getResources().getColor(R.color.colorPrimary))
                    .setPositiveButton("Email", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                    "mailto", "info@restaurantfinder.com", null));
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                            emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                            startActivity(Intent.createChooser(emailIntent, "Send us an Email"));

                        }
                    })
                    .setNegativeButton("Phone Us", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:01145627382"));
                            startActivity(intent);
                        }
                    })
                    .show();

        } else if (id == R.id.nav_loginout) {
            if (mAuth != null) {
                mAuth.signOut();
                AuthUI.getInstance().signOut(getApplicationContext());
                Intent intent = new Intent(this, LogInActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Not Logged In.", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.action_settings) {
            Intent intent = new Intent(CuisineActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_profile){
                if(currentUser != null){
                    Intent intent = new Intent(CuisineActivity.this, Profile.class);
                    intent.putExtra(TAG_USER_ID, mAuth.getCurrentUser().getUid());
                    startActivity(intent);
                }else{
                    Toast.makeText(CuisineActivity.this, "Login required!", Toast.LENGTH_LONG).show();
                }
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}