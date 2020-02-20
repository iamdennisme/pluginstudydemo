package com.dennisce.pluginstudydemo.hookhelper;
import com.dennisce.pluginstudydemo.util.ReflectUtil;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import timber.log.Timber;

public class HookHelper {
    public static boolean tryHookActivity(){
        try {
            Object object= ReflectUtil.getField("android.app.ActivityManagerNative",null,"gDefault");
             final Object iActivityManager=ReflectUtil.getField("android.util.Singleton",object,"mInstance");
             Class<?> iActivityManagerInterface=Class.forName("android.app.IActivityManager");
             Object proxy= Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                     new Class[]{iActivityManagerInterface},
                     new InvocationHandler() {
                 @Override
                 public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                     if (method.getName().equals("startActivity")) {
                         Timber.d("invoke start activity");
                         Object o= method.invoke(iActivityManager, args);
                         return o;
                     }
                     return method.invoke(iActivityManager,args);
                 }
             });
            ReflectUtil.setField("android.util.Singleton",object,"mInstance",proxy);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
            return false;
    }
}
