package com.spacebunny.hshen.inscat.view.post_list;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.google.gson.reflect.TypeToken;
import com.spacebunny.hshen.inscat.R;
import com.spacebunny.hshen.inscat.model.Post;
import com.spacebunny.hshen.inscat.utils.ModelUtils;
import com.spacebunny.hshen.inscat.view.post_detail.PostActivity;
import com.spacebunny.hshen.inscat.view.post_detail.PostFragment;

import java.util.List;

public class PostListAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_POST = 1;
    private static final int VIEW_TYPE_LOADING = 2;

    private List<Post> data;
    private LoadMoreListener loadMoreListener;
    private boolean showLoading;

    public PostListAdapter(@NonNull List<Post> data,  @NonNull LoadMoreListener loadMoreListener) {
        this.data = data;
        this.loadMoreListener = loadMoreListener;
        this.showLoading = true;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_POST) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_post, parent, false);
            return new PostViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_loading, parent, false);
            return new RecyclerView.ViewHolder(view) {};
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final int viewType = getItemViewType(position);
        if (viewType == VIEW_TYPE_LOADING) {
            loadMoreListener.onLoadMore();
        }
        else {
            final Post post = data.get(position);

            PostViewHolder postViewHolder = (PostViewHolder) holder;
            postViewHolder.likeCount.setText(String.valueOf(post.likes.count));
            postViewHolder.commentCount.setText(String.valueOf(post.comments.count));
            postViewHolder.viewCount.setText(String.valueOf(post.views_count));

            DraweeController controller = Fresco.newDraweeControllerBuilder()
                                            .setUri(Uri.parse(post.getImageUrl()))
                                            .setAutoPlayAnimations(true)
                                            .build();
            postViewHolder.image.setController(controller);

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
        return showLoading ? data.size() + 1 : data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position < data.size()
                ? VIEW_TYPE_POST
                : VIEW_TYPE_LOADING;
    }

    public void append(@NonNull List<Post> morePosts) {
        data.addAll(morePosts);
        notifyDataSetChanged();
    }

    public int getDataCount() {
        Log.d("TAG", "Data size is " + data.size());
        return data.size();
    }

    public void setShowLoading(boolean showLoading) {
        this.showLoading = showLoading;
        notifyDataSetChanged();
    }

    public interface LoadMoreListener {
        void onLoadMore();
    }
}
