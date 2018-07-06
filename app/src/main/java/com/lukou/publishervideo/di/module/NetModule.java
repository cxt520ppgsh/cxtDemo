package com.lukou.publishervideo.di.module;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lidao.httpmodule.http.BaseHttpService;
import com.lidao.httpmodule.http.base.HttpParams;
import com.lukou.publishervideo.BuildConfig;
import com.lukou.publishervideo.app.MainApplication;
import com.lukou.publishervideo.model.net.ApiService;
import com.lukou.publishervideo.model.net.HeaderInterceptor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

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

