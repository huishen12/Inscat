package com.spacebunny.hshen.inscat.view.post_list;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.spacebunny.hshen.inscat.R;
import com.spacebunny.hshen.inscat.model.Post;
import com.spacebunny.hshen.inscat.utils.ModelUtils;
import com.spacebunny.hshen.inscat.view.post_detail.PostActivity;
import com.spacebunny.hshen.inscat.view.post_detail.PostFragment;

import java.util.List;

public class PostListAdapter extends RecyclerView.Adapter {

    private List<Post> data;
    public PostListAdapter(@NonNull List<Post> data) {
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final Post post = data.get(position);

        PostViewHolder postViewHolder = (PostViewHolder) holder;
        postViewHolder.likeCount.setText(String.valueOf(post.likes_count));
        postViewHolder.commentCount.setText(String.valueOf(post.comments_count));
        postViewHolder.viewCount.setText(String.valueOf(post.views_count));
        postViewHolder.image.setImageResource(R.drawable.post_placeholder);
        postViewHolder.cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = holder.itemView.getContext();
                Intent intent = new Intent(context, PostActivity.class);
                intent.putExtra(PostFragment.KEY_POST, ModelUtils.toString(post, new TypeToken<Post>(){}));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
