package com.lukou.publishervideo.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.intersection.listmodule.adapter.RecyclerViewAdapter;
import com.intersection.listmodule.viewholder.BaseViewHolder;
import com.intersection.viewmodule.display.RetryLoadListener;
import com.lidao.httpmodule.http.HttpException;
import com.lukou.publishervideo.R;
import com.lukou.publishervideo.databinding.PageErrorLayoutBinding;
import com.lukou.publishervideo.widget.swipeRefresh.MySwipeRefreshLayout;

/**
 * @author sunbinqiang
 */
public abstract class ListRecyclerViewAdapter<T> extends RecyclerViewAdapter<T> {
    private MySwipeRefreshLayout swipeRefreshLayout;

    public void setSwipeRefreshLayout(@NonNull MySwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
        swipeRefreshLayout.setOnRefreshListener(() -> reset(false));
    }

    @Override
    protected void onRequestFinished() {
        super.onRequestFinished();
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.postDelayed(() -> swipeRefreshLayout.setRefreshing(false), 200);
        }
        if (!TextUtils.isEmpty(getRefreshMsg())) {
            swipeRefreshLayout.setRefreshText(getRefreshMsg());
        }
    }

    @Override
    protected BaseViewHolder onCreatePageErrorViewHolder(Context context, ViewGroup parent) {
        YxPageErrorViewHolder pageErrorViewHolder = new YxPageErrorViewHolder(context, parent);
        pageErrorViewHolder.setRetry(this::retry);
        if(this.getThrowable() instanceof HttpException){
            pageErrorViewHolder.setHttpException((HttpException) this.getThrowable());
        }
        return pageErrorViewHolder;
    }

    @Override
    protected BaseViewHolder onCreateEmptyViewHolder(Context context, ViewGroup parent) {
        return new BaseViewHolder(context, parent, R.layout.list_empty_page_viewholder);
    }

    @Override
    protected BaseViewHolder onCreateItemErrorViewHolder(Context context, ViewGroup parent) {
        return super.onCreateItemErrorViewHolder(context, parent);
    }

    @Override
    protected BaseViewHolder onCreatePageLoadingViewHolder(Context context, ViewGroup parent) {
        return super.onCreatePageLoadingViewHolder(context, parent);
    }

    @Override
    protected BaseViewHolder onCreateEndItemViewHolder(Context context, ViewGroup parent) {
        return new BaseViewHolder(context, parent, R.layout.list_bottom_item_viewholder);
    }

    public class YxPageErrorViewHolder extends BaseViewHolder<PageErrorLayoutBinding> implements View.OnClickListener {

        public RetryLoadListener retryLoadListener;

        private YxPageErrorViewHolder(View itemView) {
            super(itemView);
        }

        public YxPageErrorViewHolder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.page_error_layout);
            binding.retry.setOnClickListener(this);
            binding.feedbackTv.setOnClickListener(v -> {

            });
        }

        public void setRetry(RetryLoadListener retryCallback) {
            this.retryLoadListener = retryCallback;
        }

        public void setHttpException(HttpException httpException){
            binding.setHttpException(httpException);
        }

        @Override
        public void onClick(View v) {
            if (retryLoadListener != null) {
                retryLoadListener.retryLoad();
            }
        }
    }

}
