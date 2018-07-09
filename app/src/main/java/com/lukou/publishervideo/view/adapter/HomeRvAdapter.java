package com.lukou.publishervideo.view.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lukou.publishervideo.R;
import com.lukou.publishervideo.base.BaseRecycleViewAdapter;
import com.lukou.publishervideo.model.bean.PublisherVideo;
import com.lukou.publishervideo.model.net.ApiFactory;
import com.lukou.publishervideo.presenter.HomeActivityPresenter;
import com.lukou.publishervideo.utils.SharedPreferencesUtil;
import com.lukou.publishervideo.view.dialog.CommodityDialog;
import com.lukou.publishervideo.view.dialog.SetTagDialog;
import com.lukou.publishervideo.utils.LKUtil;
import com.lukou.publishervideo.utils.ScreenUtil;
import com.lukou.publishervideo.view.widget.MyVideoView;
import com.lukou.publishervideo.view.widget.MyVideoViewController;
import com.lukou.publishervideo.view.widget.VideoRecycleView;


import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by cxt on 2018/6/14.
 */

public class HomeRvAdapter extends BaseRecycleViewAdapter<PublisherVideo> {
    private Context mContext;
    private ApiFactory mApiFactory;
    private SharedPreferences mSharedPreferences;
    private VideoRecycleView recyclerView;
    private HomeActivityPresenter mPresenter;

    @Inject
    HomeRvAdapter(Context context, ApiFactory apiFactory, SharedPreferences sharedPreferences, HomeActivityPresenter presenter) {
        super(context);
        mContext = context;
        mSharedPreferences = sharedPreferences;
        mApiFactory = apiFactory;
        mPresenter = presenter;
        refresh();
    }

    public void setRecyclerView(VideoRecycleView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Override
    public RecyclerView.ViewHolder createItemViewholder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.acitivity_home_rv_item, parent, false);
        HomeRvItemViewHolder holder = new HomeRvItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HomeRvItemViewHolder) {
            ((HomeRvItemViewHolder) holder).setVideoView(getList().get(position), position == getList().size() - 1, position);
            //防止第一个视频不刷新
            if (position == 0 && VideoRecycleView.getCurrentPosition() == 0) {
                ((HomeRvItemViewHolder) holder).setVideoURL(getList().get(0));
            }
        }
    }

    @Override
    public void refresh() {
        if (mSharedPreferences.getString(SharedPreferencesUtil.SP_ASIGNER_NAME, "").equals("")) {
            Toast.makeText(mContext, "请先选择审核人", Toast.LENGTH_SHORT).show();
            return;
        }
        mPresenter.addSubscription(mApiFactory.getPublisherVideo(1, 0, mSharedPreferences.getString(SharedPreferencesUtil.SP_ASIGNER_NAME, "")
        ).subscribe(list -> setList(list), throwable -> {

        }));

    }

    @Override
    public void loadMore() {
        mPresenter.addSubscription(mApiFactory.getPublisherVideo(1, 0, mSharedPreferences.getString(SharedPreferencesUtil.SP_ASIGNER_NAME, "")
        ).subscribe(list -> addList(list), throwable -> {

        }));
    }


    public class HomeRvItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.videoView)
        MyVideoViewController videoView;
        @BindView(R.id.isAds_bt)
        Button isAds_bt;
        @BindView(R.id.notAds_bt)
        Button notAds_bt;
        @BindView(R.id.isAds)
        LinearLayout isAdsLl;
        @BindView(R.id.notAds)
        LinearLayout notAdsLl;
        PublisherVideo publisherVideo;
        boolean isEnd = false;
        public View view;


        HomeRvItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
            this.setIsRecyclable(false);
        }

        private void setVideoView(PublisherVideo video, boolean isEnd, int position) {
            this.publisherVideo = video;
            this.isEnd = isEnd;
            setTagButton();
            videoView.initVideoPlayer(position);
            setvideoTag();
        }

        private void setvideoTag() {
            if (publisherVideo.getType() == 2) {
                notAds_bt.setSelected(true);
                isAds_bt.setSelected(false);
            } else if (publisherVideo.getType() != 2 && publisherVideo.getType() != 0) {
                notAds_bt.setSelected(false);
                isAds_bt.setSelected(true);
            } else {
                notAds_bt.setSelected(false);
                isAds_bt.setSelected(false);
            }
        }

        public void setVideoURL(PublisherVideo video) {
            if (videoView != null) {
                videoView.play(video.getVideoUrl());
                recyclerView.setCanScrollToNext(publisherVideo.getType() == 0 ? false : true);
            }
        }

        public void destroyVideoView() {
            videoView.destroy();
        }

        @OnClick(R.id.isAds_bt)
        void isAds_bt_Click() {

            new SetTagDialog.Builder(mContext).setPublisherVideo(publisherVideo).setSetTagFinishListener(new SetTagDialog.SetTagFinishListener() {
                @Override
                public void onSetTagSuccess() {
                    mPresenter.initAsigner();
                    recyclerView.setCanScrollToNext(true);
                    recyclerView.scrollToNext();
                }

                @Override
                public void onSetTagFaild() {

                }
            }).show();
        }

        @OnClick(R.id.notAds_bt)
        void notAds_bt_Click() {
            mPresenter.addSubscription(mApiFactory.setTag(publisherVideo.getFid(), 2).subscribe(pandaHackerHttpResult -> {
                publisherVideo.setType(2);
                recyclerView.setCanScrollToNext(true);
                if (!isEnd) {
                    recyclerView.scrollToNext();
                } else {
                    //滚动到footer
                    recyclerView.smoothScrollToPosition(getList().size());
                }
                mPresenter.initAsigner();
            }, throwable -> {


            }));
        }

        @OnClick(R.id.commondity)
        void commondity() {
            new CommodityDialog.Builder(mContext).setPublisherVideo(publisherVideo).show();
        }

        private void setTagButton() {
            float margin = LKUtil.dp2px(mContext, 20);

            view.findViewById(R.id.layout_bottom).post(new Runnable() {
                @Override
                public void run() {
                    float y = view.findViewById(R.id.layout_bottom).getY() - notAdsLl.getHeight();
                    notAdsLl.setY(y - margin);
                    isAdsLl.setY(y - isAdsLl.getHeight() - margin * 2);

                    notAdsLl.setX(ScreenUtil.getScreenW(mContext) - notAdsLl.getWidth() - margin);
                    isAdsLl.setX(ScreenUtil.getScreenW(mContext) - isAdsLl.getWidth() - margin);

                    notAdsLl.setVisibility(View.VISIBLE);
                    isAdsLl.setVisibility(View.VISIBLE);
                }
            });
        }

    }


}