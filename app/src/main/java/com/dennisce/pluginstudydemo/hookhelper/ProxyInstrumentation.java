package com.dennisce.pluginstudydemo.hookhelper;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;

import com.dennisce.pluginstudydemo.base.Constant;
import com.dennisce.pluginstudydemo.StubActivity;
import com.dennisce.pluginstudydemo.loader.ApkLoadManager;

public class ProxyInstrumentation extends Instrumentation {
    private Instrumentation rawInstrumentation;

    ProxyInstrumentation(Instrumentation rawInstrumentation) {
        this.rawInstrumentation = rawInstrumentation;
    }

    @Override
    public Activity newActivity(ClassLoader cl, String className, Intent intent) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        if (!intent.getComponent().getClassName().equals(StubActivity.class.getName())) {
            return rawInstrumentation.newActivity(cl, className, intent);
        }
        Intent rawIntent = intent.getParcelableExtra(Constant.RAW_INTENT);
        return rawInstrumentation.newActivity(ApkLoadManager.getSingleton().getPluginClassLoader(), rawIntent.getComponent().getClassName(), rawIntent);
    }
}
