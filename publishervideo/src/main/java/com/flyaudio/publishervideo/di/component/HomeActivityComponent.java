package com.flyaudio.publishervideo.di.component;

import com.flyaudio.publishervideo.view.activity.HomeActivity;
import com.flyaudio.publishervideo.di.module.HomeActivityModule;
import com.flyaudio.publishervideo.di.scope.HomeActivityScope;

import dagger.Component;

@HomeActivityScope
@Component(modules = HomeActivityModule.class, dependencies = PublisherVideoComponent.class)
public interface HomeActivityComponent {
    void inject(HomeActivity activity);
}
