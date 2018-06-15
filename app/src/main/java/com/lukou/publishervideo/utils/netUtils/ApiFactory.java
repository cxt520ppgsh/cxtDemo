package com.lukou.publishervideo.utils.netUtils;

import android.content.Context;

import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import com.lidao.httpmodule.http.BaseHttpService;
import com.lidao.httpmodule.http.base.HttpParams;
import com.lukou.publishervideo.BuildConfig;
import com.lukou.publishervideo.app.MainApplication;
import com.lukou.publishervideo.bean.Asiginer;
import com.lukou.publishervideo.bean.PublisherVideo;
import com.lukou.publishervideo.mvp.home.dagger.scope.HomeActivityScope;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;


/**
 * Created by cxt on 2018/6/14.
 */
public class ApiFactory extends BaseHttpService {

    private volatile static ApiService apiService;

    @Inject
    public ApiFactory(ApiService apiService) {
        super(MainApplication.instance());
        this.apiService = apiService;
    }

    <T> Observable.Transformer<T, T> lift() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Response<ResponseBody>> test() {
        return apiService.test();
    }

    public Observable<KuaishouHttpResult<PublisherVideo>> getPublisherVideo(
            int page,
            String keyWord,
            String sort_field,
            int delete_type,
            int video_type,
            String start_date,
            String end_date,
            int delivery_level,
            String asigner)

    {
        return apiService.getPublisherVideo(page,
                keyWord,
                sort_field,
                delete_type,
                video_type,
                start_date,
                end_date,
                delivery_level,
                asigner).compose(lift());
    }

    public Observable<KuaishouHttpResult<Asiginer>> getAsigner() {
        return apiService.getAsigner().compose(lift());
    }

}


