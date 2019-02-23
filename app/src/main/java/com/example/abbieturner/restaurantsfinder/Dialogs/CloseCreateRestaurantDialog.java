package com.example.abbieturner.restaurantsfinder.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.abbieturner.restaurantsfinder.Activities.CreateRestaurantActivity;
import com.example.abbieturner.restaurantsfinder.R;

public class CloseCreateRestaurantDialog {
    private AlertDialog dialog;
    private Context context;
    private LayoutInflater inflater;

    public CloseCreateRestaurantDialog(Context context){
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        createDialog();
    }

    private void createDialog(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        View mView= inflater.inflate(R.layout.dialog_close_create_restaurant, null);

        TextView btnCancel = mView.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideDialog();
            }
        });

        TextView btnOk = mView.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeCreateRestaurantActivity();
            }
        });

        mBuilder.setView(mView);
        dialog = mBuilder.create();

        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount=0.9f;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
    }

    public void showDialog(){
        dialog.show();
    }
    public void hideDialog(){
        dialog.hide();
    }
    private void closeCreateRestaurantActivity(){
        ((CreateRestaurantActivity)context).finish();
    }
}
