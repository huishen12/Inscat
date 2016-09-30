package com.spacebunny.hshen.inscat.view.post_detail;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spacebunny.hshen.inscat.R;
import com.spacebunny.hshen.inscat.model.Post;

class PostAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_POST_IMAGE = 0;
    private static final int VIEW_TYPE_POST_INFO = 1;

    private final Post post;

    public PostAdapter(@NonNull Post post) {
        this.post = post;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case VIEW_TYPE_POST_IMAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_image, parent, false);
                return new ImageViewHolder(view);
            case VIEW_TYPE_POST_INFO:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_info, parent, false);
                return new InfoViewHolder(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int viewType = getItemViewType(position);
        switch (viewType) {
            case VIEW_TYPE_POST_IMAGE:
                break;
            case VIEW_TYPE_POST_INFO:
                InfoViewHolder postDetailViewHolder = (InfoViewHolder) holder;
                postDetailViewHolder.authorName.setText(post.user.name);
                postDetailViewHolder.description.setText(post.description);
                Log.d("TAG", post.likes_count + " like count");
                postDetailViewHolder.likeCount.setText(String.valueOf(post.likes_count));
                postDetailViewHolder.commentCount.setText(String.valueOf(post.comments_count));
                postDetailViewHolder.viewCount.setText(String.valueOf(post.views_count));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position){
        if (position == 0) {
            return VIEW_TYPE_POST_IMAGE;
        }
        else {
            return VIEW_TYPE_POST_INFO;
        }
    }
}
