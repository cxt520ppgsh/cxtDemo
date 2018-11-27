package com.flyaudio.base.arouter.manager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.flyaudio.base.arouter.provider.app.IAppModuleServiceProvider;


/**
 * Created by cxt on 2018/7/18.
 */
public class ServiceManager {
    //自动注入
    @Autowired
    IAppModuleServiceProvider appModuleIntentProvider;


    public ServiceManager() {
        ARouter.getInstance().inject(this);

    }

    private static final class ServiceManagerHolder {
        private static final ServiceManager instance = new ServiceManager();
    }

    public static ServiceManager getInstance() {
        return ServiceManagerHolder.instance;
    }

    public IAppModuleServiceProvider getAppModuleIntentProvider() {
        return appModuleIntentProvider;
    }

}
