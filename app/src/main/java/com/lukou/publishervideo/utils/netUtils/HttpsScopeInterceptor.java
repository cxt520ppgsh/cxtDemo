package com.lukou.publishervideo.utils.netUtils;

import android.text.TextUtils;

import com.lukou.publishervideo.BuildConfig;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by sunbinqiang on 21/03/2018.
 */
public class HttpsScopeInterceptor implements Interceptor {

    private static final ArrayList<String> HTPPS_SCOPE_LIST = new ArrayList<>();

    static {
        HTPPS_SCOPE_LIST.add("/payment/");

        HTPPS_SCOPE_LIST.add("/order");
        HTPPS_SCOPE_LIST.add("/consignees");

        HTPPS_SCOPE_LIST.add("/unbind_third");
        HTPPS_SCOPE_LIST.add("/bind_third");
        HTPPS_SCOPE_LIST.add("/user/nickname");
        HTPPS_SCOPE_LIST.add("/user/avatar");

        HTPPS_SCOPE_LIST.add("/login");
        HTPPS_SCOPE_LIST.add("/third_reg");
        HTPPS_SCOPE_LIST.add("/third_login");
        HTPPS_SCOPE_LIST.add("/bind/phone");
        HTPPS_SCOPE_LIST.add("/captcha");
        HTPPS_SCOPE_LIST.add("/verification_code");
        HTPPS_SCOPE_LIST.add("/sms/verification_code");
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        String path = original.url().encodedPath();
        if (isInHttpsScope(path) && BuildConfig.baseUrl.contains(original.url().host())) {
            return chain.proceed(
                    original.newBuilder()
                            .url(original.url().newBuilder().scheme("https").build())
                            .build()
            );
        }
        return chain.proceed(original);
    }

    private boolean isInHttpsScope(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        for (String scope  : HTPPS_SCOPE_LIST) {
            if (path.contains(scope)) {
                return true;
            }
        }
        return false;
    }
}

