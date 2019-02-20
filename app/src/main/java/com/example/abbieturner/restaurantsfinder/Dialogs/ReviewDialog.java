package com.example.abbieturner.restaurantsfinder.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abbieturner.restaurantsfinder.Activities.RestaurantActivity;
import com.example.abbieturner.restaurantsfinder.Data.ReviewFirebase;
import com.example.abbieturner.restaurantsfinder.Data.ReviewSingleton;
import com.example.abbieturner.restaurantsfinder.R;

public class ReviewDialog{
    private AlertDialog dialog;
    private Context context;
    private LayoutInflater inflater;
    private TextView answer, question;
    private ReviewFirebase review;
    private ProgressBar pbCreateReview;

    private EditText reviewMsg;
    private ImageView picture, closeDialog;
    private TextView createReview, clearReview;
    private ImageView star1, star2, star3, star4, star5;
    private ImageView[] stars = new ImageView[5];
    private LinearLayout cameraPlaceHolder;

    public ReviewDialog(Context context){
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.review = ReviewSingleton.getInstance().getReview();

        createDialog();
    }

    private void createDialog(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        View mView= inflater.inflate(R.layout.dialog_new_review, null);



        reviewMsg = mView.findViewById(R.id.tv_review_msg);
        cameraPlaceHolder = mView.findViewById(R.id.btn_camera);
        picture = mView.findViewById(R.id.image_picture);
        createReview = mView.findViewById(R.id.btn_create_review);
        closeDialog = mView.findViewById(R.id.btn_close_dialog);
        clearReview = mView.findViewById(R.id.btn_clear_review);
        star1 = mView.findViewById(R.id.star_1);
        star2 = mView.findViewById(R.id.star_2);
        star3 = mView.findViewById(R.id.star_3);
        star4 = mView.findViewById(R.id.star_4);
        star5 = mView.findViewById(R.id.star_5);
        pbCreateReview = mView.findViewById(R.id.pb_create_review);

        star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRating(1);
            }
        });
        star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRating(2);
            }
        });
        star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRating(3);
            }
        });
        star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRating(4);
            }
        });
        star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRating(5);
            }
        });
        cameraPlaceHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RestaurantActivity)context).openCamera();
            }
        });

        stars[0] = star1;
        stars[1] = star2;
        stars[2] = star3;
        stars[3] = star4;
        stars[4] = star5;

        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideDialog();
            }
        });
        clearReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearReview();
                clearUI();
            }
        });
        createReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reviewIsValid()){
                    getDataFromUI();
                    ((RestaurantActivity)context).createReview();
                    showProgressBar();
                }else{
                    Toast.makeText(context, "Please fill required fields.", Toast.LENGTH_LONG).show();
                }
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

    private boolean reviewIsValid(){
        String review = reviewMsg.getText().toString();

        if(review.isEmpty()){
            return false;
        }

        if(ReviewSingleton.getInstance().getReview().getRating() == 0){
            return false;
        }

        return true;
    }

    private void getDataFromUI(){
        ReviewSingleton.getInstance().getReview().setReview(reviewMsg.getText().toString());
    }

    private void clearUI(){
        updateStarsUI();
        reviewMsg.setText("");
        picture.setImageResource(android.R.color.transparent);
        picture.setVisibility(View.GONE);
        cameraPlaceHolder.setVisibility(View.VISIBLE);
    }
    private void clearReview(){
        ReviewSingleton.getInstance().clearReview();
    }
    public void showDialog(){
        clearReview();
        dialog.show();
        hideProgressBar();
    }
    public void hideDialog(){
        clearReview();
        clearUI();
        dialog.hide();
        hideProgressBar();
    }
    private void setRating(int stars){
        ReviewSingleton.getInstance().getReview().setRating(stars);

        updateStarsUI();
    }
    private void updateStarsUI(){
        for(int i = 0; i < stars.length; i++){
            if(i < ReviewSingleton.getInstance().getReview().getRating()){
                stars[i].setImageResource(R.drawable.ic_star_24dp);
            }else{
                stars[i].setImageResource(R.drawable.ic_star_border_24dp);
            }
        }
    }

    public void pictureLoaded(){
        picture.setVisibility(View.VISIBLE);
        cameraPlaceHolder.setVisibility(View.GONE);
        picture.setImageBitmap(ReviewSingleton.getInstance().getReview().getPicture());
    }

    private void showProgressBar(){
        pbCreateReview.setVisibility(View.VISIBLE);
    }
    public void hideProgressBar(){
        pbCreateReview.setVisibility(View.GONE);
    }
}
