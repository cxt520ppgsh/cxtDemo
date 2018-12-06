package com.flyaudio.packagemanager.model.net;


import com.flyaudio.packagemanager.model.bean.Asiginer;
import com.flyaudio.packagemanager.model.bean.PublisherVideo;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by cxt on 2018/6/13.
 */

public interface ApiService {
    public static final String BASE_URL = "https://alissl.ucdl.pp.uc.cn/fs01/union_pack/";


    @Streaming
    @GET
    Observable<ResponseBody> executeDownload(@Header("Range") String range, @Url() String url);


    /**
     * @param start 从某个字节开始下载数据
     * @param url   文件下载的url
     * @return Observable
     * @Streaming 这个注解必须添加，否则文件全部写入内存，文件过大会造成内存溢出
     */
    @Streaming
    @GET
    Observable<ResponseBody> download(@Header("RANGE") String start, @Url String url);


    @Streaming
    @GET
    Call<ResponseBody> downloadApk(@Url String url);

    @GET("publisher/video")
    Observable<PackageHttpResult<List<PublisherVideo>>> getPublisherVideo(@Query("delete_type") int delete_type,
                                                                          @Query("video_type") int video_type,
                                                                          @Query("asigner") String asigner);

    @GET("publisher/video/asign")
    Observable<PackageHttpResult<List<Asiginer>>> getAsigner();

    @FormUrlEncoded
    @POST("publisher/video")
    Observable<PackageHttpResult> setTag(@Field("fid") String fid, @Field("type") int type);
}
