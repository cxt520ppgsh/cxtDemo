package com.lukou.publishervideo.mvp.home.p;


import android.content.Context;
import android.widget.Toast;

import com.intersection.listmodule.entity.ResultList;
import com.lukou.publishervideo.app.MainApplication;
import com.lukou.publishervideo.base.BasePresenter;
import com.lukou.publishervideo.bean.PublisherVideo;
import com.lukou.publishervideo.mvp.home.HomeActivityContract;
import com.lukou.publishervideo.utils.netUtils.ApiFactory;
import com.lukou.publishervideo.utils.netUtils.KuaishouHttpResult;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class HomeActivityPresenter extends BasePresenter<HomeActivityContract.View> {
    HomeActivityContract.View rootView;
    @Inject
    ApiFactory apiFactory;

    @Inject
    public HomeActivityPresenter(HomeActivityContract.View rootView) {
        super(rootView);
        this.rootView = rootView;
    }

    @Override
    public void onStart() {
        rootView.initView();
        getVideoList(0);
    }

    void getVideoList(int page) {
        rootView.addSubscription(apiFactory.getPublisherVideo(page,
                "",
                "send_time",
                0,
                -1,
                "",
                "",
                0,
                "")
                .subscribe(result -> rootView.refresh(result.list), throwable -> {
                    Toast.makeText(MainApplication.instance(),"刷新失败",Toast.LENGTH_SHORT).show();
                }))
        ;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
