package com.lukou.base.di.module;

import android.app.Activity;
import android.content.SharedPreferences;

import com.lukou.base.di.module.AppModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    public SharedPreferences provideSharedPreferences();
}
