package com.dennisce.pluginstudydemo.loader;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

import com.dennisce.pluginstudydemo.util.ReflectUtil;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

public class ApkLoaderManager {
    public static PathClassLoader nowdexClassLoader;
    public static Resources resources;

    public static PathClassLoader loadApk(Context context, String apkPath) {
        File apkFile = new File(apkPath);
        PathClassLoader dexClassLoader = new PathClassLoader(apkFile.getAbsolutePath(),"/data/user/0/com.dennisce.pluginstudydemo/cache/plugin/", context.getClassLoader().getParent());
        nowdexClassLoader = dexClassLoader;
        return dexClassLoader;
    }

    public static Resources loadResources(Context context, String apkPath) {
        if (resources != null) {
            return resources;
        }
        AssetManager assetManager = null;
        try {
            assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, apkPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Resources superRes = context.getResources();
        superRes.getDisplayMetrics();
        superRes.getConfiguration();
        resources = new Resources(assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
        try {
           Class<?> clz= nowdexClassLoader.loadClass("com.dennisce.testplugin.Resource");
            Field field = clz.getDeclaredField("assetManager");
            field.setAccessible(true);
            field.set(null, assetManager);
            Field field1 = clz.getDeclaredField("resources");
            field1.setAccessible(true);
            field1.set(null, resources);
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return resources;
    }
}
