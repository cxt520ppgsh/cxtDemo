package com.flyaudio.app.app;

import com.flyaudio.base.application.BaseApplication;
import com.flyaudio.packagemanager.app.PackageManagerApplication;
import com.flyaudio.publishervideo.app.PublisherVideoApplication;

/**
 * Created by cxt on 2018/8/9.
 */

public class MainApplication extends BaseApplication{
    @Override
    public void onCreate() {
        super.onCreate();
        PublisherVideoApplication.instance().init();
        PackageManagerApplication.instance().init();
    }
}
