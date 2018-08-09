package com.lukou.publishervideo.model.net;

import com.lukou.base.http.PandaHackerHttpResult;
import com.lukou.publishervideo.model.bean.Asiginer;
import com.lukou.publishervideo.model.bean.PublisherVideo;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by cxt on 2018/6/13.
 */

public interface ApiService {

    @GET("publisher/video")
    Observable<PandaHackerHttpResult<PublisherVideo>> getPublisherVideo(@Query("delete_type") int delete_type,
                                                                        @Query("video_type") int video_type,
                                                                        @Query("asigner") String asigner);

    @GET("publisher/video/asign")
    Observable<PandaHackerHttpResult<Asiginer>> getAsigner();

    @FormUrlEncoded
    @POST("publisher/video")
    Observable<PandaHackerHttpResult> setTag(@Field("fid") String fid, @Field("type") int type);
}
