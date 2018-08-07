package com.lukou.publishervideo.base;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.lukou.publishervideo.R;

/**
 * Created by cxt on 2018/7/9.
 */

public abstract class BaseBottomDialog extends BaseDialog {
    public BaseBottomDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    public void setDialogParmes() {
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.bottom_menu_animation);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(true);
    }
}
