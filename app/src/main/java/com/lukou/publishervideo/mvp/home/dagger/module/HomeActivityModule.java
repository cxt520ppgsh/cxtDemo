package com.lukou.publishervideo.mvp.home.dagger.module;

import android.content.Context;

import com.lukou.publishervideo.mvp.home.HomeActivityContract;
import com.lukou.publishervideo.mvp.home.dagger.scope.HomeActivityScope;
import com.lukou.publishervideo.mvp.home.v.dialog.CommodityDialog;

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
