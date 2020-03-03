package com.dennisce.pluginstudydemo.util;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import timber.log.Timber;

public final class ComponentUtils {
    private ComponentUtils() {
    }

    /**
     * 返回AndroidManifest.xml中注册的所有Activity的class
     *
     * @param context     环境
     * @param packageName 包名
     * @return
     */
    public static List<Class> getActivitiesClass(Context context, String packageName) {
        List<Class> returnClassList = new ArrayList<Class>();
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            if (packageInfo.activities != null) {
                Timber.d("Found " + packageInfo.activities.length + " activity in the AndrodiManifest.xml");
                for (ActivityInfo ai : packageInfo.activities) {
                    Class c;
                    try {
                        c = Class.forName(ai.name);
                        if (Activity.class.isAssignableFrom(c)) {
                            returnClassList.add(c);
                            Timber.d("%s...OK", ai.name);
                        }
                    } catch (ClassNotFoundException e) {
                        Timber.d("Class Not Found:%s", ai.name);
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return returnClassList;
    }


    /**
     * 返回AndroidManifest.xml中注册的所有Service的class
     *
     * @param context     环境
     * @param packageName 包名
     * @return
     */
    public static List<Class> getServiceClass(Context context, String packageName) {
        List<Class> returnClassList = new ArrayList<Class>();
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SERVICES);
            if (packageInfo.services != null) {
                Timber.d("Found " + packageInfo.services.length + " service in the AndrodiManifest.xml");
                for (ServiceInfo si : packageInfo.services) {
                    Class c;
                    try {
                        c = Class.forName(si.name);
                        if (Service.class.isAssignableFrom(c)) {
                            returnClassList.add(c);
                            Timber.d("%s...OK", si.name);
                        }
                    } catch (ClassNotFoundException e) {
                        Timber.d("Class Not Found:%s", si.name);
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return returnClassList;
    }

}