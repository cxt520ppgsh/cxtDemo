package com.lukou.publishervideo.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.lukou.publishervideo.app.MyApplication;
import com.lukou.publishervideo.di.component.AppComponent;

import javax.inject.Inject;

public abstract class BaseActivity<P extends BasePresenter> extends Activity {
    protected MyApplication myApplication;

    @Inject
    protected P mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initView());

        myApplication = (MyApplication) getApplication();
        ComponentInject(myApplication.getAppComponent());
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
        mPresenter = null;
    }

    protected abstract void ComponentInject(AppComponent appComponent);

    protected abstract View initView();

    protected abstract void initData();

}
