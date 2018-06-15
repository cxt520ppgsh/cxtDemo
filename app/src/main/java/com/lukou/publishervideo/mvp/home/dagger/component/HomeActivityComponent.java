package com.lukou.publishervideo.mvp.home.dagger.component;

import android.app.Fragment;

import com.lukou.publishervideo.di.component.AppComponent;
import com.lukou.publishervideo.mvp.home.v.HomeActivity;
import com.lukou.publishervideo.mvp.home.dagger.module.HomeActivityModule;
import com.lukou.publishervideo.mvp.home.dagger.scope.HomeActivityScope;

import dagger.Component;

@HomeActivityScope
@Component(modules = HomeActivityModule.class, dependencies = AppComponent.class)
public interface HomeActivityComponent {
    void inject(HomeActivity activity);
    void inject(Fragment fragment);
}
