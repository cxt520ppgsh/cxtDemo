package com.lukou.publishervideo.utils.netUtils;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by cxt on 2018/6/13.
 */

public interface ApiService {
    @GET("hotWords")
    Observable<Response<ResponseBody>> test();
}
