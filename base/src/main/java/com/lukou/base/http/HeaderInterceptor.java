package com.lukou.base.http;



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
           // builder.header("Cookie", "Hm_lvt_0dc0a76120f8a45eb79b870f4f425c8e=1528794937; user=\"2|1:0|10:1528795049|4:user|12:MTQ2MzY5MQ==|6c6036d423d45ba2a0bf28760ff78b313591797bcc8d2cf7cdcaec018e70c2d3");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return chain.proceed(builder.build());
    }

}
