package com.flyaudio.packagemanager.model.net;

import com.flyaudio.base.BuildConfig;
import com.flyaudio.base.application.BaseApplication;
import com.flyaudio.base.http.HeaderInterceptor;
import com.flyaudio.base.http.HttpsScopeInterceptor;
import com.flyaudio.packagemanager.model.bean.Asiginer;
import com.flyaudio.packagemanager.model.bean.PublisherVideo;
import com.lidao.httpmodule.http.BaseHttpService;
import com.lidao.httpmodule.http.base.HttpParams;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;


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



    /**
     * download
     * */

    private static final int DEFAULT_TIMEOUT = 10;
    private static final String TAG = "RetrofitClient";

    private ApiService downloadApiService;

    private OkHttpClient okHttpClient;

    public static String baseUrl = ApiService.BASE_URL;

    private static ApiFactory sIsntance;

    public static ApiFactory instance() {
        if (sIsntance == null) {
            synchronized (ApiFactory.class) {
                if (sIsntance == null) {
                    sIsntance = new ApiFactory();
                }
            }
        }
        return sIsntance;
    }
    private ApiFactory() {
        super(BaseApplication.instance());
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    public ApiService getApiService(){
        return apiService;
    }
}


