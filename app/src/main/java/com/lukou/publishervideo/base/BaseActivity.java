package com.lukou.publishervideo.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.lukou.publishervideo.app.MainApplication;
import com.lukou.publishervideo.di.component.AppComponent;
import com.lukou.publishervideo.utils.EventBusUtils;

import javax.inject.Inject;

import butterknife.ButterKnife;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public abstract class BaseActivity<P extends BasePresenter> extends Activity implements BaseView {
    CompositeSubscription mCompositeSubscription;

    @Inject
    protected P mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setContentView());
        ButterKnife.bind(this);
        mCompositeSubscription = new CompositeSubscription();
        ComponentInject(((MainApplication) getApplication()).getAppComponent());
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

    protected abstract void ComponentInject(AppComponent appComponent);

    protected abstract View setContentView();

    @Override
    public void addSubscription(Subscription subscription) {
        if (mCompositeSubscription != null && !mCompositeSubscription.isUnsubscribed()) {
            mCompositeSubscription.add(subscription);
        }
    }


}
