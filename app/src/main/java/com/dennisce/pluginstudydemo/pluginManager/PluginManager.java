package com.dennisce.pluginstudydemo.pluginManager;

import android.content.Context;

import com.blankj.utilcode.util.ResourceUtils;
import com.dennisce.pluginstudydemo.base.Constant;
import com.dennisce.pluginstudydemo.hookhelper.HookHelper;

import java.io.File;

import static com.dennisce.pluginstudydemo.base.Constant.ASSETS_PLUGIN_PATH;
import static com.dennisce.pluginstudydemo.base.Constant.PLUGIN_APK_NAME;
import static com.dennisce.pluginstudydemo.base.Constant.PLUGIN_PATH;

public class PluginManager {
    public static boolean initPlugin(Context context) {
        String apkPath = new File(PLUGIN_PATH, PLUGIN_APK_NAME).getPath();
        return ResourceUtils.copyFileFromAssets(ASSETS_PLUGIN_PATH, apkPath)// 拷贝插件APK到 getFilesDir()/plugin/plugin.apk
                && CodeSourceManger.mergeCodeSource(context, apkPath, new File(PLUGIN_PATH, Constant.OPTIMIZED_DIRECTORY).getPath()) // 合并dex及so库到classloader
                && ResourceManger.createResource(context, apkPath) // 添加资源，生成自定义Resource，设置插件使用自定义Resource
                && BroadcastReceiverManager.registerReceivers(context, apkPath)// 注册插件的BroadcastReceiver
                && ContentProviderManager.installProviders(context, apkPath)// 安装插件的ContentProvider
                && HookHelper.tryHookInstrumentation()// hook Instrumentation activity创建需要
                && HookHelper.tryHookIActivityManager() // hook ActivityManager activity及service创建需要
                && HookHelper.tryHookActivityThread(); // hook mH，service创建需要
    }
}
