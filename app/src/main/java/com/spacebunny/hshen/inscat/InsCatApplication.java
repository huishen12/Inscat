package com.spacebunny.hshen.inscat;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

public class InsCatApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}

