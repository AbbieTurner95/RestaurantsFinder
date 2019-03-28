package com.example.abbieturner.restaurantsfinder.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abbieturner.restaurantsfinder.FirebaseModels.Friend;
import com.example.abbieturner.restaurantsfinder.R;

import java.util.ArrayList;
import java.util.List;

public class ManageFriendsAdapter extends RecyclerView.Adapter<ManageFriendsAdapter.FriendViewHolder> {
    private List<Friend> friendsList;
    private final Context context;
    private final FriendItemClick listener;
    private boolean isFriend;

    public ManageFriendsAdapter(Context context, FriendItemClick listener, boolean isFriend) {
        friendsList = new ArrayList<>();
        this.context = context;
        this.listener = listener;
        this.isFriend = isFriend;
    }

    public void setList(List<Friend> friends) {
        if (friends != null) {
            friendsList.clear();
            friendsList.addAll(friends);
            notifyDataSetChanged();
        }
    }

    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (isFriend) {
            view = LayoutInflater.from(context).inflate(R.layout.manage_friend_item, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.manage_user_item, parent, false);
        }
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        Friend friend = friendsList.get(position);

        holder.name.setText(friend.getEmail());


    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    public class FriendViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        ImageView btnAction;

        public FriendViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.tv_name);
            btnAction = view.findViewById(R.id.btn_action);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (isFriend) {
                listener.onFriendItemClick(friendsList.get(getAdapterPosition()));
            } else {
                listener.onUserItemClick(friendsList.get(getAdapterPosition()));
            }

        }
    }

    public interface FriendItemClick {
        void onFriendItemClick(Friend friend);

        void onUserItemClick(Friend friend);
    }
}
