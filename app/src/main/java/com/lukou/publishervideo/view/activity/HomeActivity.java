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


import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

public class HomeActivity extends BaseActivity<HomeActivityPresenter> implements HomeActivityContract.View {
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

    @Override
    public void initAsigner() {
        initAsignerTv();
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
        homeRvAdapter.setLoadFinishEndListener(new BaseRecycleViewAdapter.LoadFinishEndListener() {
            @Override
            public void onRefreshFinish() {
                rv.scrollToHead();
            }

            @Override
            public void onLoadNextFinish() {
                rv.scrollToNext();
            }
        });
        rv.setAdapter(homeRvAdapter);
        rv.setRecyclerListener(holder -> {
            if (holder instanceof HomeRvAdapter.HomeRvItemViewHolder) {
                ((HomeRvAdapter.HomeRvItemViewHolder) holder).destroyVideoView();
            }
        });
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == SCROLL_STATE_IDLE) {
                    //只有当前Item播放视频
                    if (rv.getCurrentPosition() < homeRvAdapter.getList().size()) {
                        if (homeRvAdapter.getList().get(rv.getCurrentPosition()) != null) {
                            View view = rv.getLayoutManager().findViewByPosition((rv.getCurrentPosition()));
                            if (view != null) {
                                if (rv.getChildViewHolder(view) instanceof HomeRvAdapter.HomeRvItemViewHolder) {
                                    HomeRvAdapter.HomeRvItemViewHolder homeRvItemViewHolder = (HomeRvAdapter.HomeRvItemViewHolder) rv.getChildViewHolder(view);
                                    homeRvItemViewHolder.setVideoURL(homeRvAdapter.getList().get(rv.getCurrentPosition()));
                                }
                            }
                        }
                    }

                } else if (newState == SCROLL_STATE_DRAGGING) {
                    GSYVideoManager.onPause();
                }
            }

        });
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

}
