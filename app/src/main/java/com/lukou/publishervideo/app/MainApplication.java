package com.lukou.publishervideo.app;

import android.app.Application;

import com.lukou.publishervideo.di.component.AppComponent;
import com.lukou.publishervideo.di.component.DaggerAppComponent;
import com.lukou.publishervideo.di.module.AppModule;
import com.lukou.publishervideo.di.module.NetModule;
import com.lukou.publishervideo.utils.ThreadPoolUtil;

public class MainApplication extends Application {
    private AppComponent mAppComponent;
    private static MainApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mAppComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule())
                .build();
        ThreadPoolUtil.initThreadPool();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    public static MainApplication instance() {
        if (instance == null) {
            throw new RuntimeException("Application is not initialized");
        }
        return instance;
    }

}
