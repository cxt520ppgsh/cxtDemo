package com.flyaudio.publishervideo.di.component;

import android.content.SharedPreferences;

import com.flyaudio.publishervideo.di.module.NetModule;
import com.flyaudio.publishervideo.di.scope.PublisherVideoApplicationScope;
import com.flyaudio.publishervideo.model.net.ApiService;

import dagger.Component;


@PublisherVideoApplicationScope
@Component(modules = NetModule.class, dependencies = com.flyaudio.base.di.module.AppComponent.class)
public interface PublisherVideoComponent {
    public ApiService provideApiService();

    public SharedPreferences provideSharedPreferences();
}
