package com.dennisce.pluginstudydemo.hookhelper;

import android.content.ComponentName;
import android.content.Intent;

import com.blankj.utilcode.util.Utils;
import com.dennisce.pluginstudydemo.base.Constant;
import com.dennisce.pluginstudydemo.stub.StubActivity;
import com.dennisce.pluginstudydemo.stub.StubService;
import com.dennisce.pluginstudydemo.util.ComponentUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import timber.log.Timber;

public class IActivityManagerInvocationHandler implements InvocationHandler {

    private Object base;

    public IActivityManagerInvocationHandler(Object base) {
        this.base = base;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("startActivity")) {
            return hookStartActivity(method, args);
        }
        if (method.getName().equals("startService")||method.getName().equals("stopService")) {
            return hookService(method, args);
        }
        return method.invoke(base, args);
    }

    private Object hookService(Method method, Object[] args)throws IllegalAccessException, InvocationTargetException{
        Intent rawIntent= (Intent) args[1];
        List<Class> services = ComponentUtils.getServiceClass(Utils.getApp(), Utils.getApp().getPackageName());
        for (Class clz :services){
            if (clz.getName().equals(rawIntent.getComponent().getClassName())){
                return method.invoke(base, args);
            }
        }
        Intent newIntent = new Intent();
        newIntent.putExtra(Constant.RAW_INTENT, (Intent) args[1]);
        newIntent.setComponent(new ComponentName(Utils.getApp().getPackageName(), StubService.class.getName()));
        args[1] = newIntent;
        return method.invoke(base, args);
    }

    private Object hookStartActivity(Method method, Object[] args) throws IllegalAccessException, InvocationTargetException {
        Timber.d("invoke start activity");
        List<Class> activitys = ComponentUtils.getActivitiesClass(Utils.getApp(), Utils.getApp().getPackageName());
        Intent rawIntent = (Intent) args[2];
        for (Class clz : activitys) {
            if (clz.getName().equals(rawIntent.getComponent().getClassName())) {// 在manifest注册的activity
                return method.invoke(base, args);
            }
        }
        Intent newIntent = new Intent();
        newIntent.putExtra(Constant.RAW_INTENT, (Intent) args[2]);
        newIntent.setComponent(new ComponentName(Utils.getApp().getPackageName(), StubActivity.class.getName()));
        args[2] = newIntent;
        return method.invoke(base, args);
    }
}
