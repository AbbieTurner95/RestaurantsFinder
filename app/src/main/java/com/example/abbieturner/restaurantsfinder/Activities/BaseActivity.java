package com.example.abbieturner.restaurantsfinder.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class BaseActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onPause() {
        mAuth.signOut();
        finish();
        super.onPause();
    }

    @Override
    protected void onStop() {
        mAuth.signOut();
        finish();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mAuth.signOut();
        finish();
        super.onDestroy();
    }
}
