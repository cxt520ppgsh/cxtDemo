package com.lukou.base.http;



import android.os.Environment;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by sunbinqiang on 21/03/2018.
 */

public class HeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder builder = original.newBuilder()
                .method(original.method(), original.body());
        try {
            //todo
          /*  builder.header("x-device", "android")
                    .addHeader("x-platform", "app")
                    .addHeader("token", LibApplication.instance().accountService().token())
                    .addHeader("x-app-version", Environment.versionCodeStr())
                    .addHeader("x-source", Environment.source())
                    .addHeader("x-os-version", android.os.Build.VERSION.RELEASE)
                    .addHeader("x-bundle-id", Environment.getAppId())
                    .addHeader("x-model", android.os.Build.MODEL)
                    .addHeader("x-user-group", String.valueOf(LibApplication.instance().configService().userGroup().type()))
                    .addHeader("x-device-id", LibApplication.instance().configService().deviceId());*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chain.proceed(builder.build());
    }

}
