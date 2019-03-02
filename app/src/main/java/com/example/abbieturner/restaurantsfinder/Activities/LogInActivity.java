package com.example.abbieturner.restaurantsfinder.Activities;

import android.Manifest;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Fade;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abbieturner.restaurantsfinder.API.API;
import com.example.abbieturner.restaurantsfinder.Adapters.CuisineJsonAdapter;
import com.example.abbieturner.restaurantsfinder.Data.Cuisine;
import com.example.abbieturner.restaurantsfinder.Data.Cuisines;
import com.example.abbieturner.restaurantsfinder.Data.CuisinesSingleton;
import com.example.abbieturner.restaurantsfinder.R;
import com.example.abbieturner.restaurantsfinder.Services.LocationService;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.Icon;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LogInActivity extends AppCompatActivity {

    @BindView(R.id.sign_in_btn)
    Button signin_button;

    @BindView(R.id.skip_login__btn)
    TextView skipLoginBtn;

    @BindView(R.id.btn_google)
    SignInButton btnGoogle;

    private FirebaseAuth mAuth;
    private static int RC_SIGN_IN = 109;
    private int PERMISSION_ALL = 1;
    private String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private GoogleSignInClient mGoogleSignInClient;
    private ProgressDialog progressDialog;
    private GoogleSignInOptions gso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        ButterKnife.bind(this);

        setNewInstances();
        setListeners();

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        startService(new Intent(this, LocationService.class));
    }

    private void setListeners(){
        signin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(Arrays.asList(
                                        new AuthUI.IdpConfig.EmailBuilder().build(),
                                        new AuthUI.IdpConfig.GoogleBuilder().build()))
                                .build(),
                        RC_SIGN_IN);
            }
        });

        skipLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(LogInActivity.this, HomeActivity.class);
                LogInActivity.this.startActivity(myIntent);
            }
        });

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGoogleSignInClient.signOut();
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 101);

            }
        });
    }

    public void fetchCuisines() {
        service.getCuisineId("332", "53.382882", "-1.470300") //(TODO) set to yorkshire - later on will use gps of users phone
                .enqueue(new Callback<Cuisines>() {
                    @Override
                    public void onResponse(Call<Cuisines> call, Response<Cuisines> response) {
                        assert response.body() != null;
                        CuisinesSingleton.getInstance().setCuisines(response.body().cuisinesList);
                    }
    private void setNewInstances(){
        mAuth = FirebaseAuth.getInstance();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        progressDialog = new ProgressDialog(this);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 101) {
            showLoginProgressDialog();
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(LogInActivity.this, "Google sign in failed", Toast.LENGTH_LONG).show();
                hideLoginProgressDialog();
                // ...
            }
        }

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finish();
                    setEnterExitTransition(new Intent(LogInActivity.this, HomeActivity.class));
                } else {
                    finish();
                    startActivity(new Intent(LogInActivity.this, HomeActivity.class));
                }
            } else {
                if (response == null) {
                    Toast.makeText(this, R.string.sign_in_cancelled, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    new FancyAlertDialog.Builder(this)
                            .setTitle(getString(R.string.connection_error))
                            .setBackgroundColor(Color.parseColor("#b7030f"))
                            .setMessage(getString(R.string.check_connection))
                            .setNegativeBtnText(getString(R.string.ok))
                            .setNegativeBtnBackground(Color.parseColor("#FFA9A7A8"))
                            .setAnimation(Animation.POP)
                            .isCancellable(true)
                            .setIcon(R.drawable.ic_cancel_black_24dp, Icon.Visible)
                            .OnNegativeClicked(new FancyAlertDialogListener() {
                                @Override
                                public void OnClick() {
                                    finish();
                                    startActivity(getIntent());
                                }
                            })
                            .build();
                    return;
                }
                Toast.makeText(this, R.string.signin_error_trya, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                Intent intent = new Intent(LogInActivity.this, HomeActivity.class);

                startActivity(intent);
            } else {

                startActivity(new Intent(LogInActivity.this, HomeActivity.class));
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setEnterExitTransition(Intent intent) {
        getWindow().setExitTransition(new Fade().setDuration(1000));
        getWindow().setReenterTransition(new Fade().setDuration(1000));
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(LogInActivity.this).toBundle());
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

    public static boolean hasPermissions(Context context, String... permissions){
        if(context != null && permissions != null){
            for(String permission : permissions){
                if(ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED){
                    return false;
                }
            }
        }
        return true;
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        //Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(LogInActivity.this, HomeActivity.class));
                            hideLoginProgressDialog();
                        } else {
                            Toast.makeText(LogInActivity.this, "Could not log in tvUser", Toast.LENGTH_LONG).show();
                            hideLoginProgressDialog();
                        }
                    }
                });
    }

    private void showLoginProgressDialog(){
        progressDialog.setTitle("Login");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    private void hideLoginProgressDialog(){
        progressDialog.hide();
    }

}