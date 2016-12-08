package com.spacebunny.hshen.inscat.view.like_list;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.spacebunny.hshen.inscat.view.base.SingleFragmentActivity;

public class LikeListActivity extends SingleFragmentActivity {

    @NonNull
    @Override
    protected Fragment newFragment() {
        Fragment fragment = LikeListFragment.newInstance(getIntent().getExtras());
        setTitle("Likes");
        return fragment;
    }
}
