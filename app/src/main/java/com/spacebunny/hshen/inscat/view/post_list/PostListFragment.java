package com.spacebunny.hshen.inscat.view.post_list;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonSyntaxException;
import com.spacebunny.hshen.inscat.R;
import com.spacebunny.hshen.inscat.ins.Ins;
import com.spacebunny.hshen.inscat.model.Post;
import com.spacebunny.hshen.inscat.utils.UIUtils;
import com.spacebunny.hshen.inscat.view.base.SpaceItemDecoration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PostListFragment extends Fragment {

    PostListAdapter adapter;

    public static PostListFragment newInstance() {
        return new PostListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        RecyclerView view = (RecyclerView) inflater.inflate(R.layout.fragment_recycler_view, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelOffset(R.dimen.spacing_medium)));

        adapter = new PostListAdapter(new ArrayList<Post>(), new UIUtils.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                AsyncTaskCompat.executeParallel(new LoadPostTask(adapter.getDataCount() / Ins.COUNT_PER_PAGE + 1));
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private class LoadPostTask extends AsyncTask<Void, Void, List<Post>> {

        int page;

        public LoadPostTask(int page) {
            this.page = page;
            Log.d("TAG", "page number is " + page);
        }

        @Override
        protected List<Post> doInBackground(Void... params) {
            try {
                List<Post> posts = Ins.getFollowedPostListSelf();
                posts.addAll(Ins.getPostListSelf());
                return posts;
            } catch (IOException | JsonSyntaxException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Post> posts) {
            if (posts != null) {
                adapter.append(posts);
                adapter.setShowLoading(posts.size()==Ins.COUNT_PER_PAGE);
            }
            else {
                Snackbar.make(getView(), "Error!", Snackbar.LENGTH_LONG).show();
            }
        }
    }
}
