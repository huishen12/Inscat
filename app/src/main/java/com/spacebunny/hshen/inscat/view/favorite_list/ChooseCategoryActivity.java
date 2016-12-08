package com.spacebunny.hshen.inscat.view.favorite_list;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.spacebunny.hshen.inscat.R;
import com.spacebunny.hshen.inscat.view.base.SingleFragmentActivity;

import java.util.ArrayList;

public class ChooseCategoryActivity extends SingleFragmentActivity {

    @NonNull
    @Override
    protected Fragment newFragment() {
        ArrayList<String> chosenCategoryIds = getIntent().getStringArrayListExtra(CategoryListFragment.KEY_CHOSEN_CATEGORY_IDS);
        return CategoryListFragment.newInstance(true, chosenCategoryIds);
    }

    @NonNull
    @Override
    protected String getActivityTitle() {
        return getString(R.string.choose_category);
    }
}
