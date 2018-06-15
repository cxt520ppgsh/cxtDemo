package com.lukou.publishervideo.di.module;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lidao.httpmodule.http.BaseHttpService;
import com.lidao.httpmodule.http.base.HttpParams;
import com.lidao.httpmodule.http.base.HttpResult;
import com.lukou.publishervideo.BuildConfig;
import com.lukou.publishervideo.app.MainApplication;
import com.lukou.publishervideo.mvp.home.dagger.scope.HomeActivityScope;
import com.lukou.publishervideo.utils.netUtils.ApiFactory;
import com.lukou.publishervideo.utils.netUtils.ApiService;
import com.lukou.publishervideo.utils.netUtils.HeaderInterceptor;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by cxt on 2018/6/13.
 */

@Module
public class NetModule {


    public NetModule() {
    }

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    ApiService apiService() {
        final HttpParams httpParams = new HttpParams.Builder(BuildConfig.baseUrl)
                .interceptor(new HeaderInterceptor())
                //.interceptor(new HttpsScopeInterceptor())
                .build();
        return BaseHttpService.getRetrofit(MainApplication.instance(), httpParams, BuildConfig.DEBUG).create(ApiService.class);
    }
}

