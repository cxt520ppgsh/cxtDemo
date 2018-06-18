package com.lukou.publishervideo.mvp.home.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.intersection.listmodule.entity.ResultList;
import com.intersection.listmodule.viewholder.BaseViewHolder;
import com.lukou.publishervideo.R;
import com.lukou.publishervideo.base.ListRecyclerViewAdapter;
import com.lukou.publishervideo.bean.PublisherVideo;
import com.lukou.publishervideo.mvp.home.HomeActivityContract;
import com.lukou.publishervideo.mvp.home.v.dialog.CommodityDialog;
import com.lukou.publishervideo.mvp.home.v.dialog.SetAsignerDialog;
import com.lukou.publishervideo.mvp.home.v.dialog.SetTagDialog;
import com.lukou.publishervideo.utils.ThreadPoolUtil;
import com.lukou.publishervideo.utils.VitamioUtil;
import com.lukou.publishervideo.utils.netUtils.ApiFactory;
import com.lukou.publishervideo.widget.VideoRecycleView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cxt on 2018/6/14.
 */

public class HomeRvAdapter extends RecyclerView.Adapter<HomeRvAdapter.HomeRvItemViewHolder> {
    @Inject
    ApiFactory apiFactory;
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
        VideoView videoView;
        @BindView(R.id.next20Per)
        LinearLayout next20Per;
        @BindView(R.id.last20Per)
        LinearLayout last20Per;
        @BindView(R.id.replay)
        LinearLayout replay;
        @BindView(R.id.playbar)
        FrameLayout playbar;
        PublisherVideo publisherVideo;

        HomeRvItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.setIsRecyclable(false);
        }

        private void setVideoView(PublisherVideo video) {
            this.publisherVideo = video;
            VitamioUtil.initVideo(mContext, videoView, video.getVideoUrl(), playbar, last20Per, next20Per, replay);
        }

        public void setVideoURL(PublisherVideo video) {
            if (videoView != null) {
                videoView.setVideoURI(Uri.parse(video.getVideoUrl()));

                ThreadPoolUtil.getSingleThreadPool().execute(() -> {
                    while (true) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("sadasdsadas");
                        if (videoView.getController() != null) {
                            videoView.getController().show();
                        }

                    }
                });

            }
        }

        public void destroyVideoView() {
            if (videoView != null) {
                videoView.stopPlayback();
            }

        }


        @OnClick(R.id.asigner)
        void setAsigner() {
            new SetAsignerDialog(mContext, publisherVideo).show();
        }

        @OnClick(R.id.isAds)
        void isAds() {
            new SetTagDialog(mContext, publisherVideo).show();
        }


        @OnClick(R.id.commondity)
        void commondity() {
            new CommodityDialog(mContext, publisherVideo).show();
        }


    }


}