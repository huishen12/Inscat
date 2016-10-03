package com.spacebunny.hshen.inscat.view.profile_detail;


import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.spacebunny.hshen.inscat.view.base.SingleFragmentActivity;

public class ProfileActivity extends SingleFragmentActivity {

    @NonNull
    @Override
    protected Fragment newFragment() {
        return ProfileFragment.newInstance();
    }
}
