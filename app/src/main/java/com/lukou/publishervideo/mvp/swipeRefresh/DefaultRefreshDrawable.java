package com.lukou.publishervideo.mvp.swipeRefresh;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.util.TypedValue;

import com.lukou.publishervideo.R;
import com.lukou.publishervideo.utils.LKUtil;


/**
 * Created by wangzhicheng on 2017/7/4.
 */

public class DefaultRefreshDrawable extends RefreshDrawable {
    private static final int MAX_LEVEL = 25;
    private boolean isRunning;
    private Rect mBounds;
    private int mHeight;
    private Paint mPaint;
    private TextPaint mTextPaint;
    private int mLevel = 20;
    private float mDegress;
    private String mText = "下拉刷新";
    protected float oldPercent;
    private Bitmap bitmapPanda;
    private Bitmap bitmapLeaf;
    protected float mPercent;
    private int direction = 1;

    DefaultRefreshDrawable(Context context, MySwipeRefreshLayout layout) {
        super(context, layout);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(dp2px(3));
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(LKUtil.dip2px(getContext(), 13));
        mTextPaint.setColor(getContext().getResources().getColor(R.color.text_refresh_grey));
        bitmapPanda = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.panda);
        bitmapLeaf = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.leaf);
    }

    @Override
    public void setPercent(float percent) {
        mPercent = percent;
    }


    @Override
    public void offsetTopAndBottom(int offset) {
        invalidateSelf();
    }

    @Override
    public void setText(String refreshText) {
        this.mText = refreshText;
    }

    @Override
    public void start() {
        isRunning = true;
        invalidateSelf();
    }

    private void updateLevel(int level) {
        float percent = level % 30 / 30f;
        mDegress = 20 * percent;
    }

    @Override
    public void stop() {
        isRunning = false;
        mDegress = 0;
    }


    @Override
    public boolean isRunning() {
        return isRunning;
    }


    //打印出bounds的值
    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mHeight = getRefreshLayout().getFinalOffset();
        mBounds = bounds;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.save();
        drawLeaf(canvas);
        canvas.drawBitmap(bitmapPanda, mBounds.width() / 3, mPercent * mHeight - bitmapPanda.getHeight(), mPaint);
        drawText(canvas);
        canvas.restore();
        if (isRunning) {
            if (mLevel == MAX_LEVEL || mLevel == -MAX_LEVEL) {
                direction = -direction;
            }
            if (direction == 1 && Math.abs(mLevel) <= MAX_LEVEL) {
                ++mLevel;
            } else {
                --mLevel;
            }
            updateLevel(mLevel);
            invalidateSelf();
        }
    }

    private void drawLeaf(Canvas canvas) {
        int bitmapPandaHeight = bitmapPanda.getHeight();
        int bitmapPandaWidth = bitmapPanda.getWidth();
        canvas.translate(mBounds.width() / 3 + bitmapPandaWidth / 2, mHeight);
        canvas.rotate(mDegress);
        canvas.drawBitmap(bitmapLeaf, -bitmapLeaf.getWidth() / 2, -bitmapLeaf.getHeight() - (int) (bitmapPandaHeight * 0.9), mPaint);
        canvas.rotate(-mDegress);
        canvas.translate(-(mBounds.width() / 3 + bitmapPandaWidth / 2), -(mHeight));
    }

    private void drawText(Canvas canvas) {
        canvas.drawText(mText, mBounds.width() / 2, mPercent * mHeight - dp2px(2), mTextPaint);
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getContext().getResources().getDisplayMetrics());
    }

    private int evaluate(float fraction, int startValue, int endValue) {
        int startA = (startValue >> 24) & 0xff;
        int startR = (startValue >> 16) & 0xff;
        int startG = (startValue >> 8) & 0xff;
        int startB = startValue & 0xff;

        int endA = (endValue >> 24) & 0xff;
        int endR = (endValue >> 16) & 0xff;
        int endG = (endValue >> 8) & 0xff;
        int endB = endValue & 0xff;

        return ((startA + (int) (fraction * (endA - startA))) << 24) |
                ((startR + (int) (fraction * (endR - startR))) << 16) |
                ((startG + (int) (fraction * (endG - startG))) << 8) |
                ((startB + (int) (fraction * (endB - startB))));
    }

}
