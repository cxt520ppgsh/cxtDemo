package com.lukou.publishervideo.utils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by cxt on 2018/6/20.
 */

public class TagUtil {
    public static Map<String, Integer> tagMap = new LinkedHashMap<>();

    static {
        tagMap.put("非广告", 2);
        tagMap.put("熊猫优选", 3);
        tagMap.put("嘟嘟百货", 5);
        tagMap.put("竞品广告", 7);
        tagMap.put("省钱快报", 11);
        tagMap.put("楚楚街", 12);
        tagMap.put("返利网", 13);
        tagMap.put("公众号", 14);
        tagMap.put("省啦啦", 15);
        tagMap.put("一淘", 16);
        tagMap.put("贝壳优品", 17);
        tagMap.put("酷价", 18);
        tagMap.put("淘宝特价", 20);
        tagMap.put("花小钱", 21);
        tagMap.put("万利宝", 22);
        tagMap.put("其他竞品", 19);
        tagMap.put("微商", 6);
        tagMap.put("其他广告", 4);
    }

    public static int getTagId(String key) {
        return tagMap.get(key);
    }
}
