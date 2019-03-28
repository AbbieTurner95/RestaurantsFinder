package com.example.abbieturner.restaurantsfinder.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.abbieturner.restaurantsfinder.FirebaseModels.Friend;
import com.example.abbieturner.restaurantsfinder.PicassoLoader;
import com.example.abbieturner.restaurantsfinder.R;

import java.util.ArrayList;
import java.util.List;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.views.AvatarView;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder> {
    private List<Friend> friendsList;
    private final FriendItemClick listener;

    public FriendsAdapter(FriendItemClick listener) {
        friendsList = new ArrayList<>();
        this.listener = listener;
    }

    public void setList(List<Friend> friendsList) {
        if (friendsList != null) {
            this.friendsList.clear();
            this.friendsList.addAll(friendsList);
            notifyDataSetChanged();
        }
    }

    @Override
    public FriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item, parent, false);
        return new FriendsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FriendsViewHolder holder, int position) {
        Friend friend = friendsList.get(position);

        holder.name.setText(friend.getName());
        holder.imageLoader = new PicassoLoader();
        holder.imageLoader.loadImage(holder.avatarView, friend.getPictureUrl(), friend.getName());
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }


    public class FriendsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        AvatarView avatarView;
        IImageLoader imageLoader;


        public FriendsViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.friend_name);
            avatarView = view.findViewById(R.id.avatar_view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onFriendItemClick(friendsList.get(getAdapterPosition()));
        }
    }

    public interface FriendItemClick {
        void onFriendItemClick(Friend friend);
    }
}
