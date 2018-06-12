package com.lukou.publishervideo.mvp.home;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;

import com.lukou.publishervideo.R;
import com.lukou.publishervideo.base.BaseActivity;
import com.lukou.publishervideo.di.component.AppComponent;
import com.lukou.publishervideo.mvp.home.dagger.DaggerHomeActivityComponent;
import com.lukou.publishervideo.mvp.home.dagger.HomeActivityModule;
import com.lukou.publishervideo.mvp.home.p.HomeActivityPresenter;
import com.lukou.publishervideo.utils.netUtils.ApiService;

import java.io.IOException;

import javax.inject.Inject;

import io.vov.vitamio.Vitamio;
import io.vov.vitamio.utils.Log;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class HomeActivity extends BaseActivity<HomeActivityPresenter> implements HomeActivityContract.View {

    @Inject
    protected ApiService apiService;


    @Override
    protected void ComponentInject(AppComponent appComponent) {
        DaggerHomeActivityComponent
                .builder()
                .appComponent(appComponent)
                .homeActivityModule(new HomeActivityModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_home, null, false);
    }

    @Override
    protected void initData() {
        if (Vitamio.initialize(this)) {
            VideoView videoView = (VideoView) findViewById(R.id.vitamio);
            videoView.setVideoURI(Uri.parse("http://gxmov.a.yximgs.com/upic/2018/06/13/18/BMjAxODA2MTMxODQxMjRfMzg4MTU5MzkyXzY2NTk3MTM2NjJfMV8z_b_B1a0029eee7d1c729116214a489e4448e.mp4?tag=1-1528886806-p-0-5ixpsk3aly-6cd98cecb138cd39"));
            MediaController controller = new MediaController(this);
            videoView.setMediaController(controller);
            videoView.start();
        }
    }

    void retrofitTest(){
        apiService.test().
                observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<Response<ResponseBody>>() {
                    @Override
                    public void call(Response<ResponseBody> responseBodyResponse) {
                        try {
                            String s = new String(responseBodyResponse.body().bytes());
                            Log.d("dagger", s);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

}
