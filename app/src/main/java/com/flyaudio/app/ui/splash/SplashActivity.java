package com.flyaudio.app.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;

import com.flyaudio.app.R;
import com.flyaudio.packagemanager.view.PackageHomeActivity;
import com.flyaudio.publishervideo.view.activity.HomeActivity;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        startActivity(new Intent(this, PackageHomeActivity.class));
        finish();
    }
}
