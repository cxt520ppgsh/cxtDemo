package com.flyaudio.base.ui;

import rx.Subscription;

public interface BaseView {
    void addSubscription(Subscription subscription);
    void initView();
}
