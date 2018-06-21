package com.lukou.publishervideo.widget.VideoView;

import android.content.Context;
import android.util.AttributeSet;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoViewBridge;

/**
 * Created by cxt on 2018/6/21.
 */

public abstract class MyVideoPlayer extends MyBaseVideoPlayer {

    public MyVideoPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public MyVideoPlayer(Context context) {
        super(context);
    }

    public MyVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyVideoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /*******************************下面方法为管理器和播放控件交互的方法****************************************/

    @Override
    public GSYVideoViewBridge getGSYVideoManager() {
        GSYVideoManager.instance().initContext(getContext().getApplicationContext());
        return GSYVideoManager.instance();
    }

    @Override
    protected boolean backFromFull(Context context) {
        return GSYVideoManager.backFromWindowFull(context);
    }

    @Override
    protected void releaseVideos() {
        GSYVideoManager.releaseAllVideos();
    }

    @Override
    protected int getFullId() {
        return GSYVideoManager.FULLSCREEN_ID;
    }

    @Override
    protected int getSmallId() {
        return GSYVideoManager.SMALL_ID;
    }
}
