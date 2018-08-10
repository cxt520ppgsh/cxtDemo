package com.lukou.base.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.lukou.base.utils.EventBusUtils;

import javax.inject.Inject;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public abstract class BaseActivity<P extends BasePresenter> extends Activity implements BaseView {
    CompositeSubscription mCompositeSubscription;

    @Inject
    protected P mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View contentView = setContentView();
        setContentView(contentView);
        onBindActivityView(contentView);
        mCompositeSubscription = new CompositeSubscription();
        ComponentInject();
        mPresenter.onStart();
        EventBusUtils.register(this);
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        mPresenter = null;
        if (mCompositeSubscription != null && !mCompositeSubscription.isUnsubscribed()) {
            mCompositeSubscription.unsubscribe();
            mCompositeSubscription = null;
        }
        EventBusUtils.unregister(this);
        super.onDestroy();
    }

    protected abstract void ComponentInject();

    protected abstract View setContentView();
    protected void onBindActivityView(View view) {

    }

    @Override
    public void addSubscription(Subscription subscription) {
        if (mCompositeSubscription != null && !mCompositeSubscription.isUnsubscribed()) {
            mCompositeSubscription.add(subscription);
        }
    }


}
