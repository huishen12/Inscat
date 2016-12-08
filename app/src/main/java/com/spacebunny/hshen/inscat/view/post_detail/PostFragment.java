package com.spacebunny.hshen.inscat.view.post_detail;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.spacebunny.hshen.inscat.R;
import com.spacebunny.hshen.inscat.database.DBReaderHelper;
import com.spacebunny.hshen.inscat.ins.Ins;
import com.spacebunny.hshen.inscat.model.Category;
import com.spacebunny.hshen.inscat.model.Post;
import com.spacebunny.hshen.inscat.utils.ModelUtils;
import com.spacebunny.hshen.inscat.view.favorite_list.CategoryListFragment;
import com.spacebunny.hshen.inscat.view.post_list.PostListFragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class PostFragment extends Fragment {
    public static final String KEY_POST = "post";
    public static final int REQ_CODE_CATEGORY = 100;

    private PostAdapter adapter;
    private Post post;

    public DBReaderHelper dbHelper;

    public static PostFragment newInstance(@NonNull Bundle args) {
        PostFragment fragment = new PostFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        post = ModelUtils.toObject(getArguments().getString(KEY_POST), new TypeToken<Post>(){});
        adapter = new PostAdapter(this, post);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        AsyncTaskCompat.executeParallel(new LoadCollectedCategoryIdsTask(Ins.getCurrentUser().id));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_CATEGORY && resultCode == Activity.RESULT_OK) {
            List<String> chosenCategoryIds = data.getStringArrayListExtra(CategoryListFragment.KEY_CHOSEN_CATEGORY_IDS);
            List<String> addedCategoryIds = new ArrayList<>();
            List<String> removedCategoryIds = new ArrayList<>();
            List<String> collectedCategoryIds = adapter.getReadOnlyCollectedCategoryIds();

            for (String chosenCategoryId : chosenCategoryIds) {
                if (!collectedCategoryIds.contains(chosenCategoryId)) {
                    addedCategoryIds.add(chosenCategoryId);
                }
            }

            for (String collectedCategoryId : collectedCategoryIds) {
                if (!chosenCategoryIds.contains(collectedCategoryId)) {
                    removedCategoryIds.add(collectedCategoryId);
                }
            }
            AsyncTaskCompat.executeParallel(new UpdateCategoryTask(addedCategoryIds, removedCategoryIds));
        }
    }

    private class LoadCollectedCategoryIdsTask extends AsyncTask<Void, Void, List<String>> {
        private String userid;

        private LoadCollectedCategoryIdsTask(String userid) {
            this.userid = userid;
        }

        @Override
        protected List<String> doInBackground(Void... params) {

            List<Category> userCategories = dbHelper.getUserCategoryList(userid);
            List<String> postCategoryIds = dbHelper.getPostCategoryIdList(post.id);

            List<String> collectedCategoryIds = new ArrayList<>();
            for (Category userCategory: userCategories) {
                if (postCategoryIds.contains(userCategory.id)) {
                    collectedCategoryIds.add(userCategory.id);
                }
            }
            return collectedCategoryIds;
        }

        @Override
        protected void onPostExecute(List<String> collectedCategoryIds) {
            adapter.updateCollectedCategoryIds(collectedCategoryIds);
        }
    }

    private class UpdateCategoryTask extends AsyncTask<Void, Void, Void> {

        private List<String> added;
        private List<String> removed;

        private UpdateCategoryTask(@NonNull List<String> added, @NonNull List<String> removed) {
            this.added = added;
            this.removed = removed;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            int post_count;
            for (String addedId : added) {
                dbHelper.insertPostCategory(addedId, post);
                post_count = dbHelper.getCategoryPostCount(addedId);
                dbHelper.updateCategory(Integer.parseInt(addedId), ++post_count);
            }

            for (String removedId : removed) {
                Integer id = dbHelper.getPostInCategoryId(post.id, removedId);
                dbHelper.deletePostCategory(id);
                post_count = dbHelper.getCategoryPostCount(removedId);
                dbHelper.updateCategory(Integer.parseInt(removedId), --post_count);
            }

            int count = post.category_count + added.size() - removed.size();
            dbHelper.updatePost(post.id, count);
            return null;
        }

        @Override
        protected void onPostExecute(Void strings) {
            adapter.updateCollectedCategoryIds(added, removed);
        }
    }
}