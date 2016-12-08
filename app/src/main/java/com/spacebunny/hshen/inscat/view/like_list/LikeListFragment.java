package com.spacebunny.hshen.inscat.view.like_list;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.spacebunny.hshen.inscat.R;
import com.spacebunny.hshen.inscat.ins.Ins;
import com.spacebunny.hshen.inscat.model.Like;
import com.spacebunny.hshen.inscat.model.Post;
import com.spacebunny.hshen.inscat.utils.ModelUtils;
import com.spacebunny.hshen.inscat.utils.UIUtils;
import com.spacebunny.hshen.inscat.view.base.SpaceItemDecoration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LikeListFragment extends Fragment {
    public static final String TAG = "Like List Fragment";
    public static final String KEY_POST = "post";

    private LikeListAdapter mAdapter;
    Post post;

    public static LikeListFragment newInstance(@NonNull Bundle args) {
        LikeListFragment fragment = new LikeListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView view = (RecyclerView) inflater.inflate(R.layout.fragment_recycler_view, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelOffset(R.dimen.spacing_xsmall)));
        post = ModelUtils.toObject(getArguments().getString(KEY_POST), new TypeToken<Post>(){});

        mAdapter = new LikeListAdapter(new ArrayList<Like>(), new UIUtils.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                AsyncTaskCompat.executeParallel(new LoadLikeTask(mAdapter.getItemCount() / Ins.POST_COUNT_PER_PAGE + 1));
            }
        });
        recyclerView.setAdapter(mAdapter);
    }

    private class LoadLikeTask extends AsyncTask<Void, Void, List<Like>> {

        int page;
        public LoadLikeTask(int page) {
            this.page = page;
        }

        @Override
        protected List<Like> doInBackground(Void... voids) {
            try {
                return Ins.getMediaLikesWithBio(post.id);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Like> likes) {
            if (likes != null) {
                mAdapter.append(likes);
                mAdapter.setShowLoading(likes.size() == Ins.POST_COUNT_PER_PAGE);
            }
            else {
                Snackbar.make(getView(), "Loading likes error!", Snackbar.LENGTH_LONG).show();
            }
        }
    }
}
