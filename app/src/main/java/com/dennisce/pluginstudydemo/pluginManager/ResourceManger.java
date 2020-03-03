package com.dennisce.pluginstudydemo.pluginManager;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

import com.blankj.utilcode.util.ReflectUtils;

import java.lang.reflect.Method;

public class ResourceManger {
    public static Boolean createResource(Context context, String apkPath) {
        AssetManager assetManager;
        // 创建AssetManager，反射调用addAssetPath 添加APK 资源到AssetManager
        try {
            assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, apkPath);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        // 创建新的Resources
        Resources superRes = context.getResources();
        Resources resources = new Resources(assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
        // 在插件Activity中，我重写了getAssets()及getResources()
        // 而获取到的AssetManager、Resources就是这里反射设置的。所以插件activty可以正常使用资源
        ReflectUtils.reflect("com.dennisce.testplugin.Resource").field("assetManager", assetManager);
        ReflectUtils.reflect("com.dennisce.testplugin.Resource").field("resources", resources);
        return true;
    }

}
