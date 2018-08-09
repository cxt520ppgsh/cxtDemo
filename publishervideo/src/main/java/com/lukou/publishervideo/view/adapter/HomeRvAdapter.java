package com.lukou.publishervideo.view.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lukou.publishervideo.R;
import com.lukou.base.ui.BaseRecycleViewAdapter;
import com.lukou.base.bean.EventBusMessage;
import com.lukou.publishervideo.databinding.AcitivityHomeRvItemBinding;
import com.lukou.publishervideo.model.bean.PublisherVideo;
import com.lukou.publishervideo.model.net.ApiFactory;
import com.lukou.publishervideo.presenter.HomeActivityPresenter;
import com.lukou.base.utils.EventBusUtils;
import com.lukou.base.utils.SharedPreferencesUtil;
import com.lukou.publishervideo.view.activity.HomeActivity;
import com.lukou.publishervideo.view.dialog.CommodityDialog;
import com.lukou.publishervideo.view.dialog.SetTagDialog;
import com.lukou.base.utils.LKUtil;
import com.lukou.base.utils.ScreenUtil;
import com.lukou.publishervideo.view.widget.MyVideoViewController;
import com.lukou.publishervideo.view.widget.VideoRecycleView;


import javax.inject.Inject;

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
        EventBusUtils.post(new EventBusMessage(HomeActivity.HOMEACTIVITY_ADD_SUBSCRIBTION,
                mApiFactory.getPublisherVideo(1, 0, mSharedPreferences.getString(SharedPreferencesUtil.SP_ASIGNER_NAME, "")
                ).subscribe(list -> setList(list), throwable -> {
                })));

    }

    @Override
    public void loadMore() {
        EventBusUtils.post(new EventBusMessage(HomeActivity.HOMEACTIVITY_ADD_SUBSCRIBTION,
                mApiFactory.getPublisherVideo(1, 0, mSharedPreferences.getString(SharedPreferencesUtil.SP_ASIGNER_NAME, "")
                ).subscribe(list -> addList(list), throwable -> {

                })));
    }

    @Override
    public void onRefreshFinish() {
        super.onRefreshFinish();
        recyclerView.scrollToHead();
    }

    @Override
    public void onLoadMoreFinish() {
        super.onLoadMoreFinish();
        recyclerView.scrollToNext();
    }

    public class HomeRvItemViewHolder extends RecyclerView.ViewHolder {
        AcitivityHomeRvItemBinding binding;
        PublisherVideo publisherVideo;
        boolean isEnd = false;
        public View view;

        HomeRvItemViewHolder(View view) {
            super(view);
            binding = DataBindingUtil.bind(view);
            this.view = view;
            this.setIsRecyclable(false);
        }

        private void setVideoView(PublisherVideo video, boolean isEnd, int position) {
            this.publisherVideo = video;
            this.isEnd = isEnd;
            setTagButton();
            binding.videoView.initVideoPlayer(position);
            setvideoTag();
            setListener();
        }

        private void setvideoTag() {
            if (publisherVideo.getType() == 2) {
                binding.notAdsBt.setSelected(true);
                binding.isAdsBt.setSelected(false);
            } else if (publisherVideo.getType() != 2 && publisherVideo.getType() != 0) {
                binding.notAdsBt.setSelected(false);
                binding.isAdsBt.setSelected(true);
            } else {
                binding.notAdsBt.setSelected(false);
                binding.isAdsBt.setSelected(false);
            }
        }

        public void setVideoURL(PublisherVideo video) {
            if (binding.videoView != null) {
                binding.videoView.play(video.getVideoUrl());
                recyclerView.setCanScrollToNext(publisherVideo.getType() == 0 ? false : true);
            }
        }

        public void destroyVideoView() {
            binding.videoView.destroy();
        }

        private void setListener() {
            binding.isAdsBt.setOnClickListener(view -> new SetTagDialog.Builder(mContext).setPublisherVideo(publisherVideo).setSetTagFinishListener(new SetTagDialog.SetTagFinishListener() {
                @Override
                public void onSetTagSuccess() {
                    EventBusUtils.post(new EventBusMessage(HomeActivity.HOMEACTIVITY_INIT_ASIGNER_TV, ""));
                    recyclerView.setCanScrollToNext(true);
                    recyclerView.scrollToNext();
                }

                @Override
                public void onSetTagFaild() {

                }
            }).show());

            binding.notAdsBt.setOnClickListener(view -> EventBusUtils.post(new EventBusMessage(HomeActivity.HOMEACTIVITY_ADD_SUBSCRIBTION,
                    mApiFactory.setTag(publisherVideo.getFid(), 2).subscribe(pandaHackerHttpResult -> {
                        publisherVideo.setType(2);
                        recyclerView.setCanScrollToNext(true);
                        if (!isEnd) {
                            recyclerView.scrollToNext();
                        } else {
                            //滚动到footer
                            recyclerView.smoothScrollToPosition(getList().size());
                        }
                        EventBusUtils.post(new EventBusMessage(HomeActivity.HOMEACTIVITY_INIT_ASIGNER_TV, ""));
                    }, throwable -> {


                    }))));

            binding.commondity.setOnClickListener(view -> new CommodityDialog.Builder(mContext).setPublisherVideo(publisherVideo).show());
        }

        private void setTagButton() {
            float margin = LKUtil.dp2px(mContext, 20);

            view.findViewById(R.id.layout_bottom).post(() -> {
                float y = view.findViewById(R.id.layout_bottom).getY() - binding.notAds.getHeight();
                binding.notAds.setY(y - margin);
                binding.isAds.setY(y - binding.isAds.getHeight() - margin * 2);

                binding.notAds.setX(ScreenUtil.getScreenW(mContext) - binding.notAds.getWidth() - margin);
                binding.isAds.setX(ScreenUtil.getScreenW(mContext) - binding.isAds.getWidth() - margin);

                binding.notAds.setVisibility(View.VISIBLE);
                binding.isAds.setVisibility(View.VISIBLE);
            });
        }

    }


}