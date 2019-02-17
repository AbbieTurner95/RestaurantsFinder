package com.example.abbieturner.restaurantsfinder.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.abbieturner.restaurantsfinder.R;

public class ReviewDialog {
    private AlertDialog dialog;
    private Context context;
    private LayoutInflater inflater;
    private TextView answer, question;

    public ReviewDialog(Context context){
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        createDialog();
    }

    private void createDialog(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        View mView= inflater.inflate(R.layout.dialog_new_review, null);

        ImageView closeDialog = mView.findViewById(R.id.btn_close_dialog);
        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideDialog();
            }
        });


//        LinearLayout camera = mView.findViewById(R.id.btn_camera);
//        LinearLayout gallery = mView.findViewById(R.id.btn_gallery);
//
//        camera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listener.openCamera();
//                hideDialog();
//            }
//        });
//
//        gallery.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listener.openGallery();
//                hideDialog();
//            }
//        });

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
}
