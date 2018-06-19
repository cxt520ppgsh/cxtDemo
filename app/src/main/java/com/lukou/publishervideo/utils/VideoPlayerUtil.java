package com.lukou.publishervideo.utils;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.xiao.nicevideoplayer.NiceUtil;
import com.xiao.nicevideoplayer.NiceVideoPlayer;
import com.xiao.nicevideoplayer.NiceVideoPlayerController;
import com.xiao.nicevideoplayer.TxVideoPlayerController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;


/**
 * Created by cxt on 2018/6/12.
 */

public class VideoPlayerUtil {
    public static void initVideoView(NiceVideoPlayer videoPlayer, Context context, SeekBar seekBar, TextView currentTv, TextView totalTv) {
        videoPlayer.setPlayerType(NiceVideoPlayer.TYPE_IJK); // or NiceVideoPlayer.TYPE_NATIVE
        MyVideoPlayerController controller = new MyVideoPlayerController(context, seekBar, currentTv, totalTv);
        videoPlayer.setController(controller);
    }

    public static void setVideoUrl(NiceVideoPlayer videoPlayer, String URL) {
        videoPlayer.setUp(URL, null);
        videoPlayer.start();
    }


    public static void next20per(NiceVideoPlayer videoView, LinearLayout button) {
        if (videoView.isBufferingPaused() || videoView.isPaused()) {
            videoView.restart();
        }
        long position = (long) ((float) (videoView.getDuration() * 0.2 + videoView.getCurrentPosition()));
        position = position < 9000 ? 9000 : position;
        videoView.seekTo(position);

    }

    public static void last20per(NiceVideoPlayer videoView, LinearLayout button) {
        if (videoView.isBufferingPaused() || videoView.isPaused()) {
            videoView.restart();
        }
        long position = (long) ((float) (-videoView.getDuration() * 0.2 + videoView.getCurrentPosition()));
        videoView.seekTo(position);
    }

    public static void replay(NiceVideoPlayer videoView, LinearLayout button) {
        videoView.restart();
        videoView.seekTo(0);
    }

    private static class MyVideoPlayerController extends NiceVideoPlayerController implements SeekBar.OnSeekBarChangeListener {
        private SeekBar seekBar;
        private TextView currentProgressTv;
        private TextView totalProgressTv;

        public MyVideoPlayerController(Context context, SeekBar seekBar, TextView currentProgressTv, TextView totalProgressTv) {
            super(context);
            this.seekBar = seekBar;
            this.currentProgressTv = currentProgressTv;
            this.totalProgressTv = totalProgressTv;

            init();
        }

        private void init() {
            seekBar.setProgress(0);
            seekBar.setSecondaryProgress(0);
            seekBar.setOnSeekBarChangeListener(this);
        }

        @Override
        public void setTitle(String s) {

        }

        @Override
        public void setImage(int i) {

        }

        @Override
        public ImageView imageView() {
            return null;
        }

        @Override
        public void setLenght(long l) {

        }

        protected void onPlayStateChanged(int playState) {
            switch (playState) {
                case -1:
                    this.cancelUpdateProgressTimer();
                    break;
                case 2:
                    this.startUpdateProgressTimer();
                    break;
                case 7:
                    this.cancelUpdateProgressTimer();
                    break;
                default:
                    break;

            }

        }

        @Override
        protected void onPlayModeChanged(int i) {

        }

        @Override
        protected void reset() {

        }

        @Override
        protected void updateProgress() {
            long position = this.mNiceVideoPlayer.getCurrentPosition();
            long duration = this.mNiceVideoPlayer.getDuration();
            int bufferPercentage = this.mNiceVideoPlayer.getBufferPercentage();
            seekBar.setSecondaryProgress(bufferPercentage);
            int progress = (int) (100.0F * (float) position / (float) duration);
            seekBar.setProgress(progress);
            currentProgressTv.setText(NiceUtil.formatTime(position));
            totalProgressTv.setText(NiceUtil.formatTime(duration));
        }

        @Override
        protected void showChangePosition(long l, int i) {

        }

        @Override
        protected void hideChangePosition() {

        }

        @Override
        protected void showChangeVolume(int i) {

        }

        @Override
        protected void hideChangeVolume() {

        }

        @Override
        protected void showChangeBrightness(int i) {

        }

        @Override
        protected void hideChangeBrightness() {

        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (this.mNiceVideoPlayer.isBufferingPaused() || this.mNiceVideoPlayer.isPaused()) {
                this.mNiceVideoPlayer.restart();
            }

            long position = (long) ((float) (this.mNiceVideoPlayer.getDuration() * (long) seekBar.getProgress()) / 100.0F);
            this.mNiceVideoPlayer.seekTo(position);
        }
    }


}
