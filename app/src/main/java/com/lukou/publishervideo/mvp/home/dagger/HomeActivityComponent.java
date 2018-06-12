package com.lukou.publishervideo.mvp.home.dagger;

import com.lukou.publishervideo.di.component.AppComponent;
import com.lukou.publishervideo.di.scop.ActivityScope;
import com.lukou.publishervideo.mvp.home.HomeActivity;

import dagger.Component;

@ActivityScope
@Component(modules = HomeActivityModule.class, dependencies = AppComponent.class)
public interface HomeActivityComponent {
    void inject(HomeActivity activity);

}
