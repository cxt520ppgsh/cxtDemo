package com.flyaudio.base.application;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.flyaudio.base.arouter.config.ModuleOptions;
import com.flyaudio.base.arouter.manager.ModuleManager;
import com.flyaudio.base.arouter.provider.app.IAppModuleServiceProvider;
import com.flyaudio.base.di.module.AppComponent;
import com.flyaudio.base.di.module.AppModule;
import com.flyaudio.base.di.module.DaggerAppComponent;

import static com.flyaudio.base.BuildConfig.DEBUG;

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
        initArouter();
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

    protected void initArouter() {
        if (DEBUG) {
            ARouter.openLog();
            ARouter.openDebug();
            ARouter.printStackTrace();
        }
        ARouter.init(this);
        ModuleOptions.ModuleBuilder builder = new ModuleOptions.ModuleBuilder(this)
                .addModule(IAppModuleServiceProvider.APP_MODULE_SERVICE, IAppModuleServiceProvider.APP_MODULE_SERVICE);
        ModuleManager.getInstance().init(builder.build());
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

}
