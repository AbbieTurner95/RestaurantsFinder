package com.example.abbieturner.restaurantsfinder.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.abbieturner.restaurantsfinder.Dialogs.CloseActivityConfirmationDialog;
import com.example.abbieturner.restaurantsfinder.FirebaseAccess.Listeners.UserListener;
import com.example.abbieturner.restaurantsfinder.FirebaseAccess.User;
import com.example.abbieturner.restaurantsfinder.FirebaseModels.UserFirebaseModel;
import com.example.abbieturner.restaurantsfinder.R;
import com.example.abbieturner.restaurantsfinder.Singletons.UserInstance;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditProfile extends AppCompatActivity implements UserListener {

    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;
    @BindView(R.id.tv_name)
    EditText et_name;
    @BindView(R.id.ll_choose_photo)
    LinearLayout btnChoosePhoto;
    @BindView(R.id.rl_photo)
    RelativeLayout photoLayout;
    @BindView(R.id.iv_photo)
    ImageView photoView;
    @BindView(R.id.iv_clear_photo)
    ImageView btnClearPhoto;

    private UserFirebaseModel user;
    private CloseActivityConfirmationDialog closeConfirmationDialog;
    private User userDataAccess;
    private String errorMsg;
    private ProgressDialog saveDialog;
    private final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);

        createNewInstances();
        setListeners();
        setUpToolBar();
        loadDataToUI();
    }

    @Override
    public void onBackPressed() {
        openCloseConformationDialog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_restaurant_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.action_confirm:
                handleSave();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setListeners(){
        btnClearPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnChoosePhoto.setVisibility(View.VISIBLE);
                photoLayout.setVisibility(View.GONE);
                photoView.setImageResource(android.R.color.transparent);
                user.setPicture(null);
                user.setPictureUrl(null);
            }
        });

        btnChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
    }

    private void setUpToolBar(){
        setSupportActionBar(toolbar);
        toolbar.setTitle("Edit Profile");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCloseConformationDialog();
            }
        });
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

    }

    private void openCloseConformationDialog(){
        closeConfirmationDialog.showDialog();
    }


    private void createNewInstances(){
        errorMsg = getResources().getString(R.string.error_message);
        userDataAccess = new User(this);
        user = UserInstance.getInstance().getUser();
        closeConfirmationDialog = new CloseActivityConfirmationDialog(this);

        saveDialog = new ProgressDialog(this);
        saveDialog.setTitle("Saving changes...");
    }

    private void loadDataToUI(){
        if(user != null){
            et_name.setText(user.getName());
            if(user.hasPicture()){
                btnChoosePhoto.setVisibility(View.GONE);
                photoLayout.setVisibility(View.VISIBLE);
                photoView.setImageBitmap(user.getPicture());
            }
        }else{
            Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
        }
    }

    private void handleSave(){
        saveDialog.show();
        getDataFromUI();
        saveChanges();
    }

    private void getDataFromUI(){
        user.setName(et_name.getText().toString());
    }

    private void saveChanges(){
        userDataAccess.editUser(user);
    }

    @Override
    public void OnUserLoaded(UserFirebaseModel user, boolean hasFailed) {

    }

    @Override
    public void OnUserUpdated(boolean hasFailed) {
        if(saveDialog.isShowing()){
            saveDialog.dismiss();
        }
        if(hasFailed){
            Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
        }else{
            UserInstance.getInstance().setUser(user);
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap img = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                if(img != null){
                    btnChoosePhoto.setVisibility(View.GONE);
                    photoLayout.setVisibility(View.VISIBLE);
                    user.setPicture(img);
                    photoView.setImageBitmap(img);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
