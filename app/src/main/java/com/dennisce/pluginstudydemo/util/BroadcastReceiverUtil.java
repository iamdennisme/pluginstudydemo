package com.dennisce.pluginstudydemo.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;

import com.blankj.utilcode.util.ReflectUtils;
import com.dennisce.pluginstudydemo.loader.ApkLoadManager;

public class BroadcastReceiverUtil {
    public void register(Context context){
        registerNormal(context);
    }
    private void registerManifestRegisterBoard(Context context){


    }

    private void registerNormal(Context context){
        try {
          BroadcastReceiver  receiver = (BroadcastReceiver) ApkLoadManager.getSingleton().getPluginClassLoader().loadClass("com.dennisce.testplugin.PluginBroadcastReceiver").newInstance();
            context.registerReceiver(receiver,new IntentFilter("Plugin_Receiver"));

        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }
}
