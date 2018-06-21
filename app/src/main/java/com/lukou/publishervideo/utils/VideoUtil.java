package com.lukou.publishervideo.utils;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lukou.publishervideo.widget.VideoView.MyStandardVideoPlayer;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;

import java.util.Formatter;
import java.util.Locale;

/**
 * Created by cxt on 2018/6/21.
 */

public class VideoUtil {
    public static final String VIDEO_TAG = "VIDEO_TAG";

    public static void initVideoView(Context context, MyStandardVideoPlayer videoPlayer, int position, String url, SeekBar seekBar, TextView currentTv, TextView totalTv) {
        GSYVideoManager.instance().setVideoType(context, GSYVideoType.IJKEXOPLAYER2);
        videoPlayer.setTag(VIDEO_TAG);
        videoPlayer.setPlayPosition(position);
        videoPlayer.setReleaseWhenLossAudio(false);
        videoPlayer.setIsTouchWiget(false);
        videoPlayer.setProgressBar(seekBar, currentTv, totalTv);
    }

    public static void setVideoUrl(MyStandardVideoPlayer videoPlayer, String url) {
        videoPlayer.setUpLazy(url, true, null, null, null);
        videoPlayer.startPlayLogic();
    }


    public static void next20per(MyStandardVideoPlayer videoView, LinearLayout button) {
        long position = (long) ((float) (videoView.getDuration() * 0.2 + videoView.getCurrentPositionWhenPlaying()));
        position = position < 9000 ? 9000 : position;
        videoView.seekTo(position);

    }

    public static void last20per(MyStandardVideoPlayer videoView, LinearLayout button) {
        long position = (long) ((float) (-videoView.getDuration() * 0.2 + videoView.getCurrentPositionWhenPlaying()));
        videoView.seekTo(position);
    }

    public static void replay(MyStandardVideoPlayer videoView, LinearLayout button) {
        videoView.startPlayLogic();
        videoView.seekTo(0);
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
