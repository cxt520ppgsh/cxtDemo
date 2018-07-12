package com.lukou.publishervideo.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;
import android.widget.SeekBar;

import com.lukou.publishervideo.R;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.util.Formatter;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by cxt on 2018/7/4.
 */

public class MyVideoView extends StandardGSYVideoPlayer implements MyVideoViewController {
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
        setSeekBar();
    }

    @Override
    public void onPrepared() {
        super.onPrepared();
        if (completeToSeek != null) {
            seekTo(completeToSeek.second);
            completeToSeek = null;
        }
        seekBarFollow();
    }

    @OnClick(R.id.last20Per)
    void last20PerClick() {
        seekto(-0.2f);
    }

    @OnClick(R.id.next20Per)
    void next20PerClick() {
        seekto(+0.2f);
    }

    @OnClick(R.id.replay)
    void replayClick() {
        replay();
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

    @Override
    public void play(String url) {
        setUpLazy(url, true, null, null, null);
        startPlayLogic();
    }

    @Override
    public void replay() {
        startPlayLogic();
        seekTo(0);
    }

    @Override
    public void seekto(float per) {
        seekBarFollow();
        long position = (long) ((float) (getDuration() * per + getCurrentPositionWhenPlaying()));
        seekTo(position);
    }

    @Override
    public void seekto(long position) {
        seekBarFollow();
        seekTo(position);
    }

    @Override
    public void pause() {
        pause();
    }

    @Override
    public void stop() {
        stop();
        release();
    }

    @Override
    public void destroy() {

    }

    @Override
    public void setLiveDataUI(int progress, int secProgress, int currentTime, int totalTime) {
        if (mGSYVideoProgressListener != null && mCurrentState == CURRENT_STATE_PLAYING) {
            mGSYVideoProgressListener.onProgress(progress, secProgress, currentTime, totalTime);
        }

        if (mProgressBar == null || mTotalTimeTextView == null || mCurrentTimeTextView == null) {
            return;
        }

        if (!mTouchingProgressBar) {
            if (progress != 0) mProgressBar.setProgress(progress);
        }
        if (getGSYVideoManager().getBufferedPercentage() > 0) {
            secProgress = getGSYVideoManager().getBufferedPercentage();
        }
        if (secProgress > 94) secProgress = 100;
        setSecondaryProgress(secProgress);
        mTotalTimeTextView.setText(CommonUtil.stringForTime(totalTime));
        if (currentTime > 0)
            mCurrentTimeTextView.setText(CommonUtil.stringForTime(currentTime));

        if (mBottomProgressBar != null) {
            if (progress != 0) mBottomProgressBar.setProgress(progress);
            setSecondaryProgress(secProgress);
        }
    }

    @Override
    protected void setProgressAndTime(int progress, int secProgress, int currentTime, int totalTime) {
        setLiveDataUI(progress, secProgress, currentTime, totalTime);
    }

    @Override
    public void setSeekBar() {
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
                    MyVideoView.this.seekto(position);
                }
            }
        });
    }

    @Override
    public void seekBarFollow() {
        onClickUiToggle();
    }

    @Override
    public void initVideoPlayer(int position) {
        GSYVideoManager.instance().setVideoType(getContext(), GSYVideoType.IJKEXOPLAYER2);
        setTag("VIDEO_TAG");
        setPlayPosition(position);
        setReleaseWhenLossAudio(false);
        setIsTouchWiget(false);
    }


    public static String formatTime(long milliseconds) {
        if (milliseconds > 0L && milliseconds < 86400000L) {
            long totalSeconds = milliseconds / 1000L;
            long seconds = totalSeconds % 60L;
            long minutes = totalSeconds / 60L % 60L;
            long hours = totalSeconds / 3600L;
            StringBuilder stringBuilder = new StringBuilder();
            Formatter mFormatter = new Formatter(stringBuilder, Locale.getDefault());
            return hours > 0L ? mFormatter.format("%d:%02d:%02d", new Object[]{Long.valueOf(hours), Long.valueOf(minutes), Long.valueOf(seconds)}).toString() : mFormatter.format("%02d:%02d", new Object[]{Long.valueOf(minutes), Long.valueOf(seconds)}).toString();
        } else {
            return "00:00";
        }
    }

}
