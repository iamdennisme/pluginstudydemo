package com.dennisce.pluginstudydemo.loader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;

import com.blankj.utilcode.util.ReflectUtils;
import com.blankj.utilcode.util.ZipUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import dalvik.system.PathClassLoader;

public class ApkLoadManager {
    private volatile static ApkLoadManager apkLoadManager;
    private Context context;
    // APK，由pluginPath及pluginApkName生成
    private File apkFile;
    // 根据abi解压so后生成
    private File libFile;
    // 插件APK的主目录
    private String pluginPath;
    // 插件的classloader
    private PathClassLoader pluginClassLoader;

    private ApkLoadManager() {
    }

    public static ApkLoadManager getSingleton() {
        if (apkLoadManager == null) {
            synchronized (ApkLoadManager.class) {
                if (apkLoadManager == null) {
                    apkLoadManager = new ApkLoadManager();
                }
            }
        }
        return apkLoadManager;
    }

    public void init(Context context, String pluginPath, String pluginApkName) {
        this.context = context;
        this.pluginPath = pluginPath;
        this.apkFile = new File(pluginPath, pluginApkName);
    }

    public boolean load() {
        // 解压so文件
        if (!extractLib()) {
            return false;
        }
        // 生成PluginClassloader
        if (!createPluginClassloader()) {
            return false;
        }
        if (!registerReceivers()) {
            return false;
        }
        // 生成Resource
        return createResource();
    }

    private boolean createPluginClassloader() {
        if (!apkFile.exists()) {
            return false;
        }
        if (libFile == null || !libFile.exists()) {
            return false;
        }
        pluginClassLoader = new PathClassLoader(apkFile.getAbsolutePath(), libFile.getPath(), context.getClassLoader().getParent());
        return true;
    }

    private Boolean createResource() {
        AssetManager assetManager;
        // 创建AssetManager，反射调用addAssetPath 添加APK 资源到AssetManager
        try {
            assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, apkFile.getPath());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        // 创建新的Resources
        Resources superRes = context.getResources();
        Resources resources = new Resources(assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
        // 在插件Activity中，我重写了getAssets()及getResources()
        // 而获取到的AssetManager、Resources就是这里反射设置的。所以插件activty可以正常使用资源
        ReflectUtils.reflect("com.dennisce.testplugin.Resource", pluginClassLoader).field("assetManager", assetManager);
        ReflectUtils.reflect("com.dennisce.testplugin.Resource", pluginClassLoader).field("resources", resources);
        return true;
    }

    private boolean extractLib() {
        String[] abis = Build.SUPPORTED_ABIS;
        for (String abi : abis) {
            if (extractLibWithAbiName(abi)) {
                return true;
            }
        }
        return false;
    }

    private boolean extractLibWithAbiName(String abiName) {
        try {
            ZipUtils.unzipFileByKeyword(apkFile.getPath(), pluginPath, abiName);
            libFile = new File(pluginPath, "lib" + "/" + abiName);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean registerReceivers() {
        Object packageParser = ReflectUtils.reflect("android.content.pm.PackageParser").newInstance().get();
        Object packageObj = ReflectUtils.reflect(packageParser).method("parsePackage", apkFile, PackageManager.GET_RECEIVERS).get();
        List receivers = ReflectUtils.reflect(packageObj).field("receivers").get();
        for (Object receiver : receivers) {
           if (!registerReceiver(receiver)){
               return false;
           }
        }
        return true;
    }

    private boolean registerReceiver(Object receiver) {
        List<IntentFilter> filtes= ReflectUtils.reflect(receiver).field("intents").get();
        for (IntentFilter intentFilter:filtes){
            ActivityInfo receiverInfo=ReflectUtils.reflect(receiver).field("info").get();
            BroadcastReceiver broadcastReceiver=ReflectUtils.reflect(receiverInfo.name,pluginClassLoader).newInstance().get();
            context.registerReceiver(broadcastReceiver,intentFilter);
        }
        return true;
    }

    public PathClassLoader getPluginClassLoader() {
        return pluginClassLoader;
    }
}
