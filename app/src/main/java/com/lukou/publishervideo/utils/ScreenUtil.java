package com.lukou.publishervideo.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by cxt on 2018/6/18.
 */

public class ScreenUtil {
    public static int getScreenW(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenH(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }
}
