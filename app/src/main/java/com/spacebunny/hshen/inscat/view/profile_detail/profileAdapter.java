package com.spacebunny.hshen.inscat.view.profile_detail;

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
import com.spacebunny.hshen.inscat.model.User;
import com.spacebunny.hshen.inscat.utils.ModelUtils;
import com.spacebunny.hshen.inscat.utils.UIUtils;
import com.spacebunny.hshen.inscat.view.post_detail.PostActivity;
import com.spacebunny.hshen.inscat.view.post_detail.PostFragment;
import com.spacebunny.hshen.inscat.view.post_list.PostViewHolder;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter {
    public static final String TAG = "Profile Adapter";
    private User user;
    private List<Post> data;
    private static final int VIEW_TYPE_PROFILE_INFO = 0;
    private static final int VIEW_TYPE_PROFILE_POST = 1;
    private static final int VIEW_TYPE_PROFILE_LOADING = 2;

    private UIUtils.LoadMoreListener loadMoreListener;
    private boolean showLoading;

    ProfileAdapter(User user, List<Post> data, @NonNull UIUtils.LoadMoreListener loadMoreListener) {
        this.user = user;
        this.data = data;
        this.loadMoreListener = loadMoreListener;
        this.showLoading = true;
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
            case VIEW_TYPE_PROFILE_LOADING:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_loading, parent, false);
                return new RecyclerView.ViewHolder(view) {};
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final int viewType = getItemViewType(position);
        switch (viewType) {
            case VIEW_TYPE_PROFILE_LOADING:
                loadMoreListener.onLoadMore();
                break;

            case VIEW_TYPE_PROFILE_INFO:
                ProfileInfoViewHolder infoViewHolder = (ProfileInfoViewHolder) holder;
                infoViewHolder.profilePhoto.setImageURI(Uri.parse(user.profile_picture));
                infoViewHolder.profileName.setText(user.full_name);
//                infoViewHolder.profileDescription.setText(user.description);
                infoViewHolder.profileFollowing.setText(String.valueOf(user.counts.follows));
                infoViewHolder.profileFollower.setText(String.valueOf(user.counts.followed_by));
                break;

            case VIEW_TYPE_PROFILE_POST:
                final Post post = data.get(position-1);
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
        return showLoading ? data.size() + 2 : data.size() + 1;
    }

    @Override
    public int getItemViewType(int position){
        Log.d("TAG", "Position is " + position);
        Log.d("TAG", "Data size in view type is " + data.size());
        if (position == 0) {
            return VIEW_TYPE_PROFILE_INFO;
        }
        else if (position >= data.size()){
            return VIEW_TYPE_PROFILE_LOADING;
        }
        else {
            return VIEW_TYPE_PROFILE_POST;
        }
    }

    public void append(@NonNull List<Post> morePosts) {
        data.addAll(morePosts);
        notifyDataSetChanged();
    }



    public int getDataCount() {
        Log.d(TAG, "Data size is " + data.size());
        return data.size();
    }

    public void setShowLoading(boolean showLoading) {
        this.showLoading = showLoading;
        notifyDataSetChanged();
    }
}
