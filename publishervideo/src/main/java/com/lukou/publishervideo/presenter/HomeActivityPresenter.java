package com.lukou.publishervideo.presenter;


import javax.inject.Inject;

public class HomeActivityPresenter extends HomeActivityContract.Presenter {
    HomeActivityContract.View rootView;

    @Inject
    public HomeActivityPresenter(HomeActivityContract.View rootView) {
        super(rootView);
        this.rootView = rootView;
    }

    @Override
    public void onStart() {
        rootView.initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
