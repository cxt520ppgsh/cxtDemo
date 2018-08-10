package com.lukou.publishervideo.view.activity;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.lukou.base.arouter.module.appmodule.AppModuleService;
import com.lukou.base.bean.EventBusMessage;
import com.lukou.base.ui.BaseActivity;
import com.lukou.base.utils.SharedPreferencesUtil;
import com.lukou.publishervideo.R;

import com.lukou.publishervideo.app.PublisherVideoApplication;
import com.lukou.publishervideo.databinding.ActivityHomeBinding;
import com.lukou.publishervideo.di.component.DaggerHomeActivityComponent;
import com.lukou.publishervideo.model.bean.Asiginer;
import com.lukou.publishervideo.model.net.ApiFactory;
import com.lukou.publishervideo.presenter.HomeActivityContract;
import com.lukou.publishervideo.view.adapter.HomeRvAdapter;
import com.lukou.publishervideo.di.module.HomeActivityModule;
import com.lukou.publishervideo.presenter.HomeActivityPresenter;

import com.lukou.publishervideo.view.dialog.SetAsignerDialog;
import com.shuyu.gsyvideoplayer.GSYVideoManager;


import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import rx.Subscription;

public class HomeActivity extends BaseActivity<HomeActivityPresenter> implements HomeActivityContract.View {
    public static final String HOMEACTIVITY_INIT_ASIGNER_TV = "HOMEACTIVITY_INIT_ASIGNER_TV";
    public static final String HOMEACTIVITY_ADD_SUBSCRIBTION = "HOMEACTIVITY_ADD_SUBSCRIBTION";
    @Inject
    public ApiFactory apiFactory;
    @Inject
    public SharedPreferences sharedPreferences;
    @Inject
    HomeRvAdapter homeRvAdapter;
    ActivityHomeBinding binding;

    @Override
    protected void ComponentInject() {
        DaggerHomeActivityComponent
                .builder()
                .publisherVideoComponent(PublisherVideoApplication.instance().getPublisherVideoComponent())
                .homeActivityModule(new HomeActivityModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected View setContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_home, null, false);
    }

    @Override
    protected void onBindActivityView(View view) {
        super.onBindActivityView(view);
        binding = DataBindingUtil.bind(view);
    }

    @Override
    public void initView() {
        initAsignerTv();
        initRv();
    }

    public void initAsignerTv() {
        String asignerName = sharedPreferences.getString(SharedPreferencesUtil.SP_ASIGNER_NAME, "");
        int asignerCount = sharedPreferences.getInt(SharedPreferencesUtil.SP_ASIGNER_COUNT, 0);
        binding.asigner.setText(asignerName == "" ? "请设置审核人" : asignerName + ":" + asignerCount);
        binding.asigner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SetAsignerDialog.Builder(HomeActivity.this).setSetAsignerDialog(new SetAsignerDialog.SetAsignerFinishListener() {
                    @Override
                    public void onSetAsignerSuccess(String name, int count) {
                        binding.asigner.setText(name + ":" + count);
                        sharedPreferences.edit().putString(SharedPreferencesUtil.SP_ASIGNER_NAME, name).commit();
                        sharedPreferences.edit().putInt(SharedPreferencesUtil.SP_ASIGNER_COUNT, count).commit();
                        homeRvAdapter.refresh();
                    }

                    @Override
                    public void onSetAsignerTagFaild() {

                    }
                }).show();
            }
        });
        //更新剩余审核数
        addSubscription(ApiFactory.getInstance().getAsigner()
                .subscribe(list -> {
                    for (Asiginer asiginer : list) {
                        if (asiginer.getAsignerName().equals(asignerName)) {
                            sharedPreferences.edit().putInt(SharedPreferencesUtil.SP_ASIGNER_COUNT, asiginer.getAsignCnt()).commit();
                            binding.asigner.setText(asiginer.getAsignerName() + ":" + asiginer.getAsignCnt());
                        }
                    }
                }, throwable -> {

                }));
    }

    private void initRv() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.rv.setLayoutManager(linearLayoutManager);
        homeRvAdapter.setRecyclerView(binding.rv);
        homeRvAdapter.setSwipeRefreshLayout(binding.swipeRefreshLayout, binding.rv);
        binding.rv.setAdapter(homeRvAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        GSYVideoManager.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        GSYVideoManager.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onMessage(EventBusMessage event) {
        switch (event.getCode()) {
            case HOMEACTIVITY_INIT_ASIGNER_TV:
                initAsignerTv();
                break;
            case HOMEACTIVITY_ADD_SUBSCRIBTION:
                if (event.getMsg() instanceof Subscription) {
                    addSubscription((Subscription) event.getMsg());
                }
                break;
            default:
                break;
        }
    }
}
