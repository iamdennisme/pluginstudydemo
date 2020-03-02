package com.dennisce.pluginstudydemo;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class StubActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stub);
        PluginManager.initPlugin(this);
    }
}
