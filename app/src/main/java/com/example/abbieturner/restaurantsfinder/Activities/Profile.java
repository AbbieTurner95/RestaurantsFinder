package com.example.abbieturner.restaurantsfinder.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SnapHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.abbieturner.restaurantsfinder.Adapters.EmptyRecyclerView;
import com.example.abbieturner.restaurantsfinder.Adapters.FriendsAdapter;
import com.example.abbieturner.restaurantsfinder.Adapters.ReviewsAdapter;
import com.example.abbieturner.restaurantsfinder.Adapters.UserReviewsAdapter;
import com.example.abbieturner.restaurantsfinder.Data.ReviewFirebase;
import com.example.abbieturner.restaurantsfinder.Data.ReviewModel;
import com.example.abbieturner.restaurantsfinder.Dialogs.UserReviewPictureDialog;
import com.example.abbieturner.restaurantsfinder.FirebaseAccess.Friends;
import com.example.abbieturner.restaurantsfinder.FirebaseAccess.Listeners.FriendsListener;
import com.example.abbieturner.restaurantsfinder.FirebaseAccess.Listeners.UserListener;
import com.example.abbieturner.restaurantsfinder.FirebaseAccess.Reviews;
import com.example.abbieturner.restaurantsfinder.FirebaseAccess.User;
import com.example.abbieturner.restaurantsfinder.FirebaseModels.Friend;
import com.example.abbieturner.restaurantsfinder.FirebaseModels.UserFirebaseModel;
import com.example.abbieturner.restaurantsfinder.FirebaseModels.UserReview;
import com.example.abbieturner.restaurantsfinder.PicassoLoader;
import com.example.abbieturner.restaurantsfinder.R;
import com.example.abbieturner.restaurantsfinder.Singletons.UserInstance;
import com.example.abbieturner.restaurantsfinder.StartSnapHelper;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.views.AvatarView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class Profile extends BaseActivity
        implements UserListener, UserReviewsAdapter.UserReviewItemClick,
        Reviews.ReviewsListener, FriendsAdapter.FriendItemClick,
        FriendsListener {

    @BindView(R.id.avatar_personal_photo)
    AvatarView avatarPersonalPhoto;
    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_friends)
    TextView tvFriends;
    @BindView(R.id.tv_reviews)
    TextView tvReviews;
    @BindView(R.id.tv_since_date)
    TextView tvSinceDate;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.reviews_recycler_view)
    EmptyRecyclerView reviewsRV;
    @BindView(R.id.reviews_empty_view)
    LinearLayout reviewsEmptyView;
    @BindView(R.id.pb_reviews)
    ProgressBar reviewsProgressBar;

    @BindView(R.id.friends_recycler_view)
    EmptyRecyclerView friendsRV;
    @BindView(R.id.friends_rv_empty_view)
    LinearLayout friendsEmptyView;
    @BindView(R.id.pb_popular_restaurants)
    ProgressBar friendsProgressBar;



    private IImageLoader imageLoader;
    private String userId, TAG_USER_ID, loadingDialogTitle;
    private User userDataAccess;
    private ProgressDialog loadingDialog;
    private LinearLayoutManager reviewsLayoutManager, friendsLayoutManager;
    private UserReviewsAdapter reviewsAdapter;
    private FriendsAdapter friendsAdapter;
    private Reviews reviewsDataAccess;
    private FirebaseAuth mAuth;
    private UserReviewPictureDialog pictureDialog;
    private Friends friendsDataAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_bar_profile);
        ButterKnife.bind(this);

        setNewInstances();
        setUpToolbar();
        setUpReviewsRecyclerView();
        setUpFriendsRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUser();
        loadFriends();
        loadReviews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.action_edit:
                Intent editIntent = new Intent(Profile.this, EditProfile.class);
                startActivity(editIntent);
                break;
            case R.id.action_add_friend:
                Intent friendsIntent = new Intent(Profile.this, ManageFriends.class);
                startActivity(friendsIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setNewInstances(){
        imageLoader = new PicassoLoader();
        TAG_USER_ID = getResources().getString(R.string.TAG_USER_ID);
        userDataAccess = new User(this);
        loadingDialog = new ProgressDialog(this);
        loadingDialogTitle = "Loading profile...";
        loadingDialog.setTitle(loadingDialogTitle);
        reviewsDataAccess = new Reviews(this);
        mAuth = FirebaseAuth.getInstance();
        pictureDialog = new UserReviewPictureDialog(this);
        friendsDataAccess = new Friends(this);
    }

    private void setUpToolbar(){
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);

    }

    private void getUser(){
        userId = getIntent().getStringExtra(TAG_USER_ID);
        if(userId != null){
            loadingDialog.show();
            userDataAccess.getUserById(userId);
        }else{
            Toast.makeText(this, "Failed to load user", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void OnUserLoaded(UserFirebaseModel user, boolean hasFailed) {
        if(loadingDialog.isShowing()){
            loadingDialog.dismiss();
        }
        if(hasFailed || user == null){
            Toast.makeText(this, "Failed to load user", Toast.LENGTH_LONG).show();
        }else{
            UserInstance.getInstance().setUser(user);
            displayUserData();
            if(!isLoggedInProfile()){
                MenuItem item = toolbar.getMenu().findItem(R.id.action_edit);
                MenuItem item2 = toolbar.getMenu().findItem(R.id.action_add_friend);
                item.setVisible(false);
                item2.setVisible(false);
            }else{
                MenuItem item = toolbar.getMenu().findItem(R.id.action_edit);
                MenuItem item2 = toolbar.getMenu().findItem(R.id.action_add_friend);
                item.setVisible(true);
                item2.setVisible(true);
            }
        }
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

    }

    private void displayUserData(){
        UserFirebaseModel user = UserInstance.getInstance().getUser();
        if(user != null){
            tvUserName.setText(user.getName());
            tvSinceDate.setText(user.getMemberSince());
            tvStatus.setText(user.getMemberStatus());

            imageLoader.loadImage(avatarPersonalPhoto, user.getPictureUrl(), user.getName());
        }
    }

    private boolean isLoggedInProfile(){
        return UserInstance.getInstance().getUser().getId().equals(mAuth.getUid());
    }

    private void setUpFriendsRecyclerView(){
        friendsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        friendsAdapter = new FriendsAdapter(this);
        friendsAdapter.setList(null);
        friendsRV.setLayoutManager(friendsLayoutManager);

        friendsRV.setEmptyView(friendsEmptyView);
        friendsRV.setAdapter(friendsAdapter);

        SnapHelper popularSnapHelper = new StartSnapHelper();
        popularSnapHelper.attachToRecyclerView(friendsRV);
    }

    private void setUpReviewsRecyclerView(){
        reviewsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        reviewsAdapter = new UserReviewsAdapter(this, this);
        reviewsAdapter.setReviews(null);
        reviewsRV.setLayoutManager(reviewsLayoutManager);

        reviewsRV.setEmptyView(reviewsEmptyView);
        reviewsRV.setAdapter(reviewsAdapter);
    }

    private void loadReviews(){
        reviewsAdapter.setReviews(new ArrayList<UserReview>());
        userId = getIntent().getStringExtra(TAG_USER_ID);
        if(userId != null || !userId.isEmpty()){
            reviewsProgressBar.setVisibility(View.VISIBLE);
            reviewsDataAccess.getUserReviews(userId);
        }
    }

    private void loadFriends(){
        friendsAdapter.setList(new ArrayList<Friend>());
        userId = getIntent().getStringExtra(TAG_USER_ID);
        if(userId != null || !userId.isEmpty()){
            friendsProgressBar.setVisibility(View.VISIBLE);
            friendsDataAccess.getFriends(userId);
        }
    }

    @Override
    public void onReviewsLoaded(List<ReviewFirebase> reviews, boolean hasFailed) {

    }

    @Override
    public void onUserReviewsLoaded(List<UserReview> userReviews, boolean hasFailed) {
        reviewsProgressBar.setVisibility(View.GONE);
        if(hasFailed){
            Toast.makeText(this, "Failed to load reviews.", Toast.LENGTH_LONG).show();
        }else{
            reviewsAdapter.setReviews(userReviews);
            String r = "Reviews Left - " + userReviews.size();
            tvReviews.setText(r);
        }
    }

    @Override
    public void onUserReviewItemClick(UserReview review) {
        if (review.hasPictureUrl()) {
            pictureDialog.showDialog(review);
        } else {
            Toast.makeText(this, "This review does not have any picture.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFriendItemClick(Friend friend) {
        Intent intent = new Intent(Profile.this, Profile.class);
        intent.putExtra(TAG_USER_ID, friend.getUserId());
        startActivity(intent);
    }

    @Override
    public void onGetFriendsCompleted(List<Friend> friends, boolean hasFailed) {
        friendsProgressBar.setVisibility(View.GONE);
        if(hasFailed){
            Toast.makeText(this, "Failed to load friend", Toast.LENGTH_LONG).show();
        }else{
            if(friends != null){
                UserInstance.getInstance().setFriends(friends);
                friendsAdapter.setList(friends);
                String s = "Friends - " + friends.size();
                tvFriends.setText(s);
            }
        }
    }

    @Override
    public void onCreateFriendCompletes(boolean hasFailed) {
    }

    @Override
    public void onRemoveFriendCompleted(String friendId, boolean hasFailed) {

    }
}
