package com.dennisce.pluginstudydemo.hookhelper;

import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;

import com.dennisce.pluginstudydemo.Constant;
import com.dennisce.pluginstudydemo.StubActivity;

public class ProxyInstrumentation extends Instrumentation {
    private Instrumentation rawInstrumentation;

    public ProxyInstrumentation(Instrumentation rawInstrumentation) {
        this.rawInstrumentation = rawInstrumentation;
    }

    @Override
    public Activity newActivity(ClassLoader cl, String className, Intent intent) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        if (!intent.getComponent().getClassName().equals(StubActivity.class.getName())) {
            return rawInstrumentation.newActivity(cl, className, intent);
        }
        Intent rawIntent = intent.getParcelableExtra(Constant.RAW_INTENT);
        return rawInstrumentation.newActivity(cl, rawIntent.getComponent().getClassName(), rawIntent);
    }

    @Override
    public void callActivityOnCreate(Activity activity, Bundle icicle) {
        super.callActivityOnCreate(activity, icicle);
    }

    @Override
    public Activity newActivity(Class<?> clazz, Context context, IBinder token, Application application, Intent intent, ActivityInfo info, CharSequence title, Activity parent, String id, Object lastNonConfigurationInstance) throws IllegalAccessException, InstantiationException {
        return super.newActivity(clazz, context, token, application, intent, info, title, parent, id, lastNonConfigurationInstance);
    }
}
