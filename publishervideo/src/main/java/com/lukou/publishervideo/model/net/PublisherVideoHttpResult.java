package com.lukou.publishervideo.model.net;


import com.lidao.httpmodule.http.base.BaseHttpResult;

import java.util.List;

/**
 * Created by cxt on 2018/6/14.
 */

public class PublisherVideoHttpResult<T> extends BaseHttpResult<T> {
    public int total;
    public int start;
    public int perpage;
    public String msg;
    public int code;
    public String title;
    public String data;
    public T list;

    @Override
    public int getmCode() {
        return code;
    }

    @Override
    public String getmMsg() {
        return msg;
    }

    @Override
    public T getmData() {
        return list;
    }
}
