package com.spacebunny.hshen.inscat.view.profile_detail;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.spacebunny.hshen.inscat.R;
import com.spacebunny.hshen.inscat.model.Post;
import com.spacebunny.hshen.inscat.model.User;
import com.spacebunny.hshen.inscat.utils.ModelUtils;
import com.spacebunny.hshen.inscat.view.post_detail.PostActivity;
import com.spacebunny.hshen.inscat.view.post_detail.PostFragment;
import com.spacebunny.hshen.inscat.view.post_list.PostViewHolder;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter {
    private User user;
    private List<Post> data;
    private static final int VIEW_TYPE_PROFILE_INFO = 0;
    private static final int VIEW_TYPE_PROFILE_POST = 1;

    ProfileAdapter(User user, List<Post> data) {
        this.user = user;
        this.data = data;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case VIEW_TYPE_PROFILE_INFO:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.profile_info, parent, false);
                return new ProfileInfoViewHolder(view);
            case VIEW_TYPE_PROFILE_POST:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_post, parent, false);
                return new PostViewHolder(view);
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final int viewType = getItemViewType(position);
        switch (viewType) {
            case VIEW_TYPE_PROFILE_INFO:
                ProfileInfoViewHolder infoViewHolder = (ProfileInfoViewHolder) holder;
                infoViewHolder.profilePhoto.setImageResource(R.drawable.user_photo_placeholder);
                infoViewHolder.profileName.setText(user.name);
                infoViewHolder.profileDescription.setText(user.description);
                infoViewHolder.profileFollowing.setText(String.valueOf(user.following));
                infoViewHolder.profileFollower.setText(String.valueOf(user.follower));
                break;

            case VIEW_TYPE_PROFILE_POST:
                final Post post = data.get(position);
                PostViewHolder postViewHolder = (PostViewHolder) holder;
                postViewHolder.likeCount.setText(String.valueOf(post.likes_count));
                postViewHolder.commentCount.setText(String.valueOf(post.comments_count));
                postViewHolder.viewCount.setText(String.valueOf(post.views_count));
                postViewHolder.cover.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = holder.itemView.getContext();
                        Intent intent = new Intent(context, PostActivity.class);
                        intent.putExtra(PostFragment.KEY_POST, ModelUtils.toString(post, new TypeToken<Post>() {
                        }));
                        context.startActivity(intent);
                    }
                });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position){
        if (position == 0) {
            return VIEW_TYPE_PROFILE_INFO;
        }
        else {
            return VIEW_TYPE_PROFILE_POST;
        }
    }
}
