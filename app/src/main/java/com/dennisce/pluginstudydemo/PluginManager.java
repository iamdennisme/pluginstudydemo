package com.dennisce.pluginstudydemo;

import android.content.Context;

import com.blankj.utilcode.util.ResourceUtils;
import com.dennisce.pluginstudydemo.hookhelper.HookHelper;
import com.dennisce.pluginstudydemo.loader.ApkLoadManager;

import java.io.File;

import static com.dennisce.pluginstudydemo.base.Constant.ASSETS_PLUGIN_PATH;
import static com.dennisce.pluginstudydemo.base.Constant.PLUGIN_APK_NAME;
import static com.dennisce.pluginstudydemo.base.Constant.PLUGIN_PATH;

public class PluginManager {
    public static boolean initPlugin(Context context) {
        // 拷贝插件APK到 getFilesDir()/plugin/plugin.apk
        if (!ResourceUtils.copyFileFromAssets(ASSETS_PLUGIN_PATH, new File(PLUGIN_PATH, PLUGIN_APK_NAME).getPath())) {
            return false;
        }
        // hook Instrumentation
        if (!HookHelper.tryHookInstrumentation()) {
            return false;
        }
        // hook ActivityManager
        if (!HookHelper.tryHookIActivityManager()) {
            return false;
        }

        // hook HookActivityThread
        if (!HookHelper.tryHookActivityThread()) {
            return false;
        }

        if (!loadApk(context)) {
            return false;
        }

        return false;
    }

    private static boolean loadApk(Context context) {
        ApkLoadManager apkLoadManager = ApkLoadManager.getSingleton();
        apkLoadManager.init(context, PLUGIN_PATH, PLUGIN_APK_NAME);
        return apkLoadManager.load();
    }
}
