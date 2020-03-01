package com.dennisce.pluginstudydemo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import com.dennisce.pluginstudydemo.hookhelper.HookHelper;
import com.dennisce.pluginstudydemo.loader.ApkLoaderManager;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getResources().getColor(R.color.colorAccent);
        getResources().getDrawable(R.drawable.ic_launcher_background);
    }

    @Override
    public Resources getResources() {
        return super.getResources();
    }

    @Override
    public AssetManager getAssets() {
        return super.getAssets();
    }

    public void testHookActivity(View view) throws ClassNotFoundException {
        if (!HookHelper.tryHookInstrumentation()){
            return;
        }
        if (!HookHelper.tryHookStartActivity()){
            return;
        }
        ApkLoaderManager.loadApk(this,"/data/user/0/com.dennisce.pluginstudydemo/cache/plugin/app-debug.apk");
        ApkLoaderManager.loadResources(this,"/data/user/0/com.dennisce.pluginstudydemo/cache/plugin/app-debug.apk");
        Intent newIntent = new Intent();
        newIntent.setComponent(new ComponentName("com.dennisce.testplugin","com.dennisce.testplugin.PluginActivity"));
        startActivity(newIntent);
    }

}
