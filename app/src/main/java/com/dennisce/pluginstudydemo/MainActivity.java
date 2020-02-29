package com.dennisce.pluginstudydemo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dennisce.pluginstudydemo.hookhelper.HookHelper;
import com.dennisce.pluginstudydemo.loader.ApkLoaderManager;


public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void testHookActivity(View view) throws ClassNotFoundException {
        if (!HookHelper.tryHookInstrumentation()){
            return;
        }
        if (!HookHelper.tryHookStartActivity()){
            return;
        }
        Class<?> clz = ApkLoaderManager.nowdexClassLoader.loadClass("com.dennisce.testplugin.PluginActivity");
        Intent newIntent = new Intent();
        newIntent.setComponent(new ComponentName("com.dennisce.testplugin","com.dennisce.testplugin.PluginActivity"));
        startActivity(newIntent);
    }

}
