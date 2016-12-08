package com.spacebunny.hshen.inscat.view.comment_list;


import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.spacebunny.hshen.inscat.view.base.SingleFragmentActivity;

public class CommentListActivity extends SingleFragmentActivity {

    @NonNull
    @Override
    protected Fragment newFragment() {
        Fragment fragment = CommentListFragment.newInstance(getIntent().getExtras());
        setTitle("Comments");
        return fragment;
    }
}
