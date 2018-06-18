package com.lukou.publishervideo.widget.swipeRefresh;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

import com.lukou.publishervideo.R;


/**
 * Created by sunbinqiang on 27/12/2017.
 */

public class SupportSwipeRefreshLayout extends SwipeRefreshLayout {
    public SupportSwipeRefreshLayout(Context context) {
        this(context, null);
    }

    public SupportSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setColorSchemeResources(
                R.color.colorPrimary, R.color.colorPrimaryDark,
                R.color.colorAccent, R.color.colorAccent);
    }
}
