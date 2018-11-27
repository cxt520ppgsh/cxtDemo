package com.flyaudio.base.arouter.provider.app;


import com.flyaudio.base.arouter.provider.base.IBaseProvider;

/**
 * Created by cxt on 2018/7/18.
 */
public interface IAppModuleServiceProvider extends IBaseProvider {
    String APP_MODULE_SERVICE = "/app/service";

    void appStart();
}
