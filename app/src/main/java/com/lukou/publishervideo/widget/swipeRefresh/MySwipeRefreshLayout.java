package com.lukou.publishervideo.widget.swipeRefresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ListViewCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.lidao.httpmodule.http.utils.NetworkUtil;
import com.lukou.publishervideo.R;
import com.lukou.publishervideo.utils.ToastManager;

import java.security.InvalidParameterException;

/**
 * Created by wangzhicheng on 2017/6/26.
 * 自定义SwipeRefreshLayout
 * 用法同SwipeRefreshLayout
 */

public class MySwipeRefreshLayout extends ViewGroup implements NestedScrollingChild, NestedScrollingParent {

    private static final float DECELERATE_INTERPOLATION_FACTOR = 2f;
    private static final int DRAG_MAX_DISTANCE = 64;
    private static final int INVALID_POINTER = -1;
    private static final float DRAG_RATE = .5f;

    public static final int STYLE_DEFAULT = 0;
    public static final int STYLE_HOME = 1;
    private String refreshText = "下拉刷新";
    private View mTarget;

    private ImageView mRefreshView;
    private Interpolator mDecelerateInterpolator;
    private int mTouchSlop;
    private int mSpinnerFinalOffset;
    private int mTotalDragDistance;
    private RefreshDrawable mRefreshDrawable;
    private int mCurrentOffsetTop;
    private boolean mRefreshing;
    private int mActivePointerId;
    private boolean mIsBeingDragged;
    private float mInitialMotionY;
    private int mFrom;
    private boolean mNotify;
    private OnRefreshListener mListener;

    public int mDurationToStartPosition;
    public int mDurationToCorrectPosition;
    private int mInitialOffsetTop;
    private boolean mDispatchTargetTouchDown;
    private float mDragPercent;

    //NestedScroll
    private NestedScrollingChildHelper mNestedScrollingChildHelper;
    private final NestedScrollingParentHelper mNestedScrollingParentHelper;
    private boolean mReturningToStart;
    private float mTotalUnconsumed;
    private boolean mNestedScrollInProgress;
    private final int[] mParentScrollConsumed = new int[2];
    private final int[] mParentOffsetInWindow = new int[2];

    public MySwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.refresh_PullRefreshLayout);
        final int type = a.getInteger(R.styleable.refresh_PullRefreshLayout_refreshType, STYLE_DEFAULT);
        a.recycle();

        mNestedScrollingChildHelper = new NestedScrollingChildHelper(this);
        mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        setNestedScrollingEnabled(true);

        mDecelerateInterpolator = new DecelerateInterpolator(DECELERATE_INTERPOLATION_FACTOR);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        int defaultDuration = getResources().getInteger(android.R.integer.config_mediumAnimTime);
        mDurationToStartPosition = defaultDuration;
        mDurationToCorrectPosition = defaultDuration;
        mSpinnerFinalOffset = mTotalDragDistance = dp2px(DRAG_MAX_DISTANCE);

        mRefreshView = new ImageView(context);
        setRefreshStyle(type);
        mRefreshView.setVisibility(View.GONE);
        addView(mRefreshView, 0);
        setWillNotDraw(false);
        this.setChildrenDrawingOrderEnabled(true);
    }

    public int getFinalOffset() {
        return mSpinnerFinalOffset;
    }

    public void setRefreshStyle(int type) {
        setRefreshing(false);
        switch (type) {
            case STYLE_DEFAULT:
                mRefreshDrawable = new DefaultRefreshDrawable(getContext(), this);
                break;
            default:
                throw new InvalidParameterException("Type does not exist");
        }
        mRefreshView.setImageDrawable(mRefreshDrawable);
    }

    public void setRefreshDrawable(RefreshDrawable drawable) {
        setRefreshing(false);
        mRefreshDrawable = drawable;
        mRefreshView.setImageDrawable(mRefreshDrawable);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mTarget == null) {
            ensureTarget();
        }
        if (mTarget == null) {
            return;
        }
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingRight() - getPaddingLeft(), MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY);
        mTarget.measure(widthMeasureSpec, heightMeasureSpec);
        mRefreshView.measure(widthMeasureSpec, heightMeasureSpec);
