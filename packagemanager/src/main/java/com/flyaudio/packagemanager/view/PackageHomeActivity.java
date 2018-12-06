package com.flyaudio.packagemanager.view;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.flyaudio.base.bean.EventBusMessage;
import com.flyaudio.base.ui.BaseActivity;
import com.flyaudio.packagemanager.R;
import com.flyaudio.packagemanager.app.PackageManagerApplication;
import com.flyaudio.packagemanager.databinding.ActivityPackageHomeBinding;
import com.flyaudio.packagemanager.di.component.DaggerActivityComponent;
import com.flyaudio.packagemanager.di.module.ActivityModule;
import com.flyaudio.packagemanager.model.net.ApiFactory;
import com.flyaudio.packagemanager.model.net.download.DownloadCallBack;
import com.flyaudio.packagemanager.model.pkg.InstallListener;
import com.flyaudio.packagemanager.model.pkg.PkgInstallManager;
import com.flyaudio.packagemanager.presenter.home.HomeActivityContract;
import com.flyaudio.packagemanager.presenter.home.HomeActivityPresenter;


import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

public class PackageHomeActivity extends BaseActivity<HomeActivityPresenter> implements HomeActivityContract.View {
    public static final String HOMEACTIVITY_INIT_ASIGNER_TV = "HOMEACTIVITY_INIT_ASIGNER_TV";
    public static final String HOMEACTIVITY_ADD_SUBSCRIBTION = "HOMEACTIVITY_ADD_SUBSCRIBTION";
    @Inject
    public ApiFactory apiFactory;
    @Inject
    public SharedPreferences sharedPreferences;
    boolean pause = false;
    String url = "Wandoujia_423614_web_seo_baidu_homepage.apk";

    ActivityPackageHomeBinding binding;

    @Override
    protected void ComponentInject() {
        DaggerActivityComponent
                .builder()
                .packageManagerComponent(PackageManagerApplication.instance().getPackageComponent())
                .activityModule(new ActivityModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected View setContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_package_home, null, false);
    }

    @Override
    protected void onBindActivityView(View view) {
        super.onBindActivityView(view);
        binding = DataBindingUtil.bind(view);
    }

    @Override
    public void initView() {
        PkgInstallManager.getInstance().install(url,
                new DownloadCallBack() {
                    @Override
                    public void onProgress(int progress) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (binding.installPer != null) {
                                    binding.installPer.setText(progress+"");
                                }
                            }
                        });
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(String msg) {

                    }
                },
                new InstallListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onFinish() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (binding.installPer != null) {
                                    binding.installPer.setText("安装开始");
                                }
                            }
                        });
                    }

                    @Override
                    public void onFail(Throwable e) {
                        Log.e("asd", e.getMessage());
                    }
                });
        binding.installPer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!pause) {
                    pause = true;
                    PkgInstallManager.getInstance().pause(url);
                } else {
                    pause = false;
                    PkgInstallManager.getInstance().reStart(url);
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onMessage(EventBusMessage event) {
        switch (event.getCode()) {
            case HOMEACTIVITY_INIT_ASIGNER_TV:

                break;

            default:
                break;
        }
    }
}
