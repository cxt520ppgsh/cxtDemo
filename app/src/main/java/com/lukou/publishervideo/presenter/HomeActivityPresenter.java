package com.lukou.publishervideo.presenter;


import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.lukou.publishervideo.app.MainApplication;
import com.lukou.publishervideo.base.BasePresenter;
import com.lukou.publishervideo.model.net.ApiFactory;
import com.lukou.publishervideo.view.activity.HomeActivity;
import com.lukou.publishervideo.utils.SharedPreferencesUtil;

import javax.inject.Inject;

public class HomeActivityPresenter extends HomeActivityContract.Presenter {
    HomeActivityContract.View rootView;
    @Inject
    ApiFactory apiFactory;
    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    public HomeActivityPresenter(HomeActivityContract.View rootView) {
        super(rootView);
        this.rootView = rootView;
    }

    @Override
    public void initAsigner() {
        rootView.initAsigner();
    }

    @Override
    public void onStart() {
        rootView.initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
