package com.dennisce.pluginstudydemo.hookhelper;

import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;


import com.dennisce.pluginstudydemo.Constant;
import com.dennisce.pluginstudydemo.StubActivity;
import com.dennisce.pluginstudydemo.util.ReflectUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


import timber.log.Timber;

public class HookHelper {
    public static boolean tryHookStartActivity() {
        try {
            Object object = ReflectUtil.getField("android.app.ActivityManagerNative", null, "gDefault");
            final Object iActivityManager = ReflectUtil.getField("android.util.Singleton", object, "mInstance");
            Class<?> iActivityManagerInterface = Class.forName("android.app.IActivityManager");
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
            ReflectUtil.setField("android.util.Singleton", object, "mInstance", proxy);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean tryHookInstrumentation() {
        try {
            Object currentActivityThread = ReflectUtil.getField("android.app.ActivityThread", null, "sCurrentActivityThread");
            Instrumentation rawInstrumentation = (Instrumentation) ReflectUtil.getField("android.app.ActivityThread", currentActivityThread, "mInstrumentation");
            ReflectUtil.setField("android.app.ActivityThread", currentActivityThread, "mInstrumentation", new ProxyInstrumentation(rawInstrumentation));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
