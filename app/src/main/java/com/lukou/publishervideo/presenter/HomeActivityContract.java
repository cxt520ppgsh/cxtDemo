package com.lukou.publishervideo.presenter;


import com.google.android.exoplayer2.C;
import com.lukou.publishervideo.base.BasePresenter;
import com.lukou.publishervideo.base.BaseView;

public interface HomeActivityContract {

    interface View extends BaseView {
        void initView();

        void initAsigner();

    }

    abstract class Presenter extends BasePresenter<View> {
        public Presenter(View rootView) {
            super(rootView);
        }

        abstract void initAsigner();
    }

}
