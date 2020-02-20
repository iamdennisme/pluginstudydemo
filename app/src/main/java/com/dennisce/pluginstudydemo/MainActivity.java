package com.dennisce.pluginstudydemo;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.dennisce.pluginstudydemo.hookhelper.HookHelper;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void testHookActivity(View view) {
        if (!HookHelper.tryHookStartActivity()) {
            Toast.makeText(this, "hook失败", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!HookHelper.tryHookInstrumentation()) {
            Toast.makeText(this, "hook失败", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(new Intent(this, NotInManifestActivity.class));
    }

}
