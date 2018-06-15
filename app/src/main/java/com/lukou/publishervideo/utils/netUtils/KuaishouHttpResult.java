package com.lukou.publishervideo.utils.netUtils;

import com.lukou.publishervideo.bean.PublisherVideo;

import java.util.List;

/**
 * Created by cxt on 2018/6/14.
 */

public class KuaishouHttpResult<T> {
    public int total;
    public int start;
    public int perpage;
    public String msg;
    public int code;
    public String title;
    public List<PublisherVideo> list;
}
