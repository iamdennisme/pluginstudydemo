package com.dennisce.pluginstudydemo.base;

import android.app.Application;

import com.dennisce.pluginstudydemo.loader.ApkLoaderManager;
import com.dennisce.pluginstudydemo.util.FileUtils;

import timber.log.Timber;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        FileUtils.getInstance(this).copyAssetsToSD("plugins", "plugin");
    }
}
