package com.lukou.publishervideo.presenter;


import com.lukou.base.ui.BasePresenter;
import com.lukou.base.ui.BaseView;

public interface HomeActivityContract {

    interface View extends BaseView {

    }

    abstract class Presenter extends BasePresenter<View> {
        public Presenter(View rootView) {
            super(rootView);
        }
    }

}
