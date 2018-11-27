package com.flyaudio.publishervideo.view.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.flyaudio.base.utils.ScreenUtil;
import com.flyaudio.publishervideo.view.adapter.HomeRvAdapter;

/**
 * Created by cxt on 2018/6/18.
 */

public class VideoRecycleView extends RecyclerView {
    private float ACTION_Y = 0;
    private float dowxY = 0;
    private float dy = 0;
    private static int currentPosition = 0;
    private static int oldPosition = 0;
    private HomeRvAdapter homeRvAdapter;
    LinearLayoutManager layoutMgr;
    //当 当前Item的视频未打标签是设为false阻止上滑到下一个视频，否则设为true
    private boolean canScrollToNext = false;
    private boolean isToasted = false;
    private CustomLinearLayoutManager linearLayoutManager;

    public VideoRecycleView(Context context) {
        this(context, null, 0);
    }

    public VideoRecycleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        ACTION_Y = (float) (ScreenUtil.getScreenH(context) * 0.3);
        init();
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

    private void init() {
        linearLayoutManager = new CustomLinearLayoutManager(getContext());
        setRecyclerListener(holder -> {
            if (holder instanceof HomeRvAdapter.HomeRvItemViewHolder) {
                ((HomeRvAdapter.HomeRvItemViewHolder) holder).destroyVideoView();
            }
        });

        addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                homeRvAdapter = (HomeRvAdapter) getAdapter();
                if (newState == SCROLL_STATE_IDLE) {
                    //只有当前Item播放视频
                    if (oldPosition != getCurrentPosition()) {
                        if (getCurrentPosition() < homeRvAdapter.getList().size()) {
                            if (homeRvAdapter.getList().get(getCurrentPosition()) != null) {
                                View view = getLayoutManager().findViewByPosition((getCurrentPosition()));
                                if (view != null) {
                                    if (getChildViewHolder(view) instanceof HomeRvAdapter.HomeRvItemViewHolder) {
                                        HomeRvAdapter.HomeRvItemViewHolder homeRvItemViewHolder = (HomeRvAdapter.HomeRvItemViewHolder) getChildViewHolder(view);
                                        homeRvItemViewHolder.setVideoURL(homeRvAdapter.getList().get(getCurrentPosition()));
                                        oldPosition = getCurrentPosition();
                                    }
                                }
                            }
                        }
                    }

                } else if (newState == SCROLL_STATE_DRAGGING) {
                    // GSYVideoManager.onPause();
                }
            }

        });
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
                linearLayoutManager.setScrollEnabled(true);
                break;
            case MotionEvent.ACTION_MOVE:
                dy = e.getY() - dowxY;
                //未打标签处理
                if (!canScrollToNext && dy < 0) {
                    linearLayoutManager.setScrollEnabled(false);
                    if (!isToasted) {
                        isToasted = true;
                        Toast.makeText(getContext(), "先打标签才可以浏览下个视频", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }

                break;
            case MotionEvent.ACTION_UP:
                isToasted = false;
                //未打标签处理
                if (!canScrollToNext && dy < 0) {
                    dy = 0;
                    return true;
                }

                //swiperefreshlayout处理
                if (!canScrollVertically(-1)) {
                    dy = 0;
                    return super.onTouchEvent(e);
                }

                //松手自动滑动到下个item
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

    public class CustomLinearLayoutManager extends LinearLayoutManager {
        private boolean isScrollEnabled = true;

        public CustomLinearLayoutManager(Context context) {
            super(context);
        }

        public void setScrollEnabled(boolean flag) {
            this.isScrollEnabled = flag;
        }

        @Override
        public boolean canScrollVertically() {

            return isScrollEnabled && super.canScrollVertically();
        }
    }

}
