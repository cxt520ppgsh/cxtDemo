package com.lukou.publishervideo.utils.netUtils;

import com.lidao.httpmodule.http.base.HttpResult;
import com.lukou.publishervideo.bean.Asiginer;
import com.lukou.publishervideo.bean.PublisherVideo;

import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by cxt on 2018/6/13.
 */

public interface ApiService {
    @GET("hotWords")
    Observable<Response<ResponseBody>> test();

    @GET("publisher/video")
    Observable<KuaishouHttpResult<PublisherVideo>> getPublisherVideo(@Query("page") int page,
                                                                     @Query("keyWord")String keyWord,
                                                                     @Query("sort_field")String sort_field,
                                                                     @Query("delete_type")int delete_type,
                                                                     @Query("video_type")int video_type,
                                                                     @Query("start_date")String start_date,
                                                                     @Query("end_date")String end_date,
                                                                     @Query("delivery_level") int delivery_level,
                                                                     @Query("asigner")String asigner);

    @GET("publisher/video/asign")
    Observable<KuaishouHttpResult<Asiginer>> getAsigner();
}
