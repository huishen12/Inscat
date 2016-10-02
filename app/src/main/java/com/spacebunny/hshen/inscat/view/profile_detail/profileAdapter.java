package com.spacebunny.hshen.inscat.view.profile_detail;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.spacebunny.hshen.inscat.model.Post;
import com.spacebunny.hshen.inscat.model.User;

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
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
