package com.lukou.publishervideo.di.component;

import android.content.SharedPreferences;

import com.lukou.publishervideo.di.module.NetModule;
import com.lukou.publishervideo.di.scope.PublisherVideoApplicationScope;
import com.lukou.publishervideo.model.net.ApiService;

import dagger.Component;


@PublisherVideoApplicationScope
@Component(modules = NetModule.class, dependencies = com.lukou.base.di.module.AppComponent.class)
public interface PublisherVideoComponent {
    public ApiService provideApiService();

    public SharedPreferences provideSharedPreferences();
}
