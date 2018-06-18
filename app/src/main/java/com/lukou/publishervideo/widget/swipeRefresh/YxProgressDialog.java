package com.lukou.publishervideo.widget.swipeRefresh;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.lukou.publishervideo.R;


/**
 * Created by sunbinqiang on 15/09/2017.
 */

public class YxProgressDialog extends Dialog {

    private TextView loadingTv;

    public YxProgressDialog(@NonNull Context context) {
        super(context, R.style.progressDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_dialog_layout);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        loadingTv = findViewById(R.id.loading_tv);
    }

    public void setText(String loading) {
        if (TextUtils.isEmpty(loading)) {
            loadingTv.setVisibility(View.GONE);
        }
        loadingTv.setText(loading);
    }
}
