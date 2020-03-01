package com.dennisce.pluginstudydemo.loader;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ZipUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

import dalvik.system.PathClassLoader;

public class ApkLoaderManager {
    public static PathClassLoader nowdexClassLoader;
    public static Resources resources;

    public static PathClassLoader loadApk(Context context, String apkPath) {
        File apkFile = new File(apkPath);
        extractSo(context,apkPath);
        PathClassLoader dexClassLoader = new PathClassLoader(apkFile.getAbsolutePath(), new File(context.getCacheDir(), "plugin").getPath(), context.getClassLoader().getParent());
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
            Class<?> clz = nowdexClassLoader.loadClass("com.dennisce.testplugin.Resource");
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


    private static void extractSo(Context context,String apkPath){
        File file =new File(context.getCacheDir(), "plugin/apk");
        try {
            if (file.exists()){
                file.delete();
            }
            ZipUtils.unzipFileByKeyword(apkPath,file.getPath(),".so");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] abis = Build.SUPPORTED_ABIS;
        for(String abi:abis){
           for (File file1: Objects.requireNonNull(new File(file,"lib").listFiles())){
               if (file1.getName().equals(abi)){
                  for (File file2:file1.listFiles()){
                      FileUtils.copy(file2,new File(context.getCacheDir(), "plugin"+"/"+file2.getName()));
                      return;
                  }
               }
           }
        }
    }
}
