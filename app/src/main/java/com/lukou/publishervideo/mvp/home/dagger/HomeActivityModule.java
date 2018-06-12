package com.lukou.publishervideo.mvp.home.dagger;

import com.lukou.publishervideo.di.scop.ActivityScope;
import com.lukou.publishervideo.mvp.home.HomeActivityContract;

import dagger.Module;
import dagger.Provides;

@Module
public class HomeActivityModule {
    private HomeActivityContract.View view;

    public HomeActivityModule(HomeActivityContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    HomeActivityContract.View provideTempLateView() {
        return this.view;
    }

}
