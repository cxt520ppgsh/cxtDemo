package com.lukou.publishervideo.model.net;

import rx.Observable;

import com.lidao.httpmodule.http.BaseHttpService;
import com.lidao.httpmodule.http.base.HttpParams;
import com.lukou.base.BuildConfig;
import com.lukou.base.application.BaseApplication;
import com.lukou.base.http.HeaderInterceptor;
import com.lukou.base.http.HttpsScopeInterceptor;
import com.lukou.publishervideo.model.bean.Asiginer;
import com.lukou.publishervideo.model.bean.PublisherVideo;

import java.util.List;

import javax.inject.Inject;


/**
 * Created by cxt on 2018/6/14.
 */
public class ApiFactory extends BaseHttpService {

    private volatile static ApiService apiService;
    private volatile static ApiFactory apiFactory;

    @Inject
    public ApiFactory(ApiService apiService) {
        super(BaseApplication.instance());
        this.apiService = apiService;
    }

    public static synchronized ApiFactory getInstance() {
        if (apiFactory == null || apiService == null) {
            HttpParams httpParams = new HttpParams.Builder(BuildConfig.baseUrl)
                    .interceptor(new HeaderInterceptor())
                    .interceptor(new HttpsScopeInterceptor())
                    .build();
            apiService = getRetrofit(BaseApplication.instance(), httpParams, BuildConfig.DEBUG).create(ApiService.class);
            apiFactory = new ApiFactory(apiService);
        }
        return apiFactory;
    }

    public Observable<List<PublisherVideo>> getPublisherVideo(
            int delete_type,
            int video_type,
            String asigner)

    {
        return apiService.getPublisherVideo(
                delete_type,
                video_type,
                asigner).compose(lifts());
    }

    public Observable setTag(String fid, int type) {
        return apiService.setTag(fid, type).compose(lifts());
    }

    public Observable<List<Asiginer>> getAsigner() {
        return apiService.getAsigner().compose(lifts());
    }

}


