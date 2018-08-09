package com.lukou.publishervideo.app;

import com.lukou.base.app.BaseApplication;
import com.lukou.publishervideo.di.component.DaggerPublisherVideoComponent;
import com.lukou.publishervideo.di.component.PublisherVideoComponent;
import com.lukou.publishervideo.di.module.NetModule;

public class PublisherVideoApplication extends BaseApplication {
    private static PublisherVideoApplication instance;
    private PublisherVideoComponent publisherVideoComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initComponnent();
    }

    public static PublisherVideoApplication instance() {
        if (instance == null) {
            throw new RuntimeException("Application is not initialized");
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
