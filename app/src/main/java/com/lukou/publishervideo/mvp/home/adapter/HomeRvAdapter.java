package com.lukou.publishervideo.mvp.home.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lukou.publishervideo.R;
import com.lukou.publishervideo.bean.PublisherVideo;
import com.lukou.publishervideo.mvp.home.v.activity.HomeActivity;
import com.lukou.publishervideo.mvp.home.v.dialog.CommodityDialog;
import com.lukou.publishervideo.mvp.home.v.dialog.SetTagDialog;
import com.lukou.publishervideo.utils.LKUtil;
import com.lukou.publishervideo.utils.VideoUtil;
import com.lukou.publishervideo.utils.netUtils.ApiFactory;
import com.lukou.publishervideo.utils.netUtils.KuaishouHttpResult;
import com.lukou.publishervideo.utils.netUtils.ScreenUtil;
import com.lukou.publishervideo.widget.MyVideoView;
import com.lukou.publishervideo.widget.VideoRecycleView;


import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by cxt on 2018/6/14.
 */

public class HomeRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private HomeActivity homeActivity;
    private List<PublisherVideo> publisherVideos = new ArrayList<>();
    @Inject
    ApiFactory apiFactory;

    @Inject
    HomeRvAdapter(Context context) {
        mContext = context;
        homeActivity = (HomeActivity) context;
    }

    public void setPublisherVideoList(List<PublisherVideo> list) {
        if (list == null) {
            return;
        }
        publisherVideos = list;
        notifyDataSetChanged();

    }

    public void addPublisherVideoList(List<PublisherVideo> list) {
        for (PublisherVideo publisherVideo : list) {
            publisherVideos.add(publisherVideo);
        }
        notifyDataSetChanged();
    }

    public List<PublisherVideo> getPublisherVideoList() {
        return publisherVideos;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.acitivity_home_rv_item, parent, false);
            HomeRvItemViewHolder holder = new HomeRvItemViewHolder(view);
            return holder;
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.activity_home_rv_foot, parent,
                    false);
            return new FootViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HomeRvItemViewHolder) {
            ((HomeRvItemViewHolder) holder).setVideoView(publisherVideos.get(position), position == publisherVideos.size() - 1, position);
            //防止第一个视频不刷新
            if (position == 0 && VideoRecycleView.getCurrentPosition() == 0) {
                ((HomeRvItemViewHolder) holder).setVideoURL(publisherVideos.get(0));
            }
        }
    }


    @Override
    public int getItemCount() {
        return publisherVideos.size() == 0 ? 0 : publisherVideos.size() + 1;
    }


    public class HomeRvItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.videoView)
        MyVideoView videoView;
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
            VideoUtil.initVideoView(mContext, videoView, position);
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
                VideoUtil.setVideoUrl(videoView, video.getVideoUrl());
                VideoRecycleView.setCanScrollToNext(publisherVideo.getType() == 0 ? false : true);
            }
        }

        public void destroyVideoView() {
            videoView.release();
        }

        @OnClick(R.id.isAds_bt)
        void isAds_bt_Click() {
            new SetTagDialog(mContext, publisherVideo).show();

        }

        @OnClick(R.id.notAds_bt)
        void notAds_bt_Click() {
            homeActivity.addSubscription(apiFactory.setTag(publisherVideo.getFid(), 2).subscribe(new Action1<KuaishouHttpResult>() {
                @Override
                public void call(KuaishouHttpResult kuaishouHttpResult) {
                    publisherVideo.setType(2);
                    VideoRecycleView.setCanScrollToNext(true);
                    if (!isEnd) {
                        homeActivity.rv.scrollToNext();
                    } else {
                        //滚动到footer
                        homeActivity.rv.smoothScrollToPosition(publisherVideos.size());
                    }
                    homeActivity.initAsignerTv();
                }
            }, throwable -> {


            }));
        }

        @OnClick(R.id.commondity)
        void commondity() {
            new CommodityDialog(mContext, publisherVideo).show();
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


    private class FootViewHolder extends RecyclerView.ViewHolder {

        public FootViewHolder(View view) {
            super(view);
        }
    }

}