package com.dennisce.pluginstudydemo.base;

import android.app.Application;

import com.dennisce.pluginstudydemo.loader.ApkLoaderManager;

import timber.log.Timber;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        ApkLoaderManager.loadApk(this,"/data/user/0/com.dennisce.pluginstudydemo/cache/plugin/app-debug.apk");
        ApkLoaderManager.loadResources(this,"/data/user/0/com.dennisce.pluginstudydemo/cache/plugin/app-debug.apk");
    }
}
