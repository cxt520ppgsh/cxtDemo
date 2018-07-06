package com.lukou.publishervideo.utils;

import android.content.Context;

import com.lukou.publishervideo.view.widget.MyVideoView;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;

import java.util.Formatter;
import java.util.Locale;

/**
 * Created by cxt on 2018/6/21.
 */

public class VideoUtil {
    public static final String VIDEO_TAG = "VIDEO_TAG";

    public static void initVideoView(Context context, MyVideoView myVideoView, int position) {
        GSYVideoManager.instance().setVideoType(context, GSYVideoType.IJKEXOPLAYER2);
        myVideoView.setTag(VIDEO_TAG);
        myVideoView.setPlayPosition(position);
        myVideoView.setReleaseWhenLossAudio(false);
        myVideoView.setIsTouchWiget(false);
    }

    public static void setVideoUrl(MyVideoView myVideoView, String url) {
        myVideoView.setUpLazy(url, true, null, null, null);
        myVideoView.startPlayLogic();
    }

    //按百分比跳
    public static void perSeek(MyVideoView videoView, float per) {
        long position = (long) ((float) (videoView.getDuration() * per + videoView.getCurrentPositionWhenPlaying()));
        position = position < 9000 ? 9000 : position;
        videoView.seekTo(position);

    }

    public static void replay(MyVideoView videoView) {
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
