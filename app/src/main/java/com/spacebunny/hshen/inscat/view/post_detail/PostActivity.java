package com.spacebunny.hshen.inscat.view.post_detail;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.spacebunny.hshen.inscat.view.base.SingleFragmentActivity;

public class PostActivity extends SingleFragmentActivity {

    public static final String KEY_POST_TITLE = "post_title";

    @NonNull
    @Override
    protected Fragment newFragment() {
        return PostFragment.newInstance(getIntent().getExtras());
    }

    protected String getActivityTitle() {
        return getIntent().getStringExtra(KEY_POST_TITLE);
    }
}
