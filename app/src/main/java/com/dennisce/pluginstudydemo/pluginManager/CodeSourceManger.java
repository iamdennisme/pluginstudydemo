package com.dennisce.pluginstudydemo.pluginManager;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.blankj.utilcode.util.ReflectUtils;
import com.blankj.utilcode.util.ZipUtils;
import com.dennisce.pluginstudydemo.base.Constant;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import dalvik.system.DexFile;

public class CodeSourceManger {
    public static boolean mergeCodeSource(Context context, String apkPath, String optimizedDirectory) {
        return mergePluginDex(context, apkPath, optimizedDirectory) && mergeNative(context, apkPath, optimizedDirectory);
    }

    private static boolean mergePluginDex(Context context, String apkPath, String optimizedDirectory) {
        try {
            Object pathList = ReflectUtils.reflect(context.getClassLoader()).field("pathList").get();
            // 原有的dex数组
            Object[] oldElementsArray = ReflectUtils.reflect(pathList).field("dexElements").get();
            Class<?> elementClass = oldElementsArray.getClass().getComponentType();
            // 合并之后的dex数组
            Object[] newElementsArray = (Object[]) Array.newInstance(elementClass, oldElementsArray.length + 1);
            // 生成插件的ElementClass
            Object newElementClass = ReflectUtils.reflect(elementClass).newInstance(new File(apkPath), false, new File(apkPath), parseDex(apkPath, optimizedDirectory)).get();
            // 把原始的elements复制进去
            System.arraycopy(oldElementsArray, 0, newElementsArray, 0, oldElementsArray.length);
            // 插件的那个element复制进去
            System.arraycopy(new Object[]{newElementClass}, 0, newElementsArray, oldElementsArray.length, new Object[]{newElementClass}.length);
            // 替换
            ReflectUtils.reflect(pathList).field("dexElements", newElementsArray);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 合并so库，这里只根据sdk 23 进行了实现，据我观察源码，sdk23 sdk22 sdk 24 的 makePathElements方法参数都不一样，但都比较简单，这里就不一一实现了
    private static boolean mergeNative(Context context, String apkPath, String optimizedDirectory) {
        try {
            String librarySearchPath = null;
            try {
                ZipUtils.unzipFileByKeyword(apkPath, new File(apkPath).getParentFile().getPath(), Build.CPU_ABI);
                librarySearchPath = new File(Constant.PLUGIN_PATH + File.separator + "lib" + File.separator + Build.CPU_ABI).getAbsolutePath();
                // 需要删除其余的文件,防止占用磁盘空间。
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (TextUtils.isEmpty(librarySearchPath)) {
                return false;
            }

            List<File> pluginNativeLibraryDirList = new LinkedList<>();
            pluginNativeLibraryDirList.add(new File(librarySearchPath));
            Object dexPathList = ReflectUtils.reflect(context.getClassLoader()).field("pathList").get();

            // 添加插件的so库
            List<File> allNativeLibDirList = new ArrayList<>(pluginNativeLibraryDirList);
            // 添加宿主的so库
            List<File> old_nativeLibraryDirectories = ReflectUtils.reflect(dexPathList).field("nativeLibraryDirectories").get();
            allNativeLibDirList.addAll(old_nativeLibraryDirectories);
            // 添加system的so库
            List<File> systemNativeLibraryDirectories = ReflectUtils.reflect(dexPathList).field("systemNativeLibraryDirectories").get();
            allNativeLibDirList.addAll(systemNativeLibraryDirectories);
            // 反射makePathElements获取c++存放的Element
            Object[] allNativeLibraryPathElements = ReflectUtils.reflect(dexPathList).method("makePathElements",
                    allNativeLibDirList, new File(optimizedDirectory), new ArrayList<IOException>()).get();
            // 重新设置so库为合并后的结果
            ReflectUtils.reflect(dexPathList).field("nativeLibraryPathElements", allNativeLibraryPathElements);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static DexFile parseDex(String zipFilePath, String optimizedDirectory) throws IOException {
        String dexFilePath = optimizedPathFor(new File(zipFilePath), new File(optimizedDirectory));
        return DexFile.loadDex(zipFilePath, dexFilePath, 0);
    }

    private static String optimizedPathFor(File path, File optimizedDirectory) {
        final String DEX_SUFFIX = ".dex";
        String fileName = path.getName();
        if (!fileName.endsWith(DEX_SUFFIX)) {
            int lastDot = fileName.lastIndexOf(".");
            if (lastDot < 0) {
                fileName += DEX_SUFFIX;
            } else {
                fileName = fileName.substring(0, lastDot) +
                        DEX_SUFFIX;
            }
        }
        File result = new File(optimizedDirectory, fileName);
        return result.getPath();
    }
}
