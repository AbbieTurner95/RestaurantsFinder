package com.example.abbieturner.restaurantsfinder.Activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.abbieturner.restaurantsfinder.Adapters.EmptyRecyclerView;
import com.example.abbieturner.restaurantsfinder.Adapters.ManageFriendsAdapter;
import com.example.abbieturner.restaurantsfinder.Dialogs.CloseActivityConfirmationDialog;
import com.example.abbieturner.restaurantsfinder.FirebaseAccess.Friends;
import com.example.abbieturner.restaurantsfinder.FirebaseAccess.Listeners.FriendsListener;
import com.example.abbieturner.restaurantsfinder.FirebaseAccess.Listeners.UserListener;
import com.example.abbieturner.restaurantsfinder.FirebaseAccess.User;
import com.example.abbieturner.restaurantsfinder.FirebaseModels.Friend;
import com.example.abbieturner.restaurantsfinder.FirebaseModels.UserFirebaseModel;
import com.example.abbieturner.restaurantsfinder.R;
import com.example.abbieturner.restaurantsfinder.Singletons.UserInstance;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ManageFriends extends BaseActivity implements
        ManageFriendsAdapter.FriendItemClick,
        UserListener, FriendsListener {

    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;
    @BindView(R.id.et_search)
    EditText searchInput;
    @BindView(R.id.btn_search)
    ImageView btnSearch;

    @BindView(R.id.users_recycler_view)
    EmptyRecyclerView usersRV;
    @BindView(R.id.users_empty_view)
    LinearLayout usersEmptyView;
    @BindView(R.id.pb_users)
    ProgressBar usersProgressBar;

    @BindView(R.id.friends_recycler_view)
    EmptyRecyclerView friendsRV;
    @BindView(R.id.friends_empty_view)
    LinearLayout friendsEmptyView;
    @BindView(R.id.pb_friends)
    ProgressBar friendsProgressBar;

    private CloseActivityConfirmationDialog closeConfirmationDialog;
    private LinearLayoutManager usersLayoutManager, friendsLayoutManager;
    private ManageFriendsAdapter friendsAdapter, usersAdapter;
    private User userDataAccess;
    private FirebaseAuth mAuth;
    private Friends friendsDataAccess;
    private String errorMessage;
    private ProgressDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_friends);
        ButterKnife.bind(this);

        createNewInstances();
        setUpToolBar();
        setUpFriendsRecyclerView();
        setUpUsersRecyclerView();
        setUpListeners();
    }

    private void setUpListeners(){
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSearch();
            }
        });
    }

    private void handleSearch(){
        String searchTerm = searchInput.getText().toString();

        userDataAccess.getUsers(searchTerm);
    }

    private void setUpToolBar(){
        toolbar.setTitle("Manage Friends");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
    }

    private void openCloseConformationDialog(){
        closeConfirmationDialog.showDialog();
    }

    private void createNewInstances(){
        closeConfirmationDialog = new CloseActivityConfirmationDialog(this);
        userDataAccess = new User(this);
        mAuth = FirebaseAuth.getInstance();
        friendsDataAccess = new Friends(this);
        errorMessage = getResources().getString(R.string.error_message);

        loadingDialog = new ProgressDialog(this);
    }

    private void openLoadingDialog(String msg){
        loadingDialog.setTitle(msg);
        loadingDialog.show();
    }

    private void hideLoadingDialog(){
        if(loadingDialog.isShowing()){
            loadingDialog.dismiss();
        }
    }

    private void setUpFriendsRecyclerView(){
        friendsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        friendsAdapter = new ManageFriendsAdapter(this, this, true);
        friendsAdapter.setList(UserInstance.getInstance().getFriends());
        friendsRV.setLayoutManager(friendsLayoutManager);

        friendsRV.setEmptyView(friendsEmptyView);
        friendsRV.setAdapter(friendsAdapter);
    }

    private void setUpUsersRecyclerView(){
        usersLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        usersAdapter = new ManageFriendsAdapter(this, this, false);
        usersAdapter.setList(null);
        usersRV.setLayoutManager(usersLayoutManager);

        usersRV.setEmptyView(usersEmptyView);
        usersRV.setAdapter(usersAdapter);
    }

    @Override
    public void onFriendItemClick(Friend friend) {
        openLoadingDialog("Removing friend...");
        removeFriend(friend);
    }

    @Override
    public void onUserItemClick(Friend friend) {
        openLoadingDialog("Adding friend...");
        addUserToFriends(friend);
    }

    @Override
    public void OnUserLoaded(UserFirebaseModel user, boolean hasFailed) {

    }

    @Override
    public void OnUserUpdated(boolean hasFailed) {

    }

    @Override
    public void OnUsersLoaded(List<Friend> users, boolean hasFailed) {
        if(hasFailed){
            Toast.makeText(this, "Failed to get users.", Toast.LENGTH_LONG).show();
        }else{
            if(users != null){
                removeFriendsAndSelf(users);
                String toastMsg = users.size() == 1 ? "Found: " + users.size() + " user" : "Found: " + users.size() + " users";
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                usersAdapter.setList(users);
            }
        }
    }

    @Override
    public void OnUserExists(boolean exists, boolean hasFailed) {

    }

    @Override
    public void OnUserCreated(boolean hasFailed) {

    }

    private void removeFriendsAndSelf(List<Friend> users){
        String email = mAuth.getCurrentUser().getEmail();

        Friend f = null;
        for(Friend user : users){
            if(user.getEmail().equals(email)){
                f = user;
            }
        }
        users.remove(f);
    }

    private boolean containsUser(List<Friend> users, String email){
        for(Friend user : users){
            if(user.getEmail().equals(email)){
                return true;
            }
        }

        return false;
    }

    private void removeUser(List<Friend> users, Friend friend){
        Iterator<Friend> itr = users.iterator();

        while (itr.hasNext()) {
            Friend user = itr.next();

            if (user.getEmail().equals(friend.getEmail())) {
                users.remove(user);
            }
        }
    }

    private void addUserToFriends(Friend user){
        String userId = mAuth.getUid();
        friendsDataAccess.addFriend(userId, user);
    }

    private void removeFriend(Friend friend){
        friendsDataAccess.removeFriend(mAuth.getUid(), friend.getUserId());
    }

    @Override
    public void onGetFriendsCompleted(List<Friend> friends, boolean hasFailed) {
        hideLoadingDialog();
        if(hasFailed){
            Toast.makeText(this, "Failed to load friends", Toast.LENGTH_LONG).show();
        }else{
            if(friends != null){
                UserInstance.getInstance().setFriends(friends);
                friendsAdapter.setList(friends);
                handleSearch();
            }
        }
    }

    @Override
    public void onCreateFriendCompletes(boolean hasFailed) {
        hideLoadingDialog();
        if(hasFailed){
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }else{
            openLoadingDialog("Loading friends...");
            friendsDataAccess.getFriends(mAuth.getUid());
        }
    }

    @Override
    public void onRemoveFriendCompleted(String friendId, boolean hasFailed) {
        hideLoadingDialog();
        if(hasFailed){
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Friend removed", Toast.LENGTH_LONG).show();
            removeFriendFromLocal(friendId);
        }
    }

    private void removeFriendFromLocal(String friendId){
        List<Friend> friends = UserInstance.getInstance().getFriends();

        Friend f = null;
        for(Friend friend : friends){
            if(friend.getUserId().equals(friendId)){
                f = friend;
            }
        }
        if(f != null){
            friends.remove(f);
        }

        UserInstance.getInstance().setFriends(friends);
        friendsAdapter.setList(friends);
    }
}
