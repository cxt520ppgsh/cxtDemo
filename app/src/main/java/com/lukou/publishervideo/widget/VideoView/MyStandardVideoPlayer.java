package com.lukou.publishervideo.widget.VideoView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lukou.publishervideo.R;
import com.lukou.publishervideo.utils.VideoUtil;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoControlView;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

import java.util.IllegalFormatCodePointException;
import java.util.TimerTask;

/**
 * Created by cxt on 2018/6/21.
 */

public class MyStandardVideoPlayer extends MyVideoPlayer {


    protected SeekBar progressBar;

    protected TextView currentTv;

    protected TextView totalTv;

    private Pair<Boolean, Long> completeToSeek;

    public MyStandardVideoPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public MyStandardVideoPlayer(Context context) {
        super(context);
    }

    public MyStandardVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setProgressBar(SeekBar progressBar, TextView current, TextView total) {
        this.progressBar = progressBar;
        this.currentTv = current;
        this.totalTv = total;
        progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
    }

    @Override
    protected void setProgressAndTime(int progress, int secProgress, int currentTime, int totalTime) {
        super.setProgressAndTime(progress, secProgress, currentTime, totalTime);
        if (progressBar != null && currentTv != null && totalTv != null) {
            progressBar.setProgress(progress);
            progressBar.setSecondaryProgress(secProgress);
            currentTv.setText(VideoUtil.formatTime(currentTime));
            totalTv.setText(VideoUtil.formatTime(totalTime));
        }
    }


    //播放视频
    @Override
    public void startPlayLogic() {
        prepareVideo();
        startDismissControlViewTimer();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return false;
    }

    /**
     * 继承后重写可替换为你需要的布局
     *
     * @return
     */
    @Override
    public int getLayoutId() {
        return R.layout.videoplayer_layout;
    }

    @Override
    protected void showWifiDialog() {

    }

    @Override
    protected void showProgressDialog(float deltaX, String seekTime, int seekTimePosition, String totalTime, int totalTimeDuration) {


    }

    @Override
    protected void dismissProgressDialog() {

    }

    @Override
    protected void showVolumeDialog(float deltaY, int volumePercent) {

    }

    @Override
    protected void dismissVolumeDialog() {

    }

    @Override
    protected void showBrightnessDialog(float percent) {

    }

    @Override
    protected void dismissBrightnessDialog() {

    }

    @Override
    protected void onClickUiToggle() {

    }

    @Override
    protected void hideAllWidget() {

    }

    @Override
    protected void changeUiToNormal() {

    }

    @Override
    protected void changeUiToPreparingShow() {

    }

    @Override
    protected void changeUiToPlayingShow() {

    }

    @Override
    protected void changeUiToPauseShow() {

    }

    @Override
    protected void changeUiToError() {

    }

    @Override
    protected void changeUiToCompleteShow() {

    }

    @Override
    protected void changeUiToPlayingBufferingShow() {

    }




}
