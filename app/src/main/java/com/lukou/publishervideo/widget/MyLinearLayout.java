package com.lukou.publishervideo.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by cxt on 2018/6/21.
 */

public class MyLinearLayout extends LinearLayout {
    private float ACTION_Y = 0;
    private float dowxY = 0;
    private float dy = 0;

    public MyLinearLayout(Context context) {
        super(context);
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
