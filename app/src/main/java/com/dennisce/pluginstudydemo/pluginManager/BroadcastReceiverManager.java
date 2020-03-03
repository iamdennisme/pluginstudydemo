package com.dennisce.pluginstudydemo.pluginManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;

import com.blankj.utilcode.util.ReflectUtils;

import java.io.File;
import java.util.List;

public class BroadcastReceiverManager {

    public static boolean registerReceivers(Context context, String apkPath) {
        Object packageParser = ReflectUtils.reflect("android.content.pm.PackageParser").newInstance().get();
        Object packageObj = ReflectUtils.reflect(packageParser).method("parsePackage", new File(apkPath), PackageManager.GET_RECEIVERS).get();
        List receivers = ReflectUtils.reflect(packageObj).field("receivers").get();
        for (Object receiver : receivers) {
            if (!registerReceiver(context, receiver)) {
                return false;
            }
        }
        return true;
    }

    private static boolean registerReceiver(Context context, Object receiver) {
        List<IntentFilter> filtes = ReflectUtils.reflect(receiver).field("intents").get();
        for (IntentFilter intentFilter : filtes) {
            ActivityInfo receiverInfo = ReflectUtils.reflect(receiver).field("info").get();
            BroadcastReceiver broadcastReceiver = ReflectUtils.reflect(receiverInfo.name).newInstance().get();
            context.registerReceiver(broadcastReceiver, intentFilter);
        }
        return true;
    }
}
