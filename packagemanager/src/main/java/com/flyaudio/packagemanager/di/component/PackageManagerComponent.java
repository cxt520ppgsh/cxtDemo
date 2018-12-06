package com.flyaudio.packagemanager.di.component;

import android.content.SharedPreferences;

import com.flyaudio.packagemanager.di.module.NetModule;
import com.flyaudio.packagemanager.di.scope.PackageManagerApplicationScope;
import com.flyaudio.packagemanager.model.net.ApiService;

import dagger.Component;


@PackageManagerApplicationScope
@Component(modules = NetModule.class, dependencies = com.flyaudio.base.di.module.AppComponent.class)
public interface PackageManagerComponent {
    public ApiService provideApiService();

    public SharedPreferences provideSharedPreferences();
}
