package com.lukou.app.provider;

import android.content.Context;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lukou.base.arouter.provider.app.IAppModuleServiceProvider;

/**
 * Created by cxt on 2018/7/18.
 */
@Route(path = IAppModuleServiceProvider.APP_MODULE_SERVICE)
public class IAppServiceProvider implements IAppModuleServiceProvider {

    @Override
    public void init(Context context) {

    }

    @Override
    public void appStart() {
        Log.d("APPMODULE","app_start");
    }

}
