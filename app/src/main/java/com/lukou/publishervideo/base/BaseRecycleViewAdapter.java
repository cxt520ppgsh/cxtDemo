package com.lukou.publishervideo.base;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lukou.publishervideo.R;
import com.lukou.publishervideo.model.bean.PublisherVideo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxt on 2018/7/6.
 */

public abstract class BaseRecycleViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    private boolean isRefresh = false;
    private boolean isLoading = false;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = -1;
    private SwipeRefreshLayout swipeRefreshLayout;
    Context context;
    List<T> list = new ArrayList<>();

    public BaseRecycleViewAdapter(Context context) {
        this.context = context;
    }

    public void setSwipeRefreshLayout(@NonNull SwipeRefreshLayout swipeRefreshLayout, RecyclerView recyclerView) {
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.recyclerView = recyclerView;
        this.swipeRefreshLayout.setColorScheme(R.color.colorPrimary);
        this.swipeRefreshLayout.setOnRefreshListener(() -> {
            if (!isRefresh) {
                isRefresh = true;
                refresh();
            }
        });
        linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //下拉刷新 上拉加载更多
                {
                    int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                    if (lastVisibleItemPosition + 1 == getItemCount()) {
                        boolean isRefreshing = swipeRefreshLayout.isRefreshing();
                        if (isRefreshing) {
                            notifyItemRemoved(getItemCount());
                            return;
                        }
                        if (!isLoading) {
                            isLoading = true;
                            loadMore();
                        }
                    }
                }
            }
        });
    }

    public void setList(List list) {
        this.list = list;
        if (!isRefresh) {
            notifyDataSetChanged();
            onRefreshFinish();
        } else {
            new Handler().postDelayed(() -> {
                swipeRefreshLayout.setRefreshing(false);
                isRefresh = false;
                notifyDataSetChanged();
                onRefreshFinish();
            }, 500);
        }
    }

    public void addList(List<T> newList) {
        for (T listItem : newList) {
            list.add(listItem);
        }
        if (isLoading) {
            new Handler().postDelayed(() -> {
                isLoading = false;
                notifyItemRemoved(getItemCount());
                onLoadMoreFinish();
            }, 500);
        } else {
            notifyDataSetChanged();
        }

    }

    public List<T> getList() {
        return list;
    }

    private RecyclerView.ViewHolder getViewHolder(int position) {
        if (recyclerView == null || recyclerView.getAdapter() == null || recyclerView.getAdapter().getItemCount() == 0) {
            return null;
        }
        int count = recyclerView.getAdapter().getItemCount();
        if (position < 0 || position > count - 1) {
            return null;
        }
        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
        if (viewHolder == null) {
            RecyclerView.RecycledViewPool pool = recyclerView.getRecycledViewPool();
            int recycleViewcount = pool.getRecycledViewCount(0);
            viewHolder = pool.getRecycledView(0);
            try {
                pool.putRecycledView(viewHolder);
            } catch (Exception e) {

            }
        }
        return viewHolder;

    }

    @Override
    public int getItemCount() {
        return list.size() == 0 ? 0 : list.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            return createItemViewholder(parent);
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(context).inflate(R.layout.activity_home_rv_foot, parent,
                    false);
            return new FootViewHolder(view);
        } else {
            return null;
        }
    }

    public abstract RecyclerView.ViewHolder createItemViewholder(ViewGroup parent);

    public abstract void refresh();

    public abstract void loadMore();

    public void onRefreshFinish() {

    }

    public void onLoadMoreFinish() {

    }

    private class FootViewHolder extends RecyclerView.ViewHolder {

        public FootViewHolder(View view) {
            super(view);
        }
    }
}

