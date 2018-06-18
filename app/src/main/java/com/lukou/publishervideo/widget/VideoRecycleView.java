package com.lukou.publishervideo.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Toast;

import com.lukou.publishervideo.utils.netUtils.ScreenUtil;

/**
 * Created by cxt on 2018/6/18.
 */

public class VideoRecycleView extends RecyclerView {
    private float ACTION_Y = 0;
    private float dowxY = 0;
    private float dy = 0;
    private static int currentPosition = 0;
    LinearLayoutManager layoutMgr;
    private static boolean canScrollToNext = false;

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

    public static void setCanScrollToNext(boolean canScrollNext) {
        canScrollToNext = canScrollNext;
    }

    public static int getCurrentPosition() {
        return currentPosition;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {

            case MotionEvent.ACTION_DOWN:
                dowxY = e.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                dy = e.getY() - dowxY;
                if (!canScrollToNext && dy < 0) {
                    dy = 0;
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                layoutMgr = (LinearLayoutManager) getLayoutManager();
                int firstPosition = layoutMgr.findFirstVisibleItemPosition();
                if (dy > 0 && dy < ACTION_Y && firstPosition != 0) {
                    currentPosition = firstPosition + 1;
                    smoothScrollToPosition(firstPosition + 1);
                } else if (dy >= ACTION_Y) {//下滑
                    currentPosition = firstPosition;
                    smoothScrollToPosition(firstPosition);
                } else if (dy < 0 && dy > -ACTION_Y) {//上滑
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
