package com.lukou.publishervideo.mvp.home.v.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.lukou.publishervideo.R;
import com.lukou.publishervideo.base.BaseActivity;
import com.lukou.publishervideo.bean.PublisherVideo;
import com.lukou.publishervideo.di.component.AppComponent;
import com.lukou.publishervideo.mvp.home.HomeActivityContract;
import com.lukou.publishervideo.mvp.home.adapter.HomeRvAdapter;
import com.lukou.publishervideo.mvp.home.dagger.component.DaggerHomeActivityComponent;
import com.lukou.publishervideo.mvp.home.dagger.module.HomeActivityModule;
import com.lukou.publishervideo.mvp.home.p.HomeActivityPresenter;
import com.lukou.publishervideo.utils.netUtils.ApiFactory;
import com.lukou.publishervideo.widget.VideoRecycleView;


import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

public class HomeActivity extends BaseActivity<HomeActivityPresenter> implements HomeActivityContract.View {

    @Inject
    ApiFactory apiFactory;
    @Inject
    HomeRvAdapter homeRvAdapter;

    @BindView(R.id.rv)
    VideoRecycleView rv;


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
    }

    private void initRv() {
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(homeRvAdapter);
        rv.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(RecyclerView.ViewHolder holder) {
                ((HomeRvAdapter.HomeRvItemViewHolder)holder).destroyVideoView();

            }
        });


        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == SCROLL_STATE_IDLE) {
                    if (rv.getCurrentPosition() < homeRvAdapter.getPublisherVideoList().size()){
                        if (homeRvAdapter.getPublisherVideoList().get(rv.getCurrentPosition()) != null) {
                            View view = rv.getLayoutManager().findViewByPosition((rv.getCurrentPosition()));
                            if (view != null) {
                                HomeRvAdapter.HomeRvItemViewHolder homeRvItemViewHolder = (HomeRvAdapter.HomeRvItemViewHolder) rv.getChildViewHolder(view);
                                homeRvItemViewHolder.setVideoURL(homeRvAdapter.getPublisherVideoList().get(rv.getCurrentPosition()));
                                rv.setCanScrollToNext(true);
                            }
                        }
                    }

                }
            }

        });
    }

    private RecyclerView.ViewHolder getViewHolder(int position) {
        if (rv == null || rv.getAdapter() == null || rv.getAdapter().getItemCount() == 0) {
            return null;
        }
        int count = rv.getAdapter().getItemCount();
        if (position < 0 || position > count - 1) {
            return null;
        }
        RecyclerView.ViewHolder viewHolder = rv.findViewHolderForAdapterPosition(position);
        if (viewHolder == null) {
            RecyclerView.RecycledViewPool pool = rv.getRecycledViewPool();
            int recycleViewcount = pool.getRecycledViewCount(0);
            viewHolder = pool.getRecycledView(0);
            try {
                pool.putRecycledView(viewHolder);
            } catch (Exception e) {

            }
        }
        return viewHolder;

    }

    @Override
    public void refresh(List<PublisherVideo> publisherVideos) {
        homeRvAdapter.setPublisherVideoList(publisherVideos);
    }
}