//        mRefreshView.measure(MeasureSpec.makeMeasureSpec(mRefreshViewWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(mRefreshViewHeight, MeasureSpec.EXACTLY));
    }

    private void ensureTarget() {
        if (mTarget != null) {
            return;
        }
        if (getChildCount() > 0) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child != mRefreshView) {
                    mTarget = child;
                }
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        ensureTarget();

        final int action = ev.getActionMasked();

        if (mReturningToStart && action == MotionEvent.ACTION_DOWN) {
            mReturningToStart = false;
        }

        if (!isEnabled() || mReturningToStart || mNestedScrollInProgress || (canChildScrollUp() && !mRefreshing)) {
            return false;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!mRefreshing) {
                    setTargetOffsetTopAndBottom(0, false);
                }
                mActivePointerId = ev.getPointerId(0);
                mIsBeingDragged = false;
                final float initialMotionY = getMotionEventY(ev, mActivePointerId);
                if (initialMotionY == -1) {
                    return false;
                }
                mInitialMotionY = initialMotionY;
                mInitialOffsetTop = mCurrentOffsetTop;
                mDispatchTargetTouchDown = false;
                mDragPercent = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_POINTER) {
                    return false;
                }
                final float y = getMotionEventY(ev, mActivePointerId);
                if (y == -1) {
                    return false;
                }
                final float yDiff = y - mInitialMotionY;
                if (mRefreshing) {
                    mIsBeingDragged = !(yDiff < 0 && mCurrentOffsetTop <= 0);
                } else if (yDiff > mTouchSlop && !mIsBeingDragged) {
                    mIsBeingDragged = true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;
            default:
                break;
        }

        return mIsBeingDragged;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        final int action = ev.getActionMasked();
        int pointerIndex = -1;

        if (mReturningToStart && action == MotionEvent.ACTION_DOWN) {
            mReturningToStart = false;
        }

        if (!isEnabled() || mReturningToStart || mNestedScrollInProgress || (canChildScrollUp() && !mRefreshing)) {
            return false;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = ev.getPointerId(0);
                mIsBeingDragged = false;
                break;
            case MotionEvent.ACTION_MOVE: {
                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    return false;
                }
                final float y = ev.getY(pointerIndex);
                final float yDiff = y - mInitialMotionY;
                int targetY;
                if (mRefreshing) {
                    targetY = (int) (mInitialOffsetTop + yDiff);
                    if (canChildScrollUp()) {
                        targetY = -1;
                        mInitialMotionY = y;
                        mInitialOffsetTop = 0;
                        if (mDispatchTargetTouchDown) {
                            mTarget.dispatchTouchEvent(ev);
                        } else {
                            MotionEvent obtain = MotionEvent.obtain(ev);
                            obtain.setAction(MotionEvent.ACTION_DOWN);
                            mDispatchTargetTouchDown = true;
                            mTarget.dispatchTouchEvent(obtain);
                        }
                    } else {
                        if (targetY < 0) {
                            if (mDispatchTargetTouchDown) {
                                mTarget.dispatchTouchEvent(ev);
                            } else {
                                MotionEvent obtain = MotionEvent.obtain(ev);
                                obtain.setAction(MotionEvent.ACTION_DOWN);
                                mDispatchTargetTouchDown = true;
                                mTarget.dispatchTouchEvent(obtain);
                            }
                            targetY = 0;
                        } else if (targetY > mTotalDragDistance) {
                            targetY = mTotalDragDistance;
                        } else {
                            if (mDispatchTargetTouchDown) {
                                MotionEvent obtain = MotionEvent.obtain(ev);
                                obtain.setAction(MotionEvent.ACTION_CANCEL);
                                mDispatchTargetTouchDown = false;
                                mTarget.dispatchTouchEvent(obtain);
                            }
                        }
                    }
                } else {
                    final float scrollTop = yDiff * DRAG_RATE;
                    float originalDragPercent = scrollTop / mTotalDragDistance;
                    if (originalDragPercent < 0) {
                        return false;
                    }
                    mDragPercent = Math.min(1f, Math.abs(originalDragPercent));
                    float extraOS = Math.abs(scrollTop) - mTotalDragDistance;
                    float slingshotDist = mSpinnerFinalOffset;
                    float tensionSlingshotPercent = Math.max(0,
                            Math.min(extraOS, slingshotDist * 2) / slingshotDist);
                    float tensionPercent = (float) ((tensionSlingshotPercent / 4) - Math.pow(
                            (tensionSlingshotPercent / 4), 2)) * 2f;
                    float extraMove = (slingshotDist) * tensionPercent * 2;
                    targetY = (int) ((slingshotDist * mDragPercent) + extraMove);
                    if (mRefreshView.getVisibility() != View.VISIBLE) {
                        mRefreshView.setVisibility(View.VISIBLE);
                    }
                    if (scrollTop < mTotalDragDistance) {
                        mRefreshDrawable.setText(refreshText);
                        mRefreshDrawable.setPercent(mDragPercent);
                    }
                }
                setTargetOffsetTopAndBottom(targetY - mCurrentOffsetTop, false);
                break;
            }
            case MotionEvent.ACTION_POINTER_DOWN:
                final int index = ev.getActionIndex();
                mActivePointerId = ev.getPointerId(index);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                if (mActivePointerId == INVALID_POINTER) {
                    return false;
                }
                if (mRefreshing) {
                    if (mDispatchTargetTouchDown) {
                        mTarget.dispatchTouchEvent(ev);
                        mDispatchTargetTouchDown = false;
                    }
                    return false;
                }
                pointerIndex = ev.findPointerIndex(mActivePointerId);
                final float y = ev.getY(pointerIndex);
                final float overScrollTop = (y - mInitialMotionY) * DRAG_RATE;
                mIsBeingDragged = false;
                if (overScrollTop > mTotalDragDistance) {
                    setRefreshing(true, true);
                } else {
                    mRefreshing = false;
                    animateOffsetToStartPosition();
                }
                mActivePointerId = INVALID_POINTER;
                return false;
            }
            default:
                break;
        }

        return true;
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean b) {
        boolean notRequest = (android.os.Build.VERSION.SDK_INT < 21 && mTarget instanceof AbsListView)
                || (mTarget != null && !ViewCompat.isNestedScrollingEnabled(mTarget));
        if (!notRequest) {
            super.requestDisallowInterceptTouchEvent(b);
        }
    }

    public void setDurations(int durationToStartPosition, int durationToCorrectPosition) {
        mDurationToStartPosition = durationToStartPosition;
        mDurationToCorrectPosition = durationToCorrectPosition;
    }

    private void animateOffsetToStartPosition() {
        mFrom = mCurrentOffsetTop;
        mAnimateToStartPosition.reset();
        mAnimateToStartPosition.setDuration(mDurationToStartPosition);
        mAnimateToStartPosition.setInterpolator(mDecelerateInterpolator);
        mAnimateToStartPosition.setAnimationListener(mToStartListener);
        mRefreshView.clearAnimation();
        mRefreshView.startAnimation(mAnimateToStartPosition);
    }

    private void animateOffsetToCorrectPosition() {
        mFrom = mCurrentOffsetTop;
        mAnimateToCorrectPosition.reset();
        mAnimateToCorrectPosition.setDuration(mDurationToCorrectPosition);
        mAnimateToCorrectPosition.setInterpolator(mDecelerateInterpolator);
        mAnimateToCorrectPosition.setAnimationListener(mRefreshListener);
        mRefreshView.clearAnimation();
        mRefreshView.startAnimation(mAnimateToCorrectPosition);
    }

    private final Animation mAnimateToStartPosition = new Animation() {
        @Override
        public void applyTransformation(float interpolatedTime, Transformation t) {
            moveToStart(interpolatedTime);
        }
    };

    private final Animation mAnimateToCorrectPosition = new Animation() {
        @Override
        public void applyTransformation(float interpolatedTime, Transformation t) {
            int endTarget = mSpinnerFinalOffset;
            int targetTop = (mFrom + (int) ((endTarget - mFrom) * interpolatedTime));
            int offset = targetTop - mTarget.getTop();
            setTargetOffsetTopAndBottom(offset, false /* requires update */);
        }
    };

    private void moveToStart(float interpolatedTime) {
        int targetTop = mFrom - (int) (mFrom * interpolatedTime);
        int offset = targetTop - mTarget.getTop();
        setTargetOffsetTopAndBottom(offset, false);
        mRefreshDrawable.setText("刷新中");
        mRefreshDrawable.setPercent(mDragPercent * (1 - interpolatedTime));
    }

    public void setRefreshing(boolean refreshing) {
        if (!NetworkUtil.isNetworkAvailable(getContext())) {
            ToastManager.showToast("无法联网，请检查网络链接");
        }
        if (mRefreshing != refreshing) {
            setRefreshing(refreshing, false /* notify */);
        }
    }

    private void setRefreshing(boolean refreshing, final boolean notify) {
        if (mRefreshing != refreshing) {
            mNotify = notify;
            ensureTarget();
            mRefreshing = refreshing;
            if (mRefreshing) {
                mRefreshDrawable.setText("刷新中");
                mRefreshDrawable.setPercent(1f);
                animateOffsetToCorrectPosition();
            } else {
                animateOffsetToStartPosition();
            }
        }
    }

    private Animation.AnimationListener mRefreshListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            mRefreshView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (mRefreshing) {
                mRefreshDrawable.start();
                if (mNotify) {
                    if (mListener != null) {
                        mListener.onRefresh();
                    }
                }
            } else {
                mRefreshDrawable.stop();
                mRefreshView.setVisibility(View.GONE);
                animateOffsetToStartPosition();
            }
            mCurrentOffsetTop = mTarget.getTop();
        }
    };
    private Animation.AnimationListener mToStartListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            mRefreshDrawable.stop();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            mRefreshDrawable.stop();
            mRefreshView.setVisibility(View.GONE);
            mCurrentOffsetTop = mTarget.getTop();
        }
    };

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = ev.getActionIndex();
        final int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mActivePointerId = ev.getPointerId(newPointerIndex);
        }
    }

    private float getMotionEventY(MotionEvent ev, int activePointerId) {
        final int index = ev.findPointerIndex(activePointerId);
        if (index < 0) {
            return -1;
        }
        return ev.getY(index);
    }

    private void setTargetOffsetTopAndBottom(int offset, boolean frontRefresh) {
        if (frontRefresh) {
            mRefreshView.bringToFront();
        }
        mTarget.offsetTopAndBottom(offset);
        mCurrentOffsetTop = mTarget.getTop();
        mRefreshDrawable.offsetTopAndBottom(offset);
    }

    private boolean canChildScrollUp() {
        if (mTarget instanceof ListView) {
            return ListViewCompat.canScrollList((ListView) mTarget, -1);
        }
        return mTarget.canScrollVertically(-1);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mTarget == null) {
            ensureTarget();
        }
        if (mTarget == null) {
            return;
        }
        int height = getMeasuredHeight();
        int width = getMeasuredWidth();
        final int childLeft = getPaddingLeft();
        final int childTop = getPaddingTop();
        final int childWidth = width - getPaddingLeft() - getPaddingRight();
        final int childHeight = height - getPaddingTop() - getPaddingBottom();

        final View child = mTarget;
        child.layout(childLeft, childTop + child.getTop(), childLeft + childWidth, childTop + childHeight + child.getTop());
        mRefreshView.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getContext().getResources().getDisplayMetrics());
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }

    public interface OnRefreshListener {
        void onRefresh();
    }

    public String getRefreshText() {
        return refreshText;
    }

    public void setRefreshText(String refreshText) {
        this.refreshText = refreshText;
    }

    private void finishSpinner(float overScrollTop) {
        if (overScrollTop > mTotalDragDistance) {
            setRefreshing(true, true);
        } else {
            mRefreshing = false;
            animateOffsetToStartPosition();
        }
        mActivePointerId = INVALID_POINTER;
    }

    private void moveSpinner(float scrollTop) {
        float originalDragPercent = scrollTop / mTotalDragDistance;
        mDragPercent = Math.min(1f, Math.abs(originalDragPercent));
        float extraOS = Math.abs(scrollTop) - mTotalDragDistance;
        float slingshotDist = mSpinnerFinalOffset;
        float tensionSlingshotPercent = Math.max(0,
                Math.min(extraOS, slingshotDist * 2) / slingshotDist);
        float tensionPercent = (float) ((tensionSlingshotPercent / 4) - Math.pow(
                (tensionSlingshotPercent / 4), 2)) * 2f;
        float extraMove = (slingshotDist) * tensionPercent * 2;
        int targetY = (int) ((slingshotDist * mDragPercent) + extraMove);
        if (mRefreshView.getVisibility() != View.VISIBLE) {
            mRefreshView.setVisibility(View.VISIBLE);
        }
        if (scrollTop < mTotalDragDistance) {
            mRefreshDrawable.setText(refreshText);
            mRefreshDrawable.setPercent(mDragPercent);
        }
        setTargetOffsetTopAndBottom(targetY - mCurrentOffsetTop, false);
    }
    // NestedScrollingChild

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mNestedScrollingChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return mNestedScrollingChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return mNestedScrollingChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        mNestedScrollingChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mNestedScrollingChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
                                        int dyUnconsumed, int[] offsetInWindow) {
        return mNestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return mNestedScrollingChildHelper.dispatchNestedPreScroll(
                dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mNestedScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mNestedScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX,
                                    float velocityY) {
        return dispatchNestedPreFling(velocityX, velocityY);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY,
                                 boolean consumed) {
        return dispatchNestedFling(velocityX, velocityY, consumed);
    }

    // NestedScrollingParent

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return isEnabled() && !mReturningToStart && !mRefreshing
                && (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes);
        startNestedScroll(axes & ViewCompat.SCROLL_AXIS_VERTICAL);
        mTotalUnconsumed = 0;
        mNestedScrollInProgress = true;
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if (dy > 0 && mTotalUnconsumed > 0) {
            if (dy > mTotalUnconsumed) {
                consumed[1] = dy - (int) mTotalUnconsumed;
                mTotalUnconsumed = 0;
            } else {
                mTotalUnconsumed -= dy;
                consumed[1] = dy;
            }
            moveSpinner(mTotalUnconsumed);
        }

        final int[] parentConsumed = mParentScrollConsumed;
        if (dispatchNestedPreScroll(dx - consumed[0], dy - consumed[1], parentConsumed, null)) {
            consumed[0] += parentConsumed[0];
            consumed[1] += parentConsumed[1];
        }
    }

    @Override
    public int getNestedScrollAxes() {
        return mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    @Override
    public void onStopNestedScroll(View target) {
        mNestedScrollingParentHelper.onStopNestedScroll(target);
        mNestedScrollInProgress = false;
        if (mTotalUnconsumed > 0) {
            finishSpinner(mTotalUnconsumed);
            mTotalUnconsumed = 0;
        }
        stopNestedScroll();
    }

    @Override
    public void onNestedScroll(final View target, final int dxConsumed, final int dyConsumed,
                               final int dxUnconsumed, final int dyUnconsumed) {
        dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed,
                mParentOffsetInWindow);
        final int dy = dyUnconsumed + mParentOffsetInWindow[1];
        if (dy < 0 && !canChildScrollUp()) {
            mTotalUnconsumed += Math.abs(dy);
            moveSpinner(mTotalUnconsumed);
        }
    }
}