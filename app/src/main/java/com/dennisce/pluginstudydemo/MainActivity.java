package com.dennisce.pluginstudydemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.dennisce.pluginstudydemo.hookhelper.HookHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void testHookActivity(View view) {
        if (!HookHelper.tryHookActivity()) {
            Toast.makeText(this, "hook失败", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(new Intent(this, OtherTestActivity.class));
    }

}
