package com.lukou.publishervideo.utils.netUtils;

import com.lidao.httpmodule.http.base.HttpResult;
import com.lukou.publishervideo.bean.Asiginer;
import com.lukou.publishervideo.bean.PublisherVideo;

import java.util.List;
import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by cxt on 2018/6/13.
 */

public interface ApiService {

    @GET("publisher/video")
    Observable<KuaishouHttpResult<PublisherVideo>> getPublisherVideo(@Query("delete_type") int delete_type,
                                                                     @Query("video_type") int video_type,
                                                                     @Query("asigner") String asigner);

    @GET("publisher/video/asign")
    Observable<KuaishouHttpResult<Asiginer>> getAsigner();

    @FormUrlEncoded
    @POST("publisher/video")
    Observable<KuaishouHttpResult> setTag(@Field("fid") String fid,@Field("type") int type);
}
