package com.lukou.publishervideo.model.net;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lidao.httpmodule.http.BaseHttpService;
import com.lidao.httpmodule.http.HttpException;
import com.lidao.httpmodule.http.base.HttpClient;
import com.lidao.httpmodule.http.base.HttpParams;
import com.lidao.httpmodule.http.base.HttpResult;
import com.lidao.httpmodule.http.lisener.IHttpService;
import com.lidao.httpmodule.http.utils.NetworkUtil;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static rx.schedulers.Schedulers.io;

/**
 * Created by cxt on 2018/7/9.
 */

public class BasePandaHackerHttpService implements IHttpService {
    private Observable.Transformer transformer;
    private volatile static Retrofit retrofit;
    private Context context;

    public BasePandaHackerHttpService(Context context) {
        this.context = context;
        this.transformer = new RetrofitTransformer(context);
    }

    <T> Observable.Transformer<PandaHackerHttpResult<T>, List<T>> listReusltLift() {
        return (Observable.Transformer<PandaHackerHttpResult<T>, List<T>>) transformer;
    }


    <T> Observable.Transformer<T, T> notListReusltLift() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).onErrorReturn((Func1<Throwable, T>) throwable -> {
                    if (!NetworkUtil.isNetworkAvailable(context)) {
                        throw new HttpException(HttpException.NETWORK_ERROR, "哎呀,熊猫没有连接网络");
                    } else {
                        throw new HttpException(HttpException.ERROR, "请检查网络,或稍后再试");
                    }
                });
    }

    public static Retrofit getRetrofit(Context context, @NonNull HttpParams httpParams, boolean isDebugMode) {
        if (retrofit == null) {
            synchronized (BasePandaHackerHttpService.class) {
                if (retrofit == null) {
                    Gson gson = new GsonBuilder()
                            .setLenient()
                            .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    retrofit = new Retrofit.Builder()
                            .client(HttpClient.get(context, httpParams, isDebugMode))
                            .baseUrl(httpParams.getBaseUrl())
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }

    private static class HttpResultFunc<T> implements Func1<PandaHackerHttpResult<T>, List<T>> {

        @Override
        public List<T> call(PandaHackerHttpResult<T> httpResult) {
            if (httpResult.isSuccess()) {
                return httpResult.list;
            } else {
                throw new HttpException(httpResult.code, httpResult.msg);
            }
        }
    }

    private static class ErrorHandlerFunc implements Func1<Exception, Object> {
        private Context context;

        public ErrorHandlerFunc(Context context) {
            this.context = context;
        }

        @Override
        public Object call(Exception e) {
            if (!NetworkUtil.isNetworkAvailable(context)) {
                throw new HttpException(HttpException.NETWORK_ERROR, "哎呀,您没有连接网络");
            } else if (e instanceof HttpException) {
                throw (HttpException) e;
            } else {
                throw new HttpException(HttpException.ERROR, "请检查网络,或稍后再试");
            }
        }
    }

    private static class RetrofitTransformer implements Observable.Transformer {
        private HttpResultFunc<Object> func;
        private ErrorHandlerFunc errorHandlerFunc;

        RetrofitTransformer(Context context) {
            this.func = new HttpResultFunc<>();
            this.errorHandlerFunc = new ErrorHandlerFunc(context);
        }


        @Override
        public Object call(Object o) {
            Observable observable = ((Observable) o);
            return observable
                    .map(func)
                    .subscribeOn(io())
                    .unsubscribeOn(io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .onErrorReturn(errorHandlerFunc);
        }
    }
}
