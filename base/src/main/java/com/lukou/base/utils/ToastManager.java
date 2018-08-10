package com.lukou.base.utils;

import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.lukou.base.R;
import com.lukou.base.application.BaseApplication;

import java.lang.ref.WeakReference;


/**
 * @author Xu
 */

public final class ToastManager {

    private static WeakReference<Toast> wToast;

    private static void cancelExistToast() {
        if (wToast != null && wToast.get() != null) {
            wToast.get().cancel();
        }
    }

    public static void showToast(CharSequence toastText, int duration) {
        cancelExistToast();
        Toast toast = Toast.makeText(BaseApplication.instance(), toastText, duration);
        toast.show();
        wToast = new WeakReference<>(toast);
    }

    public static void showToast(@StringRes int stringResId, int duration) {
        showToast(BaseApplication.instance().getString(stringResId), duration);
    }

    public static void showToast(@StringRes int stringResId) {
        showToast(stringResId, Toast.LENGTH_SHORT);
    }

    public static void showToast(CharSequence toast) {
        showToast(toast, Toast.LENGTH_SHORT);
    }

    /**
     * @param view    自定义View
     * @param gravity 位置
     */
    public static void showToast(View view, int gravity, int duration) {
        cancelExistToast();
        Toast toast = new Toast(BaseApplication.instance());
        toast.setGravity(gravity, 0, 0);
        toast.setDuration(duration);
        toast.setView(view);
        toast.show();
        wToast = new WeakReference<>(toast);
    }

    /**
     * @param view 自定义View
     */
    public static void showCenterToast(View view) {
        showCenterToast(view, Toast.LENGTH_SHORT);
    }

    /**
     * @param view 自定义View
     */
    public static void showCenterToast(View view, int duration) {
        showToast(view, Gravity.CENTER, duration);
    }

    public static void showCenterToast(@StringRes int stringResId) {
        showCenterToast(stringResId, Toast.LENGTH_SHORT);
    }

    public static void showCenterToast(String toast) {
        showCenterToast(toast, Toast.LENGTH_SHORT);
    }

    /**
     * @param stringResId 自定义
     */
    public static void showCenterToast(@StringRes int stringResId, int duration) {
        showCenterToast(BaseApplication.instance().getString(stringResId), duration);
    }

    /**
     * @param toast 自定义
     */
    public static void showCenterToast(String toast, int duration) {
        View v = View.inflate(BaseApplication.instance(), R.layout.toast_center_layout, null);
        ((TextView)v.findViewById(R.id.text)).setText(toast);
        showToast(v, Gravity.CENTER, duration);
    }

}
