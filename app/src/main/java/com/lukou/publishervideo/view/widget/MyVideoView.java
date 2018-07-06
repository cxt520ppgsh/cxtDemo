package com.lukou.publishervideo.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;
import android.widget.SeekBar;

import com.lukou.publishervideo.R;
import com.lukou.publishervideo.utils.VideoUtil;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by cxt on 2018/7/4.
 */

public class MyVideoView extends StandardGSYVideoPlayer {
    private Pair<Boolean, Long> completeToSeek;

    public MyVideoView(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public MyVideoView(Context context) {
        super(context);
    }

    public MyVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        ButterKnife.bind(this);
        setProgressBar();
    }

    //防止播放结束后Seekbar拖动无效
    private void setProgressBar() {
        if (mProgressBar == null) {
            return;
        }
        mProgressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                long position = (long) ((float) (getDuration() * (long) seekBar.getProgress()) / 100.0F);
                if (mCurrentState == CURRENT_STATE_AUTO_COMPLETE) {
                    completeToSeek = Pair.create(true, position);
                    startPlayLogic();
                } else {
                    seekTo(position);
                }

            }
        });
    }

    @Override
    public void onPrepared() {
        super.onPrepared();
        if (completeToSeek != null) {
            seekTo(completeToSeek.second);
            completeToSeek = null;
        }
        //防止初次加载时Seekbar不动
        onClickUiToggle();
    }

    @OnClick(R.id.last20Per)
    void last20PerClick() {
        onClickUiToggle();
        VideoUtil.last20per(this);
    }

    @OnClick(R.id.next20Per)
    void next20PerClick() {
        onClickUiToggle();
        VideoUtil.next20per(this);
    }

    @OnClick(R.id.replay)
    void replayClick() {
        onClickUiToggle();
        VideoUtil.replay(this);
    }

    //不隐藏底部按钮
    @Override
    protected void onClickUiToggle() {
        if (mCurrentState == CURRENT_STATE_PLAYING) {
            if (mBottomContainer != null) {
                if (mBottomContainer.getVisibility() == View.VISIBLE) {
                    setViewShowState(mBottomContainer, VISIBLE);
                }
            }
        }
    }

    //不隐藏底部按钮
    @Override
    protected void hideAllWidget() {
        setViewShowState(mBottomContainer, VISIBLE);
        setViewShowState(mTopContainer, INVISIBLE);
        setViewShowState(mBottomProgressBar, VISIBLE);
        setViewShowState(mStartButton, INVISIBLE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.my_video_player_layout;
    }

}
