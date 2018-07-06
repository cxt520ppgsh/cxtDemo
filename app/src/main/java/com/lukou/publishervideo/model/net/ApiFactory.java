package com.lukou.publishervideo.model.net;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import com.lidao.httpmodule.http.BaseHttpService;
import com.lidao.httpmodule.http.base.HttpParams;
import com.lukou.publishervideo.BuildConfig;
import com.lukou.publishervideo.app.MainApplication;
import com.lukou.publishervideo.model.bean.Asiginer;
import com.lukou.publishervideo.model.bean.PublisherVideo;

import javax.inject.Inject;


/**
 * Created by cxt on 2018/6/14.
 */
public class ApiFactory extends BaseHttpService {

    private volatile static ApiService apiService;
    private volatile static ApiFactory apiFactory;

    @Inject
    public ApiFactory(ApiService apiService) {
        super(MainApplication.instance());
        this.apiService = apiService;
    }

    public static synchronized ApiFactory getInstance() {
        if (apiFactory == null) {
            HttpParams httpParams = new HttpParams.Builder(BuildConfig.baseUrl)
                    .interceptor(new HeaderInterceptor())
                    //.interceptor(new HttpsScopeInterceptor())
                    .build();
            apiService = BaseHttpService.getRetrofit(MainApplication.instance(), httpParams, BuildConfig.DEBUG).create(ApiService.class);
            apiFactory = new ApiFactory(apiService);
        }
        return apiFactory;
    }


    <T> Observable.Transformer<T, T> lift() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<KuaishouHttpResult<PublisherVideo>> getPublisherVideo(
            int delete_type,
            int video_type,
            String asigner)

    {
        return apiService.getPublisherVideo(
                delete_type,
                video_type,
                asigner).compose(lift());
    }

    public Observable<KuaishouHttpResult> setTag(String fid, int type) {
        return apiService.setTag(fid, type).compose(lift());
    }

    public Observable<KuaishouHttpResult<Asiginer>> getAsigner() {
        return apiService.getAsigner().compose(lift());
    }

}


