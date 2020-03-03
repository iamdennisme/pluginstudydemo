package com.dennisce.pluginstudydemo.hookhelper;

import android.app.Instrumentation;
import android.os.Build;
import android.os.Handler;

import com.blankj.utilcode.util.ReflectUtils;

import java.lang.reflect.Proxy;

public class HookHelper {
    public static boolean tryHookIActivityManager() {
        try {
            Object singleton;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                singleton = ReflectUtils.reflect("android.app.ActivityManager").field("IActivityManagerSingleton").get();
            } else {
                singleton = ReflectUtils.reflect("android.app.ActivityManagerNative").field("gDefault").get();
            }
            final Object iActivityManager = ReflectUtils.reflect(singleton).field("mInstance").get();
            Class<?> iActivityManagerInterface = ReflectUtils.reflect("android.app.IActivityManager").get();
            Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                    new Class[]{iActivityManagerInterface}, new IActivityManagerInvocationHandler(iActivityManager));
            ReflectUtils.reflect(singleton).field("mInstance", proxy);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean tryHookInstrumentation() {
        try {
            Object currentActivityThread = ReflectUtils.reflect("android.app.ActivityThread").field("sCurrentActivityThread").get();
            Instrumentation rawInstrumentation = ReflectUtils.reflect(currentActivityThread).field("mInstrumentation").get();
            ReflectUtils.reflect(currentActivityThread).field("mInstrumentation", new ProxyInstrumentation(rawInstrumentation));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean tryHookActivityThread() {
        Object currentActivityThread = ReflectUtils.reflect("android.app.ActivityThread").field("sCurrentActivityThread").get();
        Handler mH = ReflectUtils.reflect(currentActivityThread).field("mH").get();
        ReflectUtils.reflect(mH).field("mCallback", new ProxyMhHandler(mH));
        return true;
    }
}
