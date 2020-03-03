package com.dennisce.pluginstudydemo.stub;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.dennisce.pluginstudydemo.PluginManager;
import com.dennisce.pluginstudydemo.R;

public class StubActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stub);
    }
}
