package com.example.abbieturner.restaurantsfinder.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.abbieturner.restaurantsfinder.Dialogs.CloseActivityConfirmationDialog;
import com.example.abbieturner.restaurantsfinder.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ManageFriends extends AppCompatActivity {

    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;

    private CloseActivityConfirmationDialog closeConfirmationDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_friends);
        ButterKnife.bind(this);

        createNewInstances();
        setUpToolBar();
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
                //handleSave();
                Toast.makeText(this, "Save", Toast.LENGTH_LONG).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpToolBar(){
        toolbar.setTitle("Manage Friends");
        setSupportActionBar(toolbar);
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
        closeConfirmationDialog = new CloseActivityConfirmationDialog(this);
    }
}
