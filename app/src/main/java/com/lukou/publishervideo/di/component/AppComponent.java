package com.lukou.publishervideo.di.component;

import android.app.Activity;
import android.content.SharedPreferences;

import com.lukou.publishervideo.di.module.AppModule;
import com.lukou.publishervideo.di.module.NetModule;
import com.lukou.publishervideo.model.net.ApiService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface AppComponent {
    void inject(Activity activity);

    ApiService provideApiService();

    SharedPreferences provideSharedPreferences();
}
