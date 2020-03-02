package com.dennisce.pluginstudydemo.hookhelper;

import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;


import com.blankj.utilcode.util.ReflectUtils;
import com.dennisce.pluginstudydemo.base.Constant;
import com.dennisce.pluginstudydemo.StubActivity;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


import timber.log.Timber;

public class HookHelper {
    public static boolean tryHookStartActivity() {
        try {
            Object singleton;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                singleton =  ReflectUtils.reflect("android.app.ActivityManager").field("IActivityManagerSingleton").get();
            }else {
                singleton =  ReflectUtils.reflect("android.app.ActivityManagerNative").field("gDefault").get();
            }
            final Object iActivityManager =ReflectUtils.reflect(singleton).field("mInstance").get();
            Class<?> iActivityManagerInterface = ReflectUtils.reflect("android.app.IActivityManager").get();
            Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                    new Class[]{iActivityManagerInterface},
                    new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            if (method.getName().equals("startActivity")) {
                                Timber.d("invoke start activity");
                            }
                            int index = 0;
                            for (int i = 0; i < args.length; i++) {
                                if (args[i] instanceof Intent) {
                                    index = i;
                                }
                            }
                            if (!(args[index] instanceof Intent)) {
                                return method.invoke(iActivityManager, args);
                            }
                            Intent newIntent = new Intent();
                            newIntent.putExtra(Constant.RAW_INTENT, (Intent) args[index]);
                            newIntent.setComponent(new ComponentName(StubActivity.class.getPackage().getName(), StubActivity.class.getName()));
                            args[index] = newIntent;
                            return method.invoke(iActivityManager, args);
                        }
                    });
            ReflectUtils.reflect(singleton).field("mInstance",proxy);
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
            ReflectUtils.reflect(currentActivityThread).field("mInstrumentation",new ProxyInstrumentation(rawInstrumentation));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
