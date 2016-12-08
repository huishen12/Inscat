package com.spacebunny.hshen.inscat.view.favorite_list;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.spacebunny.hshen.inscat.R;
import com.spacebunny.hshen.inscat.database.DBReaderHelper;
import com.spacebunny.hshen.inscat.ins.Ins;
import com.spacebunny.hshen.inscat.model.Category;
import com.spacebunny.hshen.inscat.model.User;
import com.spacebunny.hshen.inscat.utils.UIUtils;
import com.spacebunny.hshen.inscat.view.base.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CategoryListFragment extends Fragment {
    public static final int REQ_CODE_NEW_CATEGORY = 100;
    public static final String KEY_CHOOSING_MODE = "choose_mode";
    public static final String KEY_CHOSEN_CATEGORY_IDS = "chosen_category_ids";

    public RecyclerView recyclerView;
    public FloatingActionButton fab;

    private CategoryListAdapter adapter;
    private boolean isChoosingMode;
    private List<String> chosenCategoryIds;

    public DBReaderHelper dbHelper;

    public static CategoryListFragment newInstance(boolean isChoosingMode, @Nullable ArrayList<String> chosenCategoryIds) {
        Bundle args = new Bundle();
        args.putBoolean(KEY_CHOOSING_MODE, isChoosingMode);
        args.putStringArrayList(KEY_CHOSEN_CATEGORY_IDS, chosenCategoryIds);

        CategoryListFragment fragment = new CategoryListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        dbHelper = new DBReaderHelper(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fab_recycler_view, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        isChoosingMode = getArguments().getBoolean(KEY_CHOOSING_MODE);
        if (isChoosingMode) {
            chosenCategoryIds = getArguments().getStringArrayList(KEY_CHOSEN_CATEGORY_IDS);
            if (chosenCategoryIds == null) {
                chosenCategoryIds = new ArrayList<>();
            }
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.spacing_medium)));
        adapter = new CategoryListAdapter(this, new ArrayList<Category>(), new UIUtils.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                AsyncTaskCompat.executeParallel(new LoadCategoryTask(adapter.getDataCount() / Ins.COUNT_PER_PAGE + 1));
            }
        }, isChoosingMode);
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewCategoryDialogFragment newCategoryDialogFragment = NewCategoryDialogFragment.newInstance();
                newCategoryDialogFragment.setTargetFragment(CategoryListFragment.this, REQ_CODE_NEW_CATEGORY);
                newCategoryDialogFragment.show(getFragmentManager(), NewCategoryDialogFragment.TAG);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_NEW_CATEGORY && resultCode == Activity.RESULT_OK) {
            String categoryName = data.getStringExtra(NewCategoryDialogFragment.KEY_CATEGORY_NAME);
            String categoryDescription = data.getStringExtra(NewCategoryDialogFragment.KEY_CATEGORY_DESCRIPTION);
            if (!TextUtils.isEmpty(categoryName)) {
                AsyncTaskCompat.executeParallel(new NewCategoryTask(categoryName, categoryDescription, Ins.getCurrentUser().id));
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (isChoosingMode) {
            inflater.inflate(R.menu.category_list_choose_mode_menu, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save) {
            ArrayList<String> chosenCategoryIds = adapter.getSelectedCategoryIds();

            Intent result = new Intent();
            result.putStringArrayListExtra(KEY_CHOSEN_CATEGORY_IDS, chosenCategoryIds);
            getActivity().setResult(Activity.RESULT_OK, result);
            getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private class LoadCategoryTask extends AsyncTask<Void, Void, List<Category>> {

        int page;

        public LoadCategoryTask(int page) {
            this.page = page;
        }

        @Override
        protected List<Category> doInBackground(Void... voids) {
            User self = Ins.getCurrentUser();
            List<Category> categories = dbHelper.getUserCategoryList(self.id);
            return categories;
        }

        @Override
        protected void onPostExecute(List<Category> categories) {
            if (categories != null) {
                if (isChoosingMode) {
                    for (Category category : categories) {
                        if (chosenCategoryIds.contains(category.id)) {
                            category.isChoosing = true;
                        }
                    }
                }
                adapter.append(categories);
                adapter.setShowLoading(categories.size() == Ins.COUNT_PER_PAGE);
            }
            else {
                Snackbar.make(getView(), "Error!", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private class NewCategoryTask extends AsyncTask<Void, Void, Category> {
        private String name;
        private String description;
        private String userid;

        private NewCategoryTask(String name, String description, String userid) {
            this.name = name;
            this.description = description;
            this.userid = userid;
        }

        @Override
        protected Category doInBackground(Void... voids) {
            dbHelper.insertCategory(name, description, userid, 0);
            Category newCategory = new Category();
            newCategory.name = name;
            newCategory.description = description;
            newCategory.post_count = 0;
            newCategory.isChoosing = false;
            return newCategory;
        }

        @Override
        protected void onPostExecute(Category category) {
            if (category != null) {
                adapter.prepend(Collections.singletonList(category));
            } else {
                Snackbar.make(getView(), "Error!", Snackbar.LENGTH_LONG).show();
            }
        }
    }
}
