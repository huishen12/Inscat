package com.spacebunny.hshen.inscat.view.favorite_list;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.spacebunny.hshen.inscat.view.base.SingleFragmentActivity;

public class CategoryPostListActivity extends SingleFragmentActivity{
    @NonNull
    @Override
    protected Fragment newFragment() {
        return CategoryPostListFragment.newInstance(getIntent().getExtras());
    }

    protected String getActivityTitle() {
        return getIntent().getStringExtra(CategoryPostListFragment.KEY_CATEGORY_NAME);
    }
}
