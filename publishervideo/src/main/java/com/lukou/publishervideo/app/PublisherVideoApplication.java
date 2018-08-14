package com.lukou.publishervideo.app;

import com.lukou.base.application.BaseApplication;
import com.lukou.base.arouter.module.appmodule.AppModuleService;
import com.lukou.base.di.module.AppModule;
import com.lukou.publishervideo.di.component.DaggerPublisherVideoComponent;
import com.lukou.publishervideo.di.component.PublisherVideoComponent;
import com.lukou.publishervideo.di.module.NetModule;

public class PublisherVideoApplication {
    private static PublisherVideoApplication instance;
    private static PublisherVideoComponent publisherVideoComponent;


    public void init() {
        initComponnent();
        //Arouter模块调用
        AppModuleService.appStart();
    }

    public static PublisherVideoApplication instance() {
        if (instance == null) {
            instance = new PublisherVideoApplication();
        }
        return instance;
    }


    private void initComponnent() {
        publisherVideoComponent = DaggerPublisherVideoComponent.builder()
                .appComponent(BaseApplication.instance().getAppComponent())
                .netModule(new NetModule())
                .build();
    }

    public PublisherVideoComponent getPublisherVideoComponent() {
        return publisherVideoComponent;
    }

}
