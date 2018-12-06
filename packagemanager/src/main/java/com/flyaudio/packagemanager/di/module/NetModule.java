package com.flyaudio.packagemanager.di.module;

import com.flyaudio.base.BuildConfig;
import com.flyaudio.base.application.BaseApplication;
import com.flyaudio.base.http.HeaderInterceptor;
import com.flyaudio.base.http.HttpsScopeInterceptor;
import com.flyaudio.packagemanager.di.scope.PackageManagerApplicationScope;
import com.flyaudio.packagemanager.model.net.ApiService;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lidao.httpmodule.http.base.HttpClient;
import com.lidao.httpmodule.http.base.HttpParams;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by cxt on 2018/6/13.
 */

@Module
public class NetModule {


    public NetModule() {
    }

    @Provides
    @PackageManagerApplicationScope
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    @Provides
    @PackageManagerApplicationScope
    ApiService apiService() {
        HttpParams httpParams = new HttpParams.Builder(BuildConfig.baseUrl)
                .interceptor(new HeaderInterceptor())
                .interceptor(new HttpsScopeInterceptor())
                .build();
        Gson gson = (new GsonBuilder()).setLenient().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
         return new Retrofit.Builder()
                .client(HttpClient.get(BaseApplication.instance(), httpParams, BuildConfig.DEBUG))
                .baseUrl(httpParams.getBaseUrl()).addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
                .create(ApiService.class);
    }
}

