package com.dennisce.pluginstudydemo.base;

import android.app.Application;
import android.widget.Toast;

import com.blankj.utilcode.util.Utils;
import com.dennisce.pluginstudydemo.pluginManager.PluginManager;

import timber.log.Timber;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        Utils.init(this);
        if (!PluginManager.initPlugin(this)) {
            Toast.makeText(this, "插件初始化失败", Toast.LENGTH_SHORT).show();
        }

    }
}
