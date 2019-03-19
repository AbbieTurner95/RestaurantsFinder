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

public class RecommendFriendsAdapter extends RecyclerView.Adapter<RecommendFriendsAdapter.RecommendFriendViewHolder>{
    private List<Friend> friendsList;
    private final RecommendFriendItemClick listener;

    public RecommendFriendsAdapter(RecommendFriendItemClick listener) {
        friendsList = new ArrayList<>();
        this.listener = listener;
    }

    public void setList(List<Friend> friendsList) {
        if(friendsList != null){
            this.friendsList.clear();
            this.friendsList.addAll(friendsList);
            notifyDataSetChanged();
        }
    }

    @Override
    public RecommendFriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommend_friend_item, parent, false);
        return new RecommendFriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecommendFriendViewHolder holder, int position) {
        Friend friend = friendsList.get(position);

        holder.email.setText(friend.getEmail());
        holder.imageLoader = new PicassoLoader();
        holder.imageLoader.loadImage(holder.avatarView, friend.getPictureUrl(), friend.getEmail());
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }


    public class RecommendFriendViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView email;
        AvatarView avatarView;
        IImageLoader imageLoader;


        public RecommendFriendViewHolder(View view) {
            super(view);
            email = view.findViewById(R.id.tv_email);
            avatarView = view.findViewById(R.id.avatar_view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onRecommendFriendItemClick(friendsList.get(getAdapterPosition()));
        }
    }

    public interface RecommendFriendItemClick {
        void onRecommendFriendItemClick(Friend friend);
    }
}
