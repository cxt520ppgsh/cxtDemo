package com.lukou.publishervideo.di.component;

import com.lukou.publishervideo.view.activity.HomeActivity;
import com.lukou.publishervideo.di.module.HomeActivityModule;
import com.lukou.publishervideo.di.scope.HomeActivityScope;

import dagger.Component;

@HomeActivityScope
@Component(modules = HomeActivityModule.class, dependencies = AppComponent.class)
public interface HomeActivityComponent {
    void inject(HomeActivity activity);
}
