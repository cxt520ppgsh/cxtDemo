package com.lukou.publishervideo.view.activity;

import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.lukou.publishervideo.R;
import com.lukou.publishervideo.base.BaseActivity;
import com.lukou.publishervideo.base.BaseRecycleViewAdapter;
import com.lukou.publishervideo.di.component.DaggerHomeActivityComponent;
import com.lukou.publishervideo.model.bean.Asiginer;
import com.lukou.publishervideo.model.bean.EventBusMessage;
import com.lukou.publishervideo.model.bean.PublisherVideo;
import com.lukou.publishervideo.di.component.AppComponent;
import com.lukou.publishervideo.model.net.ApiFactory;
import com.lukou.publishervideo.presenter.HomeActivityContract;
import com.lukou.publishervideo.view.adapter.HomeRvAdapter;
import com.lukou.publishervideo.di.module.HomeActivityModule;
import com.lukou.publishervideo.presenter.HomeActivityPresenter;

import com.lukou.publishervideo.utils.SharedPreferencesUtil;
import com.lukou.publishervideo.view.dialog.SetAsignerDialog;
import com.lukou.publishervideo.view.widget.TextImageView;
import com.lukou.publishervideo.view.widget.VideoRecycleView;
import com.shuyu.gsyvideoplayer.GSYVideoManager;


import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscription;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

public class HomeActivity extends BaseActivity<HomeActivityPresenter> implements HomeActivityContract.View {
    public static final String HOMEACTIVITY_INIT_ASIGNER_TV = "HOMEACTIVITY_INIT_ASIGNER_TV";
    public static final String HOMEACTIVITY_ADD_SUBSCRIBTION = "HOMEACTIVITY_ADD_SUBSCRIBTION";
    @Inject
    public ApiFactory apiFactory;
    @Inject
    public SharedPreferences sharedPreferences;
    @Inject
    HomeRvAdapter homeRvAdapter;

    @BindView(R.id.rv)
    public VideoRecycleView rv;
    @BindView(R.id.asigner)
    public TextImageView asignerTv;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

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
        initAsignerTv();
        initRv();
    }

    public void initAsignerTv() {
        String asignerName = sharedPreferences.getString(SharedPreferencesUtil.SP_ASIGNER_NAME, "");
        int asignerCount = sharedPreferences.getInt(SharedPreferencesUtil.SP_ASIGNER_COUNT, 0);
        asignerTv.setText(asignerName == "" ? "请设置审核人" : asignerName + ":" + asignerCount);

        //更新剩余审核数
        addSubscription(ApiFactory.getInstance().getAsigner()
                .subscribe(list -> {
                    for (Asiginer asiginer : list) {
                        if (asiginer.getAsignerName().equals(asignerName)) {
                            sharedPreferences.edit().putInt(SharedPreferencesUtil.SP_ASIGNER_COUNT, asiginer.getAsignCnt()).commit();
                            asignerTv.setText(asiginer.getAsignerName() + ":" + asiginer.getAsignCnt());
                        }
                    }
                }, throwable -> {

                }));
    }

    private void initRv() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(linearLayoutManager);
        homeRvAdapter.setRecyclerView(rv);
        homeRvAdapter.setSwipeRefreshLayout(swipeRefreshLayout, rv);
        rv.setAdapter(homeRvAdapter);
    }

    @OnClick(R.id.asigner)
    void setAsigner() {
        new SetAsignerDialog.Builder(this).setSetAsignerDialog(new SetAsignerDialog.SetAsignerFinishListener() {
            @Override
            public void onSetAsignerSuccess(String name, int count) {
                asignerTv.setText(name + ":" + count);
                sharedPreferences.edit().putString(SharedPreferencesUtil.SP_ASIGNER_NAME, name).commit();
                sharedPreferences.edit().putInt(SharedPreferencesUtil.SP_ASIGNER_COUNT, count).commit();
                homeRvAdapter.refresh();
            }

            @Override
            public void onSetAsignerTagFaild() {

            }
        }).show();
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
