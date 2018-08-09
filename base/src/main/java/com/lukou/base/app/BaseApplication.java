package com.lukou.base.app;

import android.app.Application;

import com.lukou.base.di.module.AppComponent;
import com.lukou.base.di.module.AppModule;
import com.lukou.base.di.module.DaggerAppComponent;

/**
 * Created by cxt on 2018/8/9.
 */

public class BaseApplication extends Application {
    private AppComponent mAppComponent;
    private static BaseApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initComponnent();
    }

    public static BaseApplication instance() {
        return instance;
    }

    private void initComponnent() {
        mAppComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }


}
