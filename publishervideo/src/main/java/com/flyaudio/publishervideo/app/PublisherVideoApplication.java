package com.flyaudio.publishervideo.app;

import com.flyaudio.base.application.BaseApplication;
import com.flyaudio.base.arouter.module.appmodule.AppModuleService;
import com.flyaudio.publishervideo.di.component.DaggerPublisherVideoComponent;
import com.flyaudio.publishervideo.di.component.PublisherVideoComponent;
import com.flyaudio.publishervideo.di.module.NetModule;

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
