package com.dennisce.pluginstudydemo.pluginManager;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;

import com.blankj.utilcode.util.ReflectUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ContentProviderManager {
    public static boolean installProviders(Context context, String apkPath) {
        List<ProviderInfo> providerInfoList = getProviders(apkPath);
        for (ProviderInfo providerInfo : providerInfoList) {
            providerInfo.applicationInfo.packageName = context.getPackageName();
        }
        Object currentActivityThread = ReflectUtils.reflect("android.app.ActivityThread").field("sCurrentActivityThread").get();
        ReflectUtils.reflect(currentActivityThread).method("installContentProviders", context, providerInfoList);
        return true;
    }

    private static List<ProviderInfo> getProviders(String apkPath) {
        Object packageParser = ReflectUtils.reflect("android.content.pm.PackageParser").newInstance().get();
        Object packageObj = ReflectUtils.reflect(packageParser).method("parsePackage", new File(apkPath), PackageManager.GET_PROVIDERS).get();
        List providers = ReflectUtils.reflect(packageObj).field("providers").get();
        Object defaultUserState = ReflectUtils.reflect("android.content.pm.PackageUserState").newInstance().get();
        int userId = ReflectUtils.reflect("android.os.UserHandle").method("getCallingUserId").get();
        List<ProviderInfo> ret = new ArrayList<>();
        for (Object provider : providers) {
            ProviderInfo info = ReflectUtils.reflect(packageParser).method("generateProviderInfo", provider, 0, defaultUserState, userId).get();
            ret.add(info);
        }
        return ret;
    }
}
