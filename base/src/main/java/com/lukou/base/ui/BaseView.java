package com.lukou.base.ui;

import rx.Subscription;

public interface BaseView {
    void addSubscription(Subscription subscription);
    void initView();
}
