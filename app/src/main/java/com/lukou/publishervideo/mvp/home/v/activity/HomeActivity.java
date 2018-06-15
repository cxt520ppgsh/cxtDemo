package com.lukou.publishervideo.mvp.home.v.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.lukou.publishervideo.R;
import com.lukou.publishervideo.base.BaseActivity;
import com.lukou.publishervideo.bean.PublisherVideo;
import com.lukou.publishervideo.di.component.AppComponent;
import com.lukou.publishervideo.mvp.home.HomeActivityContract;
import com.lukou.publishervideo.mvp.home.adapter.HomeRvAdapter;
import com.lukou.publishervideo.mvp.home.dagger.component.DaggerHomeActivityComponent;
import com.lukou.publishervideo.mvp.home.dagger.module.HomeActivityModule;
import com.lukou.publishervideo.mvp.home.p.HomeActivityPresenter;
import com.lukou.publishervideo.utils.VitamioUtil;
import com.lukou.publishervideo.utils.netUtils.ApiFactory;


import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.VideoView;

public class HomeActivity extends BaseActivity<HomeActivityPresenter> implements HomeActivityContract.View {

    @Inject
    ApiFactory apiFactory;
    @Inject
    HomeRvAdapter homeRvAdapter;
    @BindView(R.id.rv)
    RecyclerView rv;


    @Override
    protected void ComponentInject(AppComponent appComponent) {
        DaggerHomeActivityComponent
                .builder()
                .appComponent(appComponent)
                .homeActivityModule(new HomeActivityModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected View setContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_home, null, false);
    }

    @Override
    public void initView() {
        initRv();
        initVitamio();
    }

    private void initRv(){
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(homeRvAdapter);
    }

    private void initVitamio(){
        Vitamio.initialize(this);
        if (!LibsChecker.checkVitamioLibs(this)) {
            Toast.makeText(this, "加载lib库失败",Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void refresh(List<PublisherVideo> publisherVideos) {
        homeRvAdapter.setPublisherVideoList(publisherVideos);
    }
}
