package com.example.abbieturner.restaurantsfinder.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.example.abbieturner.restaurantsfinder.PicassoLoader;
import com.example.abbieturner.restaurantsfinder.R;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.views.AvatarView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class Profile extends AppCompatActivity {

    @BindView(R.id.avatar_personal_photo)
    AvatarView avatarPersonalPhoto;

    private IImageLoader imageLoader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_bar_profile);
        ButterKnife.bind(this);

        setNewInstances();

        imageLoader.loadImage(avatarPersonalPhoto, "empty url", "Name");
    }

    private void setNewInstances(){
        imageLoader = new PicassoLoader();
    }
}
