package com.flyaudio.packagemanager.app;

import com.flyaudio.base.application.BaseApplication;
import com.flyaudio.base.arouter.module.appmodule.AppModuleService;
import com.flyaudio.packagemanager.di.component.DaggerPackageManagerComponent;
import com.flyaudio.packagemanager.di.component.PackageManagerComponent;
import com.flyaudio.packagemanager.di.module.NetModule;

public class PackageManagerApplication {
    private static PackageManagerApplication instance;
    private static PackageManagerComponent packageManagerComponent;


    public void init() {
        initComponnent();
        //Arouter模块调用
        AppModuleService.appStart();
    }

    public static PackageManagerApplication instance() {
        if (instance == null) {
            instance = new PackageManagerApplication();
        }
        return instance;
    }


    private void initComponnent() {
        packageManagerComponent = DaggerPackageManagerComponent.builder()
                .appComponent(BaseApplication.instance().getAppComponent())
                .netModule(new NetModule())
                .build();
    }

    public PackageManagerComponent getPackageComponent() {
        return packageManagerComponent;
    }

}
