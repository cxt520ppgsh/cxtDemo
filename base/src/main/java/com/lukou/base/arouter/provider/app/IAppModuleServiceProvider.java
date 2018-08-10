package com.lukou.base.arouter.provider.app;


import android.app.Activity;
import android.content.Context;

import com.lukou.base.arouter.provider.base.IBaseProvider;

/**
 * Created by cxt on 2018/7/18.
 */
public interface IAppModuleServiceProvider extends IBaseProvider {
    String APP_MODULE_SERVICE = "/app/service";

    void appStart();
}
