package com.flyaudio.base.di.module;

import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    public SharedPreferences provideSharedPreferences();
}
