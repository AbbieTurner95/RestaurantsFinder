package com.example.abbieturner.restaurantsfinder.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abbieturner.restaurantsfinder.R;
import com.example.abbieturner.restaurantsfinder.DownloadImageTask;
import com.example.abbieturner.restaurantsfinder.FirebaseModels.UserReview;

public class UserReviewPictureDialog {
    private AlertDialog dialog;
    private Context context;
    private LayoutInflater inflater;
    private ImageView picture;
    private ProgressBar progressBar;
    private TextView title;

    public UserReviewPictureDialog(Context context){
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        createDialog();
    }

    private void createDialog(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        View mView= inflater.inflate(R.layout.dialog_review_picture, null);

        picture = mView.findViewById(R.id.img_picture);
        progressBar = mView.findViewById(R.id.pb_picture);
        title = mView.findViewById(R.id.tv_title);


        mBuilder.setView(mView);
        dialog = mBuilder.create();

        dialog.setCancelable(true);
    }

    public void showDialog(UserReview review){
        dialog.show();

        displayPicture(review);
    }
    public void hideDialog(){
        dialog.hide();
    }

    private void displayPicture(UserReview review){
        picture.setImageResource(android.R.color.transparent);
        title.setText(review.getRestaurantName());

        if(review.hasPicture()){
            picture.setImageBitmap(review.getPicture());
            picture.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
        else if(review.hasPictureUrl()){
            picture.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            new DownloadImageTask(picture, progressBar)
                    .execute(review.getPictureUrl());
        }else{
            Toast.makeText(context, "Review does not have picture.", Toast.LENGTH_LONG).show();
            hideDialog();
        }
    }
}
