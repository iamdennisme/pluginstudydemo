package com.dennisce.pluginstudydemo.loader;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

import com.dennisce.pluginstudydemo.util.ReflectUtil;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class ApkLoaderManager {
    public static DexClassLoader nowdexClassLoader;
    public static Resources resources;

    public static DexClassLoader loadApk(Context context, String apkPath) {
        File dexFile = context.getDir("dex", Context.MODE_PRIVATE);
        File apkFile = new File(apkPath);
        ClassLoader classLoader = context.getClassLoader();
        DexClassLoader dexClassLoader = new DexClassLoader(apkFile.getAbsolutePath(),
                dexFile.getAbsolutePath(), null, classLoader.getParent());

        nowdexClassLoader = dexClassLoader;
        return dexClassLoader;
    }

    public static Resources loadResources(Context context, String apkPath) {
        if (resources != null) {
            return resources;
        }
        AssetManager mAssetManager = null;
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, apkPath);
            mAssetManager = assetManager;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Resources superRes = context.getResources();
        superRes.getDisplayMetrics();
        superRes.getConfiguration();
        resources = new Resources(mAssetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
        try {
           Class<?> clz= nowdexClassLoader.loadClass("com.dennisce.testplugin.Resource");
            Field field = clz.getDeclaredField("assetManager");
            field.setAccessible(true);
            field.set(null, mAssetManager);
            Field field1 = clz.getDeclaredField("resources");
            field1.setAccessible(true);
            field1.set(null, resources);
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return resources;
    }
}
