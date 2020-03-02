package com.dennisce.pluginstudydemo.base;
import android.app.Application;
import com.blankj.utilcode.util.Utils;
import com.dennisce.pluginstudydemo.PluginManager;
import timber.log.Timber;
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        Utils.init(this);
        PluginManager.initPlugin(this);
    }
}
