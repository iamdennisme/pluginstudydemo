package com.dennisce.pluginstudydemo.hookhelper;

import android.content.pm.ServiceInfo;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ReflectUtils;

public class ProxyMhHandler implements Handler.Callback {

    private Handler base;

    public ProxyMhHandler(Handler base) {
        this.base = base;
    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        if (msg.what==114){
            replaceRealService(msg);
        }
        base.handleMessage(msg);
        return true;
    }

    private void replaceRealService(Message message){
        Object object=message.obj;
        ServiceInfo serviceInfo=ReflectUtils.reflect(object).field("info").get();
        // 这里获取真实的service名字，应该有一个映射表，这里只是验证记载是否可行，所以直接给了serviceName
        serviceInfo.name= "com.dennisce.testplugin.PluginService";
    }
}
