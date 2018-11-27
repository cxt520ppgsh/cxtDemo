package com.flyaudio.publishervideo.di.module;

import android.content.Context;

import com.flyaudio.publishervideo.presenter.HomeActivityContract;
import com.flyaudio.publishervideo.di.scope.HomeActivityScope;

import dagger.Module;
import dagger.Provides;

@Module
public class HomeActivityModule {
    private HomeActivityContract.View view;

    public HomeActivityModule(HomeActivityContract.View view) {
        this.view = view;
    }

    @HomeActivityScope
    @Provides
    HomeActivityContract.View provideView() {
        return this.view;
    }

    @HomeActivityScope
    @Provides
    Context provideContext() {
        return (Context) this.view;
    }

}
