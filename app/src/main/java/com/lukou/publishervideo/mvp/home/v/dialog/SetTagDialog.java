package com.lukou.publishervideo.mvp.home.v.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lukou.publishervideo.R;
import com.lukou.publishervideo.base.BaseDialog;
import com.lukou.publishervideo.bean.Asiginer;
import com.lukou.publishervideo.bean.PublisherVideo;
import com.lukou.publishervideo.mvp.home.dagger.scope.HomeActivityScope;
import com.lukou.publishervideo.utils.LKUtil;
import com.lukou.publishervideo.utils.netUtils.ApiFactory;
import com.lukou.publishervideo.utils.netUtils.KuaishouHttpResult;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by cxt on 2018/6/15.
 */

public class SetTagDialog extends BaseDialog {
    private Context context;
    private PublisherVideo publisherVideo;
    @Inject
    ApiFactory apiFactory;
    @BindView(R.id.rl)
    RelativeLayout rl;

    public SetTagDialog(Context context, PublisherVideo publisherVideo) {
        super(context, R.style.main_dialog);
        this.context = context;
        this.publisherVideo = publisherVideo;
    }
    @Override
    public int setView() {
        return R.layout.set_tag_dialog_layout;
    }

    @Override
    public void init() {
        setTagListner();
    }

    private void setTagListner() {
        for (int i = 0; i < rl.getChildCount(); i++) {
            if (rl.getChildAt(i) instanceof ViewGroup){
                ViewGroup viewGroup = (ViewGroup) rl.getChildAt(i);
                for (int k = 0; k < viewGroup.getChildCount(); k++){
                    if (viewGroup.getChildAt(k) instanceof Button){
                        Button button = (Button) viewGroup.getChildAt(k);
                        button.setOnClickListener(view -> {

                        });
                    }
                }
            }
        }
    }

    @OnClick(R.id.close)
    void close(){
        dismiss();
    }
}
