package com.dennisce.pluginstudydemo.base;

import com.blankj.utilcode.util.Utils;

import java.io.File;

public class Constant {
    public static final String RAW_INTENT = "RAW_INTENT";
    public static final String ASSETS_PLUGIN_PATH = "plugin/app-debug.apk";
    public static final String PLUGIN_APK_NAME = "plugin.apk";
    public static final String PLUGIN_PATH = new File(Utils.getApp().getFilesDir(), "plugin").getPath();
}
