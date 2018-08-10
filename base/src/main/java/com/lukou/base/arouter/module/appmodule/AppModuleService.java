package com.lukou.base.arouter.module.appmodule;

import com.lukou.base.arouter.manager.ModuleManager;
import com.lukou.base.arouter.manager.ServiceManager;
import com.lukou.base.arouter.provider.app.IAppModuleServiceProvider;

/**
 * Created by cxt on 2018/7/18.
 */
public class AppModuleService {
    private static boolean hasAppModule() {
        return ModuleManager.getInstance().hasModule(IAppModuleServiceProvider.APP_MODULE_SERVICE);
    }

    public static void appStart() {
        if (!hasAppModule()) return;
        ServiceManager.getInstance().getAppModuleIntentProvider().appStart();
    }
}
