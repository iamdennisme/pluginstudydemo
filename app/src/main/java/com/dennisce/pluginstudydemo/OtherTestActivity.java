package com.dennisce.pluginstudydemo;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import timber.log.Timber;

public class OtherTestActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d("Activity real");
    }
}
