package com.spacebunny.hshen.inscat.view.favorite_list;

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

import com.spacebunny.hshen.inscat.R;
import com.spacebunny.hshen.inscat.database.DBReaderHelper;
import com.spacebunny.hshen.inscat.ins.Ins;
import com.spacebunny.hshen.inscat.model.Post;
import com.spacebunny.hshen.inscat.utils.UIUtils;
import com.spacebunny.hshen.inscat.view.base.SpaceItemDecoration;
import com.spacebunny.hshen.inscat.view.post_list.PostListAdapter;

import java.util.ArrayList;
import java.util.List;

public class CategoryPostListFragment extends Fragment {
    public static final String TAG = "Category Post Fragment";
    public static final String KEY_USER_ID = "userid";
    public static final String KEY_CATEGORY_ID = "categoryid";
    public static final String KEY_CATEGORY_NAME = "categoryname";

    private PostListAdapter adapter;
    public DBReaderHelper dbHelper;

    String userid;
    String categoryid;

    public static CategoryPostListFragment newInstance(@NonNull Bundle args) {
        CategoryPostListFragment fragment = new CategoryPostListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DBReaderHelper(getActivity());
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

        userid = getArguments().getString(KEY_USER_ID);
        categoryid = getArguments().getString(KEY_CATEGORY_ID);

        adapter = new PostListAdapter(new ArrayList<Post>(), new UIUtils.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                AsyncTaskCompat.executeParallel(new LoadCategoryPostTask());
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private class LoadCategoryPostTask extends AsyncTask<Void, Void, List<Post>> {


        public LoadCategoryPostTask() {}

        @Override
        protected List<Post> doInBackground(Void... params) {
            List<Post> posts = dbHelper.getPostListInCategory(userid, categoryid);
            return posts;
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
