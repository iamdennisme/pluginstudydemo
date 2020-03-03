package com.dennisce.pluginstudydemo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import com.dennisce.pluginstudydemo.loader.ApkLoadManager;
import com.dennisce.pluginstudydemo.stub.StubActivity;
import com.dennisce.pluginstudydemo.util.BroadcastReceiverUtil;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new BroadcastReceiverUtil().register(this);
    }

    @Override
    public Resources getResources() {
        return super.getResources();
    }

    @Override
    public AssetManager getAssets() {
        return super.getAssets();
    }

    public void startPluginActivity(View view) {
        Intent newIntent = new Intent();
        newIntent.setComponent(new ComponentName("com.dennisce.testplugin", "com.dennisce.testplugin.PluginActivity"));
        startActivity(newIntent);
    }

    public void startHostActivity(View view) {
        startActivity(new Intent(this, StubActivity.class));
    }

    public void startService(View view) {
        Intent newIntent = new Intent();
        newIntent.setComponent(new ComponentName("com.dennisce.testplugin", "com.dennisce.testplugin.PluginService"));
        startService(newIntent);
    }

    public void stopService(View view) {
        Intent newIntent = new Intent();
        newIntent.setComponent(new ComponentName("com.dennisce.testplugin", "com.dennisce.testplugin.PluginService"));
        stopService(newIntent);
    }

    public void sendBroadcast(View view) {
        Intent intent = new  Intent();
        intent.setAction("test");
        try {
            intent.setComponent(new  ComponentName(this, ApkLoadManager.getSingleton().getPluginClassLoader().loadClass("com.dennisce.testplugin,PluginManifestBroadcastReceiver").getClass()));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        sendBroadcast(intent);
    }

}
