package com.flyaudio.packagemanager.di.module;

import android.content.Context;

import com.flyaudio.packagemanager.di.scope.ActivityScope;
import com.flyaudio.packagemanager.presenter.home.HomeActivityContract;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {
    private HomeActivityContract.View view;

    public ActivityModule(HomeActivityContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    HomeActivityContract.View provideView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    Context provideContext() {
        return (Context) this.view;
    }

}
