package com.lukou.publishervideo.base;

public class BasePresenter<V extends BaseView>  implements Presenter {
    protected V mRootView;

    public BasePresenter(V rootView) {
        this.mRootView = rootView;
        onStart();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onDestroy() {
        mRootView = null ;
    }
}
