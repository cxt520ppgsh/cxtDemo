package com.lukou.publishervideo.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.lukou.publishervideo.R;


import butterknife.ButterKnife;

/**
 * Created by cxt on 2018/6/15.
 */

public abstract class BaseDialog extends Dialog {
    Context context;

    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayoutResource());
        ButterKnife.bind(this);
        setDialogParmes();
        init();
    }

    public abstract void setDialogParmes();

    public abstract int setLayoutResource();

    public abstract void init();
}
