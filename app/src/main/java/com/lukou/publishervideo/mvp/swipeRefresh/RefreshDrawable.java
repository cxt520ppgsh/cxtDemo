package com.lukou.publishervideo.mvp.swipeRefresh;

import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;

/**
 * Created by wangzhicheng on 2017/6/26.
 * 抽象动图类，要展示的动图继承这个类,动画效果自定义
 */

public abstract class RefreshDrawable extends Drawable implements Drawable.Callback, Animatable {

    private MySwipeRefreshLayout mRefreshLayout;

    public RefreshDrawable(Context context, MySwipeRefreshLayout layout) {
        mRefreshLayout = layout;
    }

    @Nullable
    public Context getContext() {
        return mRefreshLayout != null ? mRefreshLayout.getContext() : null;
    }

    public MySwipeRefreshLayout getRefreshLayout() {
        return mRefreshLayout;
    }

    /**
     * 设置下拉百分比
     *
     * @param percent range:0.0~1.0
     */
    public abstract void setPercent(float percent);

    /**
     * 计算顶部和底部的距离
     *
     * @param offset
     */
    public abstract void offsetTopAndBottom(int offset);

    @Override
    public void invalidateDrawable(Drawable who) {
        final Callback callback = getCallback();
        if (callback != null) {
            callback.invalidateDrawable(this);
        }
    }

    @Override
    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        final Callback callback = getCallback();
        if (callback != null) {
            callback.scheduleDrawable(this, what, when);
        }
    }

    @Override
    public void unscheduleDrawable(Drawable who, Runnable what) {
        final Callback callback = getCallback();
        if (callback != null) {
            callback.unscheduleDrawable(this, what);
        }
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void setAlpha(int alpha) {
    }

    public abstract void setText(String refreshText);

    @Override
    public void setColorFilter(ColorFilter cf) {
    }
}