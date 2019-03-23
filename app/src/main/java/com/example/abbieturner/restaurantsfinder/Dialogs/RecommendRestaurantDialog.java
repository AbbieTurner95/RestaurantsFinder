package com.example.abbieturner.restaurantsfinder.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abbieturner.restaurantsfinder.Adapters.EmptyRecyclerView;
import com.example.abbieturner.restaurantsfinder.Adapters.RecommendFriendsAdapter;
import com.example.abbieturner.restaurantsfinder.Data.RestaurantModel;
import com.example.abbieturner.restaurantsfinder.FirebaseAccess.Friends;
import com.example.abbieturner.restaurantsfinder.FirebaseAccess.Listeners.FriendsListener;
import com.example.abbieturner.restaurantsfinder.FirebaseAccess.Listeners.RecommendedRestaurantsListener;
import com.example.abbieturner.restaurantsfinder.FirebaseAccess.RecommendedRestaurants;
import com.example.abbieturner.restaurantsfinder.FirebaseModels.Friend;
import com.example.abbieturner.restaurantsfinder.FirebaseModels.RecommendedRestaurant;
import com.example.abbieturner.restaurantsfinder.R;

import java.util.ArrayList;
import java.util.List;

public class RecommendRestaurantDialog implements RecommendFriendsAdapter.RecommendFriendItemClick, FriendsListener,RecommendedRestaurantsListener {
    private AlertDialog dialog;
    private Context context;
    private LayoutInflater inflater;
    private EmptyRecyclerView friendsRecyclerView;
    private LinearLayout friendsEmptyView;
    private ProgressBar progressBarFriends;
    private LinearLayoutManager friendsLayoutManager;
    private RecommendFriendsAdapter friendsAdapter;
    private Friends friendsDataAccess;
    private String userId, errorMessage, restaurantRecommendedMsg;
    private RecommendedRestaurants recommendedRestaurantsDataAccess;
    private RestaurantModel restaurant;
    private TextView btnSendAll;
    private List<Friend> friends;

    public RecommendRestaurantDialog(Context context, String userId) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.userId = userId;
        friendsDataAccess = new Friends(this);
        recommendedRestaurantsDataAccess = new RecommendedRestaurants(this);

        createDialog();
    }

    private void createDialog() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        View mView = inflater.inflate(R.layout.dialog_recommend_restaurant, null);

        friendsRecyclerView = mView.findViewById(R.id.friends_recycler_view);
        friendsEmptyView = mView.findViewById(R.id.friends_empty_view);
        progressBarFriends = mView.findViewById(R.id.pb_friends);
        errorMessage = this.context.getResources().getString(R.string.error_message);
        restaurantRecommendedMsg = this.context.getResources().getString(R.string.restaurant_recommended_msg);
        btnSendAll = mView.findViewById(R.id.btn_send_all);

        btnSendAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRestaurantToAllFriends();
            }
        });

        friends = new ArrayList<Friend>();
        setUpRecyclerView();

        ImageView close = mView.findViewById(R.id.btn_close_dialog);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideDialog();
            }
        });

        mBuilder.setView(mView);
        dialog = mBuilder.create();

        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.9f;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
    }

    private void setUpRecyclerView(){
        friendsLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        friendsAdapter = new RecommendFriendsAdapter(this);
        friendsAdapter.setList(null);
        friendsRecyclerView.setLayoutManager(friendsLayoutManager);

        friendsRecyclerView.setEmptyView(friendsEmptyView);
        friendsRecyclerView.setAdapter(friendsAdapter);
    }

    public void showDialog(RestaurantModel restaurant) {
        this.restaurant = restaurant;
        dialog.show();

        btnSendAll.setVisibility(View.GONE);
        loadFriends();
    }

    private void loadFriends(){
        if(userId != null && !userId.isEmpty()){
            progressBarFriends.setVisibility(View.VISIBLE);
            friendsDataAccess.getFriends(userId);
        }
    }

    public void hideDialog() {
        dialog.hide();
    }

    private void sendRestaurantToAllFriends(){
        for(Friend friend : this.friends){
            progressBarFriends.setVisibility(View.VISIBLE);
            if(restaurant.isFirebaseRestaurant()){
                recommendedRestaurantsDataAccess.addRecommendedRestaurant(friend.getUserId(), restaurant.getId(), restaurant.getName(), restaurant.getFirebaseRestaurant().getPictureUrl());
            }else{
                recommendedRestaurantsDataAccess.addRecommendedRestaurant(friend.getUserId(), restaurant.getId(), restaurant.getName(), "No url");
            }
        }
    }

    @Override
    public void onRecommendFriendItemClick(Friend friend) {
        progressBarFriends.setVisibility(View.VISIBLE);
        if(restaurant.isFirebaseRestaurant()){
            recommendedRestaurantsDataAccess.addRecommendedRestaurant(friend.getUserId(), restaurant.getId(), restaurant.getName(), restaurant.getFirebaseRestaurant().getPictureUrl());
        }else{
            recommendedRestaurantsDataAccess.addRecommendedRestaurant(friend.getUserId(), restaurant.getId(), restaurant.getName(), "No url");
        }
    }

    @Override
    public void onGetFriendsCompleted(List<Friend> friends, boolean hasFailed) {
        progressBarFriends.setVisibility(View.GONE);
        if(hasFailed){
            this.friends = new ArrayList<Friend>();
            Toast.makeText(this.context, "Failed to load friends", Toast.LENGTH_LONG).show();
        }else{
            friendsAdapter.setList(friends);
            if(friends != null && friends.size() > 0){
                this.friends = friends;
                btnSendAll.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onCreateFriendCompletes(boolean hasFailed) {

    }

    @Override
    public void onRemoveFriendCompleted(String friendId, boolean hasFailed) {

    }

    @Override
    public void OnAddRecommendedRestaurantCompleted(boolean hasFailed) {
        progressBarFriends.setVisibility(View.GONE);
        if(hasFailed){
            Toast.makeText(this.context, errorMessage, Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this.context, restaurantRecommendedMsg, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void OnGetRecommendedRestaurantsCompleted(List<RecommendedRestaurant> restaurants, boolean hasFailed) {

    }
}
