package com.lukou.publishervideo.mvp.home.p;


import com.lukou.publishervideo.base.BasePresenter;
import com.lukou.publishervideo.mvp.home.HomeActivityContract;

import javax.inject.Inject;

public class HomeActivityPresenter extends BasePresenter<HomeActivityContract.View> {

    @Inject
    public HomeActivityPresenter(HomeActivityContract.View rootView) {
        super(rootView);
    }

}
