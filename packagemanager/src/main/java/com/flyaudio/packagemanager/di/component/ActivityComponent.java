package com.flyaudio.packagemanager.di.component;


import com.flyaudio.packagemanager.di.module.ActivityModule;
import com.flyaudio.packagemanager.di.scope.ActivityScope;
import com.flyaudio.packagemanager.view.PackageHomeActivity;

import dagger.Component;

@ActivityScope
@Component(modules = ActivityModule.class, dependencies = PackageManagerComponent.class)
public interface ActivityComponent {
    void inject(PackageHomeActivity activity);
}
