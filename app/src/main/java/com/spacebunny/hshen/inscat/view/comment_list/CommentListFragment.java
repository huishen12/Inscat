package com.spacebunny.hshen.inscat.view.comment_list;


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
import com.spacebunny.hshen.inscat.model.Comment;
import com.spacebunny.hshen.inscat.model.Post;
import com.spacebunny.hshen.inscat.utils.ModelUtils;
import com.spacebunny.hshen.inscat.utils.UIUtils;
import com.spacebunny.hshen.inscat.view.base.SpaceItemDecoration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommentListFragment extends Fragment{
    public static final String TAG = "Comment List Fragment";
    public static final String KEY_POST = "post";

    private CommentListAdapter mAdapter;
    Post post;

    public static CommentListFragment newInstance(@NonNull Bundle args) {
        CommentListFragment fragment = new CommentListFragment();
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

        mAdapter = new CommentListAdapter(new ArrayList<Comment>(), new UIUtils.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                AsyncTaskCompat.executeParallel(new LoadCommentTask(mAdapter.getCommentCount() / Ins.POST_COUNT_PER_PAGE + 1));
            }
        });
        recyclerView.setAdapter(mAdapter);
    }

    private class LoadCommentTask extends AsyncTask<Void, Void, List<Comment>> {
        int page;
        public LoadCommentTask(int page) {
            this.page = page;
        }

        @Override
        protected List<Comment> doInBackground(Void... voids) {
            try {
                return Ins.getMediaComments(post.id);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Comment> comments) {
            if (comments != null) {
                mAdapter.append(comments);
                mAdapter.setShowLoading(comments.size() == Ins.POST_COUNT_PER_PAGE);
            }
            else {
                Snackbar.make(getView(), "Loading comments error!", Snackbar.LENGTH_LONG).show();
            }
        }
    }
}
