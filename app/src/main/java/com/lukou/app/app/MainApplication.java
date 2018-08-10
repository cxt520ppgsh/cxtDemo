package com.lukou.app.app;

import com.lukou.base.application.BaseApplication;
import com.lukou.publishervideo.app.PublisherVideoApplication;

/**
 * Created by cxt on 2018/8/9.
 */

public class MainApplication extends BaseApplication{
    @Override
    public void onCreate() {
        super.onCreate();
        PublisherVideoApplication.instance().init();
    }
}
