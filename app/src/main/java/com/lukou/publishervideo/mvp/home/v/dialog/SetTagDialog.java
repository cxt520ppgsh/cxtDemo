package com.lukou.publishervideo.mvp.home.v.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
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

import com.google.android.flexbox.FlexboxLayout;
import com.lukou.publishervideo.R;
import com.lukou.publishervideo.base.BaseDialog;
import com.lukou.publishervideo.bean.Asiginer;
import com.lukou.publishervideo.bean.PublisherVideo;
import com.lukou.publishervideo.mvp.home.dagger.scope.HomeActivityScope;
import com.lukou.publishervideo.mvp.home.v.activity.HomeActivity;
import com.lukou.publishervideo.utils.LKUtil;
import com.lukou.publishervideo.utils.SharedPreferencesUtil;
import com.lukou.publishervideo.utils.TagUtil;
import com.lukou.publishervideo.utils.netUtils.ApiFactory;
import com.lukou.publishervideo.utils.netUtils.KuaishouHttpResult;
import com.lukou.publishervideo.widget.VideoRecycleView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

import static android.view.Gravity.CENTER;

/**
 * Created by cxt on 2018/6/15.
 */

public class SetTagDialog extends BaseDialog {
    private Context context;
    private HomeActivity homeActivity;
    private PublisherVideo publisherVideo;
    @BindView(R.id.rl)
    RelativeLayout rl;
    @BindView(R.id.tag_lay)
    FlexboxLayout tagLay;

    public SetTagDialog(Context context, PublisherVideo publisherVideo) {
        super(context, R.style.main_dialog);
        this.context = context;
        homeActivity = (HomeActivity) context;
        this.publisherVideo = publisherVideo;
    }

    @Override
    public int setView() {
        return R.layout.set_tag_dialog_layout;
    }

    @Override
    public void init() {
        setLayout();
    }

    private void setLayout() {
        for (String tag : TagUtil.tagMap.keySet()) {
            if (tag.equals("非广告")) {
                continue;
            }
            addItem(tag);
        }
        setTagButton();
    }

    private void addItem(String tags) {
        Button button = new Button(context);
        button.setTextSize(14);
        button.setPadding(0, 0, 0, 0);
        button.setGravity(CENTER);
        button.setTextColor(context.getResources().getColor(R.color.white));
        button.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.main_dialog_bt_selecter));
        FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LKUtil.dp2px(context, 32));
        lp.setMargins(0, LKUtil.dip2px(context, 8), 0, LKUtil.dip2px(context, 8));
        button.setLayoutParams(lp);
        tagLay.addView(button);
        if (tags != null) {
            button.setText(tags);
        } else {
            button.setVisibility(View.INVISIBLE);
        }
    }

    private void setTagButton() {
        for (int i = 0; i < rl.getChildCount(); i++) {
            if (rl.getChildAt(i) instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) rl.getChildAt(i);
                for (int k = 0; k < viewGroup.getChildCount(); k++) {
                    if (viewGroup.getChildAt(k) instanceof Button) {
                        Button button = (Button) viewGroup.getChildAt(k);
                        if (TagUtil.getTagId(button.getText().toString()) == publisherVideo.getType()) {
                            button.setSelected(true);
                        } else {
                            button.setSelected(false);
                        }
                        button.setOnClickListener(view -> {
                            homeActivity.addSubscription(homeActivity.apiFactory.setTag(publisherVideo.getFid(), TagUtil.getTagId(button.getText().toString())).subscribe(kuaishouHttpResult -> {
                                homeActivity.initAsignerTv();
                                publisherVideo.setType(TagUtil.getTagId(button.getText().toString()));
                                VideoRecycleView.setCanScrollToNext(true);
                                homeActivity.rv.scrollToNext();
                                dismiss();
                            }, new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {

                                }
                            }));
                        });
                    }
                }
            }
        }
    }

    @OnClick(R.id.close)
    void close() {
        dismiss();
    }
}
