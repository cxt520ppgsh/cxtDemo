package com.lukou.publishervideo.base;

import rx.Subscription;

public interface BaseView {
    void addSubscription(Subscription subscription);
    void initView();
}
