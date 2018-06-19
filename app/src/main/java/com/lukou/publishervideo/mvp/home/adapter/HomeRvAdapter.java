package com.lukou.publishervideo.mvp.home.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lukou.publishervideo.R;
import com.lukou.publishervideo.bean.PublisherVideo;
import com.lukou.publishervideo.mvp.home.v.dialog.CommodityDialog;
import com.lukou.publishervideo.mvp.home.v.dialog.SetAsignerDialog;
import com.lukou.publishervideo.mvp.home.v.dialog.SetTagDialog;
import com.lukou.publishervideo.utils.VideoPlayerUtil;
import com.lukou.publishervideo.utils.netUtils.ApiFactory;
import com.lukou.publishervideo.widget.VideoRecycleView;
import com.xiao.nicevideoplayer.NiceVideoPlayer;
import com.xiao.nicevideoplayer.NiceVideoPlayerManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by cxt on 2018/6/14.
 */

public class HomeRvAdapter extends RecyclerView.Adapter<HomeRvAdapter.HomeRvItemViewHolder> {
    private Context mContext;
    private List<PublisherVideo> publisherVideos = new ArrayList<>();

    @Inject
    HomeRvAdapter(Context context) {
        mContext = context;
    }

    public void setPublisherVideoList(List<PublisherVideo> list) {
        for (PublisherVideo publisherVideo : list) {
            publisherVideos.add(publisherVideo);
        }
        notifyDataSetChanged();
    }

    public List<PublisherVideo> getPublisherVideoList() {
        return publisherVideos;
    }

    @NonNull
    @Override
    public HomeRvItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.acitivity_home_rv_item, parent, false);
        HomeRvItemViewHolder holder = new HomeRvItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeRvItemViewHolder holder, int position) {
        holder.setVideoView(publisherVideos.get(position));
        if (position == 0 && VideoRecycleView.getCurrentPosition() == 0) {
            holder.setVideoURL(publisherVideos.get(0));
            VideoRecycleView.setCanScrollToNext(true);
        }
    }


    @Override
    public int getItemCount() {
        return publisherVideos.size();
    }


    public class HomeRvItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.videoView)
        NiceVideoPlayer videoView;
        @BindView(R.id.next20Per)
        LinearLayout next20Per;
        @BindView(R.id.last20Per)
        LinearLayout last20Per;
        @BindView(R.id.replay)
        LinearLayout replay;
        @BindView(R.id.mediacontroller_time_current)
        TextView currentbarTv;
        @BindView(R.id.mediacontroller_time_total)
        TextView totalTv;
        @BindView(R.id.mediacontroller_seekbar)
        SeekBar seekbarTv;
        PublisherVideo publisherVideo;


        HomeRvItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.setIsRecyclable(false);
        }

        private void setVideoView(PublisherVideo video) {
            this.publisherVideo = video;
            VideoPlayerUtil.initVideoView(videoView, mContext, seekbarTv, currentbarTv, totalTv);
        }

        public void setVideoURL(PublisherVideo video) {
            if (videoView != null) {
                VideoPlayerUtil.setVideoUrl(videoView, video.getVideoUrl());
            }
        }

        public void destroyVideoView() {
            if (videoView == NiceVideoPlayerManager.instance().getCurrentNiceVideoPlayer()) {
                NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
            }
        }

        @OnClick(R.id.isAds_bt)
        void isAds_bt_Click() {
            new SetTagDialog(mContext, publisherVideo).show();

        }

        @OnClick(R.id.notAds_bt)
        void notAds_bt_Click() {

        }


        @OnClick(R.id.commondity)
        void commondity() {
            new CommodityDialog(mContext, publisherVideo).show();
        }

        @OnClick(R.id.next20Per)
        void next20Click() {
            VideoPlayerUtil.next20per(videoView, next20Per);
        }

        @OnClick(R.id.last20Per)
        void last20Click() {
            VideoPlayerUtil.last20per(videoView, last20Per);
        }

        @OnClick(R.id.replay)
        void replayClick() {
            VideoPlayerUtil.replay(videoView, replay);
        }


    }


}