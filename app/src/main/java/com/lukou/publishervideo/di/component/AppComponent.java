package com.lukou.publishervideo.di.component;

import android.app.Activity;
import android.app.Application;

import com.lukou.publishervideo.di.module.AppModule;
import com.lukou.publishervideo.di.module.NetModule;
import com.lukou.publishervideo.utils.netUtils.ApiService;

import javax.inject.Singleton;

import dagger.Component;
@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface AppComponent {
    void inject(Activity activity);
    ApiService getApiservice();
}
