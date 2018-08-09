package com.lukou.publishervideo.view.widget;

/**
 * Created by cxt on 2018/7/9.
 */

public interface MyVideoViewController {
    void play(String url);

    void replay();

    void seekto(float per);

    void seekto(long per);

    void pause();

    void stop();

    void destroy();

    void setLiveDataUI(int progress, int secProgress, int currentTime, int totalTime);

    void initVideoPlayer(int position);

    void setSeekBar();

    void seekBarFollow();
}
