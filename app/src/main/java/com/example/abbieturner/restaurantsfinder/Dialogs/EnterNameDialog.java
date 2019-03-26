package com.example.abbieturner.restaurantsfinder.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abbieturner.restaurantsfinder.FirebaseAccess.Listeners.UserListener;
import com.example.abbieturner.restaurantsfinder.FirebaseAccess.User;
import com.example.abbieturner.restaurantsfinder.FirebaseModels.Friend;
import com.example.abbieturner.restaurantsfinder.FirebaseModels.UserFirebaseModel;
import com.example.abbieturner.restaurantsfinder.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class EnterNameDialog implements UserListener {
    private AlertDialog dialog;
    private Context context;
    private LayoutInflater inflater;
    private EditText name;
    private TextView btnCreate;
    private User userDataAccess;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private ProgressBar pbCreateProfile;

    public EnterNameDialog(Context context) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        createDialog();
    }

    private void createDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        View mView = inflater.inflate(R.layout.dialog_enter_name, null);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        pbCreateProfile = mView.findViewById(R.id.pb_create_profile);
        pbCreateProfile.setVisibility(View.GONE);
        name = mView.findViewById(R.id.tv_name);

        btnCreate = mView.findViewById(R.id.btn_create);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String enteredName = name.getText().toString();

               if(!enteredName.isEmpty() && currentUser != null){
                   pbCreateProfile.setVisibility(View.VISIBLE);
                    userDataAccess.createProfile(currentUser.getUid(), enteredName);
               }else{
                   Toast.makeText(context, "Please enter valid name.", Toast.LENGTH_LONG).show();
               }
            }
        });

        userDataAccess = new User(this);

        mBuilder.setView(mView);
        dialog = mBuilder.create();

        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.9f;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
    }

    public void showDialog() {
        dialog.show();
    }

    public void hideDialog() {
        dialog.hide();
    }

    @Override
    public void OnUserLoaded(UserFirebaseModel user, boolean hasFailed) {

    }

    @Override
    public void OnUserUpdated(boolean hasFailed) {

    }

    @Override
    public void OnUsersLoaded(List<Friend> users, boolean hasFailed) {

    }

    @Override
    public void OnUserExists(boolean exists, boolean hasFailed) {

    }

    @Override
    public void OnUserCreated(boolean hasFailed) {
        pbCreateProfile.setVisibility(View.GONE);
        if(hasFailed){
            Toast.makeText(context, "Something went wrong.", Toast.LENGTH_LONG).show();
        }else{
            hideDialog();
            Toast.makeText(context, "Profile created.", Toast.LENGTH_LONG).show();
        }
    }
}