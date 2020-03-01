package com.dennisce.pluginstudydemo.base;

import android.app.Application;

import com.blankj.utilcode.util.ResourceUtils;
import com.dennisce.pluginstudydemo.hookhelper.HookHelper;
import com.dennisce.pluginstudydemo.loader.ApkLoaderManager;

import java.io.File;

import timber.log.Timber;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        if (new File(getCacheDir(), "plugin").exists()) {
            new File(getCacheDir(), "plugin").delete();
        }
        ResourceUtils.copyFileFromAssets("plugins", new File(getCacheDir(), "plugin").getPath());
        ApkLoaderManager.loadApk(this, new File(getCacheDir(), "plugin/app-debug.apk").getPath());
        ApkLoaderManager.loadResources(this, new File(getCacheDir(), "plugin/app-debug.apk").getPath());
        HookHelper.tryHookInstrumentation();
        HookHelper.tryHookStartActivity();

    }
}
