package com.lukou.publishervideo.app;

import android.app.Application;

import com.lukou.base.app.BaseApplication;
import com.lukou.publishervideo.di.component.DaggerPublisherVideoComponent;
import com.lukou.publishervideo.di.component.PublisherVideoComponent;
import com.lukou.publishervideo.di.module.NetModule;

public class PublisherVideoApplication {
    private static PublisherVideoApplication instance;
    private static PublisherVideoComponent publisherVideoComponent;


    public static void init() {
        initComponnent();
    }

    public static PublisherVideoApplication instance() {
        if (instance == null) {
            instance = new PublisherVideoApplication();
        }
        return instance;
    }


    private static void initComponnent() {
        publisherVideoComponent = DaggerPublisherVideoComponent.builder()
                .appComponent(BaseApplication.instance().getAppComponent())
                .netModule(new NetModule())
                .build();
    }

    public PublisherVideoComponent getPublisherVideoComponent() {
        return publisherVideoComponent;
    }

}
