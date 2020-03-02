package com.dennisce.pluginstudydemo.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import timber.log.Timber;

public final class ClassUtils {
    private ClassUtils() {
    }

    /**
     * 返回AndroidManifest.xml中注册的所有Activity的class
     *
     * @param context     环境
     * @param packageName 包名
     * @param excludeList 排除class列表
     * @return
     */
    public final static List<Class> getActivitiesClass(Context context, String packageName, List<Class> excludeList) {

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
                Timber.d("Filter out, left " + returnClassList.size() + " activity," + Arrays.toString(returnClassList.toArray()));

                if (excludeList != null) {
                    returnClassList.removeAll(excludeList);
                    Timber.d("Exclude " + excludeList.size() + " activity," + Arrays.toString(excludeList.toArray()));
                }
                Timber.d("Return " + returnClassList.size() + " activity," + Arrays.toString(returnClassList.toArray()));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return returnClassList;
    }

}