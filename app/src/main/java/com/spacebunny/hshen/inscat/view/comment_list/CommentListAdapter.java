package com.spacebunny.hshen.inscat.view.comment_list;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.spacebunny.hshen.inscat.R;
import com.spacebunny.hshen.inscat.model.Comment;
import com.spacebunny.hshen.inscat.utils.UIUtils;

import java.util.List;

public class CommentListAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_LOADING = 2;
    private static final int VIEW_TYPE_COMMENT = 3;
    private List<Comment> mComments;
    private UIUtils.LoadMoreListener mLoadMoreListener;
    private boolean showLoading;

    public CommentListAdapter(@NonNull List<Comment> comments, @NonNull UIUtils.LoadMoreListener loadMoreListener) {
        this.mComments = comments;
        this.mLoadMoreListener = loadMoreListener;
        this.showLoading = true;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_COMMENT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_comments, parent, false);
            return new CommentViewHolder(view);
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
            Comment comment = mComments.get(position);

            CommentViewHolder commentViewHolder = (CommentViewHolder) holder;

            DraweeController controller = Fresco.newDraweeControllerBuilder()
                                            .setUri(Uri.parse(comment.from.profile_picture))
                                            .setAutoPlayAnimations(true)
                                            .build();
            commentViewHolder.authorPhoto.setController(controller);

            commentViewHolder.authorName.setText(comment.from.full_name);
            commentViewHolder.commentText.setText(comment.text);
        }
    }

    @Override
    public int getItemCount() {
        return showLoading ? mComments.size() + 1 : mComments.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position < mComments.size()
                ? VIEW_TYPE_COMMENT
                : VIEW_TYPE_LOADING;
    }

    public void append(@NonNull List<Comment> moreComments) {
        mComments.addAll(moreComments);
        notifyDataSetChanged();
    }

    public int getCommentCount() {
        return mComments.size();
    }

    public void setShowLoading(boolean showLoading) {
        this.showLoading = showLoading;
        notifyDataSetChanged();
    }
}
