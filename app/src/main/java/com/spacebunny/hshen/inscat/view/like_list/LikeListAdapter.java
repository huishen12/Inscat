package com.spacebunny.hshen.inscat.view.like_list;


import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.spacebunny.hshen.inscat.R;
import com.spacebunny.hshen.inscat.model.Like;
import com.spacebunny.hshen.inscat.utils.UIUtils;

import java.util.List;

public class LikeListAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_LOADING = 2;
    private static final int VIEW_TYPE_LIKE = 4;
    private List<Like> mLikes;
    private UIUtils.LoadMoreListener mLoadMoreListener;
    private boolean showLoading;

    public LikeListAdapter(@NonNull List<Like> likes, @NonNull UIUtils.LoadMoreListener loadMoreListener) {
        this.mLikes = likes;
        this.mLoadMoreListener = loadMoreListener;
        this.showLoading = true;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LIKE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_likes, parent, false);
            return new LikeViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_loading, parent, false);
            return new RecyclerView.ViewHolder(view){};
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == VIEW_TYPE_LOADING) {
            mLoadMoreListener.onLoadMore();
        }
        else {
            Like like = mLikes.get(position);
            LikeViewHolder likeViewHolder = (LikeViewHolder) holder;

            DraweeController controller = Fresco.newDraweeControllerBuilder()
                                            .setUri(Uri.parse(like.profile_picture))
                                            .setAutoPlayAnimations(true)
                                            .build();
            likeViewHolder.authorPhoto.setController(controller);
            likeViewHolder.authorName.setText(like.full_name);
            likeViewHolder.authorBio.setText(like.userbio);
        }
    }

    @Override
    public int getItemCount() {
        return showLoading ? mLikes.size() + 1 : mLikes.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position < mLikes.size()
                ? VIEW_TYPE_LIKE
                : VIEW_TYPE_LOADING;
    }

    public void append(@NonNull List<Like> moreLikes) {
        mLikes.addAll(moreLikes);
        notifyDataSetChanged();
    }

    public int getLikeCount() {
        return mLikes.size();
    }

    public void setShowLoading(boolean showLoading) {
        this.showLoading = showLoading;
        notifyDataSetChanged();
    }
}
