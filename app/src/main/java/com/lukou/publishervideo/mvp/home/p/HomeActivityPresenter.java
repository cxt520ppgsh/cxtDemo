package com.lukou.publishervideo.mvp.home.p;


import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.intersection.listmodule.entity.ResultList;
import com.lukou.publishervideo.app.MainApplication;
import com.lukou.publishervideo.base.BasePresenter;
import com.lukou.publishervideo.bean.Asiginer;
import com.lukou.publishervideo.bean.PublisherVideo;
import com.lukou.publishervideo.mvp.home.HomeActivityContract;
import com.lukou.publishervideo.mvp.home.v.activity.HomeActivity;
import com.lukou.publishervideo.utils.SharedPreferencesUtil;
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
    SharedPreferences sharedPreferences;

    @Inject
    public HomeActivityPresenter(HomeActivityContract.View rootView) {
        super(rootView);
        this.rootView = rootView;
    }

    @Override
    public void onStart() {
        rootView.initView();
        getVideoList();
    }

    public void getVideoList() {
        if (sharedPreferences.getString(SharedPreferencesUtil.SP_ASIGNER_NAME, "").equals("")){
            Toast.makeText((Context) rootView,"请先选择审核人",Toast.LENGTH_SHORT).show();
            return;
        }

        rootView.addSubscription(apiFactory.getPublisherVideo(1, 0, sharedPreferences.getString(SharedPreferencesUtil.SP_ASIGNER_NAME, "")
        )
                .subscribe(result -> rootView.refresh(HomeActivity.INIT_PUBLISH_VIDEO_LIST, result.list), throwable -> {
                    Toast.makeText(MainApplication.instance(), "刷新失败", Toast.LENGTH_SHORT).show();
                }))
        ;
    }

    public void addVideoList() {
        rootView.addSubscription(apiFactory.getPublisherVideo(1, 0, sharedPreferences.getString(SharedPreferencesUtil.SP_ASIGNER_NAME, "")
        )
                .subscribe(result -> rootView.refresh(HomeActivity.ADD_PUBLISH_VIDEO_LIST, result.list), throwable -> {
                    Toast.makeText(MainApplication.instance(), "刷新失败", Toast.LENGTH_SHORT).show();
                }))
        ;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
