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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

/**
 * Created by cxt on 2018/6/12.
 */

public class VitamioUtil {
    public static final int SEEK_COMPLEATE = 3;

    public static MediaController initVideo(Context context, VideoView videoView, String url, FrameLayout playBar, LinearLayout last20, LinearLayout next20, LinearLayout replay) {
        MediaController controller = new MediaController(context, true, playBar);
        if (Vitamio.isInitialized(context)) {
            ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) videoView.getLayoutParams();

            //float scale = (videoView.getWidth() * 1.00f) / (videoView.getHeight() * 1.00f);
            //videoView.setVideoLayout(VideoView.VIDEO_LAYOUT_FIT_PARENT,scale);
            //videoView.setVideoURI(Uri.parse(url));
            videoView.setMediaController(controller);
            videoView.setBufferSize(10240);
            videoView.requestFocus();
            videoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_LOW);
            videoView.setOnPreparedListener(mediaPlayer -> {
                mediaPlayer.setPlaybackSpeed(1.0f);
                mediaPlayer.setLooping(false);
                mediaPlayer.start();
                //controller.setInstantSeeking(true);
            });
            videoView.setOnBufferingUpdateListener((mp, percent) -> {
                //percentTv.setText("已缓冲：" + percent + "%");
            });
            videoView.setOnInfoListener((mp, what, extra) -> {
                switch (what) {
                    //开始缓冲
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                        mp.pause();
                        break;
                    //缓冲结束
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                        mp.start(); //缓冲结束再播放
                        break;
                    //正在缓冲
                    case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                        //netSpeedTv.setText("当前网速:" + extra + "kb/s");
                        break;
                }
                return true;
            });
            videoView.setOnSeekCompleteListener(mp -> {
                last20.setClickable(true);
                next20.setClickable(true);
                replay.setClickable(true);
            });
            videoView.setOnErrorListener((mp, what, extra) -> false);
            videoView.setOnCompletionListener(mp -> {

            });

            setListener(videoView, last20, next20, replay);
        }
        return controller;
    }

    public static void setListener(VideoView videoView, LinearLayout last20, LinearLayout next20, LinearLayout replay) {
        last20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                last20per(videoView, last20);
            }
        });
        next20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next20per(videoView, next20);
            }
        });
        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replay(videoView, replay);
            }
        });
    }

    public static void next20per(VideoView videoView, LinearLayout button) {
        button.setClickable(false);
        videoView.pause();
        videoView.seekTo((long) (videoView.getCurrentPosition() + (videoView.getDuration() * 0.2)));
    }

    public static void last20per(VideoView videoView, LinearLayout button) {
        button.setClickable(false);
        videoView.pause();
        videoView.seekTo((long) (videoView.getCurrentPosition() - (videoView.getDuration() * 0.2)));
    }

    public static void replay(VideoView videoView, LinearLayout button) {
        button.setClickable(false);
        videoView.pause();
        videoView.seekTo(0);
    }

}
