package com.flyaudio.publishervideo.presenter;


import com.flyaudio.base.ui.BasePresenter;
import com.flyaudio.base.ui.BaseView;

public interface HomeActivityContract {

    interface View extends BaseView {

    }

    abstract class Presenter extends BasePresenter<View> {
        public Presenter(View rootView) {
            super(rootView);
        }
    }

}
