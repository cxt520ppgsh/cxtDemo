package com.lukou.base.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;


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
        View view = LayoutInflater.from(getContext()).inflate(setLayoutResource(), null, false);
        setContentView(view);
        onBindDialogView(view);
        setDialogParmes();
        init();
    }

    protected void onBindDialogView(View view) {

    }

    public abstract void setDialogParmes();

    public abstract int setLayoutResource();

    public abstract void init();
}
