package com.lukou.publishervideo.mvp.home;


import com.lukou.publishervideo.base.BaseView;
import com.lukou.publishervideo.bean.PublisherVideo;

import java.util.List;

public interface HomeActivityContract {

    interface View extends BaseView {
        void initView();
        void refresh(List<PublisherVideo> publisherVideos);
    }

}
