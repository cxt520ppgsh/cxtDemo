package com.lukou.app.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;

import com.lukou.app.R;
import com.lukou.publishervideo.view.activity.HomeActivity;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        startActivity(new Intent(this, HomeActivity.class));
    }

}
