package com.lukou.publishervideo.app;

import android.app.Application;

import com.lukou.publishervideo.di.component.AppComponent;
import com.lukou.publishervideo.di.component.DaggerAppComponent;
import com.lukou.publishervideo.di.module.AppModule;
import com.lukou.publishervideo.di.module.NetModule;

public class MyApplication extends Application {
    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule("https://quan.lukou.com/api/"))
                .build();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}
