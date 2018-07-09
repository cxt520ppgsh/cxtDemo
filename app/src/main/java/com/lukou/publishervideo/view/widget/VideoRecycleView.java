package com.lukou.publishervideo.view.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Toast;

import com.lukou.publishervideo.utils.ScreenUtil;

/**
 * Created by cxt on 2018/6/18.
 */

public class VideoRecycleView extends RecyclerView {
    private float ACTION_Y = 0;
    private float dowxY = 0;
    private float dy = 0;
    private static int currentPosition = 0;
    LinearLayoutManager layoutMgr;
    //当 当前Item的视频未打标签是设为false阻止上滑到下一个视频，否则设为true
    private boolean canScrollToNext = false;
    private boolean isToasted = false;

    public VideoRecycleView(Context context) {
        this(context, null, 0);
    }

    public VideoRecycleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        ACTION_Y = (float) (ScreenUtil.getScreenH(context) * 0.3);
    }

    public void setCanScrollToNext(boolean canScrollNext) {
        canScrollToNext = canScrollNext;
    }

    public void scrollToNext() {
        layoutMgr = (LinearLayoutManager) getLayoutManager();
        int firstPosition = layoutMgr.findFirstVisibleItemPosition();
        currentPosition = firstPosition + 1;
        smoothScrollToPosition(firstPosition + 1);
    }

    public void scrollToHead() {
        currentPosition = 0;
        scrollToPosition(0);
    }

    public static int getCurrentPosition() {
        return currentPosition;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dowxY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                dy = ev.getY() - dowxY;
                if (Math.abs(dy) >= 5) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        layoutMgr = (LinearLayoutManager) getLayoutManager();
        int firstPosition = layoutMgr.findFirstVisibleItemPosition();
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dowxY = e.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                dy = e.getY() - dowxY;
                if (Math.abs(dy) < 10) {
                    return true;
                }

                if (!canScrollToNext && dy < 0) {
                    dy = 0;
                    if (!isToasted) {
                        isToasted = true;
                        Toast.makeText(getContext(), "先打标签才可以浏览下个视频", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }

                break;
            case MotionEvent.ACTION_UP:
                isToasted = false;
                if (!canScrollVertically(-1)) {
                    dy = 0;
                    return super.onTouchEvent(e);
                }

                if (dy > 0 && dy < ACTION_Y) {
                    currentPosition = firstPosition + 1;
                    smoothScrollToPosition(firstPosition + 1);
                } else if (dy >= ACTION_Y) {//下滑
                    currentPosition = firstPosition;
                    smoothScrollToPosition(firstPosition);
                } else if (dy < 0 && dy > -ACTION_Y) {//上滑
                    currentPosition = firstPosition;
                    smoothScrollToPosition(firstPosition);
                } else if (dy <= -ACTION_Y) {
                    currentPosition = firstPosition + 1;
                    smoothScrollToPosition(firstPosition + 1);
                } else {
                    currentPosition = firstPosition;
                    smoothScrollToPosition(firstPosition);
                }
                return true;
            default:
                break;
        }
        return super.onTouchEvent(e);
    }

}
