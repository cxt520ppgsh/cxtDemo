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
import android.widget.RelativeLayout;

import com.intersection.listmodule.entity.ResultList;
import com.intersection.listmodule.viewholder.BaseViewHolder;
import com.lukou.publishervideo.R;
import com.lukou.publishervideo.base.ListRecyclerViewAdapter;
import com.lukou.publishervideo.bean.PublisherVideo;
import com.lukou.publishervideo.mvp.home.HomeActivityContract;
import com.lukou.publishervideo.utils.VitamioUtil;
import com.lukou.publishervideo.utils.netUtils.ApiFactory;

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
        publisherVideos = list;
        notifyDataSetChanged();
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
        holder.setVideo(publisherVideos.get(position));
    }

    @Override
    public int getItemCount() {
        return publisherVideos.size();
    }


    protected class HomeRvItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.videoView)
        VideoView videoView;
        @BindView(R.id.next20Per)
        Button next20Per;
        @BindView(R.id.last20Per)
        Button last20Per;
        @BindView(R.id.replay)
        Button replay;
        @BindView(R.id.playbar)
        RelativeLayout playbar;

        HomeRvItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void setVideo(PublisherVideo video) {
            VitamioUtil.initVideo(mContext, videoView, video.getVideoUrl(), playbar, last20Per, next20Per, replay);
        }

        @OnClick(R.id.last20Per)
        void last20Per() {
            VitamioUtil.last20per(videoView, last20Per);
        }

        @OnClick(R.id.next20Per)
        void next20Per() {
            VitamioUtil.next20per(videoView, next20Per);
        }

        @OnClick(R.id.replay)
        void replay() {
            VitamioUtil.replay(videoView, replay);
        }

    }


}