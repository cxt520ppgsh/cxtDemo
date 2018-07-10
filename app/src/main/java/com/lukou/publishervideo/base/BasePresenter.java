package com.lukou.publishervideo.base;

import android.view.View;

import rx.Subscription;

public abstract class BasePresenter<V extends BaseView>  implements Presenter {
    protected V mRootView;

    public BasePresenter(V rootView) {
        this.mRootView = rootView;
    }

    public abstract void onStart();
    @Override
    public void onDestroy() {
        mRootView = null ;
    }

}
