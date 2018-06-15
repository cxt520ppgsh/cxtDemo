package com.lukou.publishervideo.mvp.home.v.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lukou.publishervideo.R;
import com.lukou.publishervideo.base.BaseDialog;
import com.lukou.publishervideo.base.BaseView;
import com.lukou.publishervideo.bean.Asiginer;
import com.lukou.publishervideo.bean.PublisherVideo;
import com.lukou.publishervideo.utils.LKUtil;
import com.lukou.publishervideo.utils.netUtils.ApiFactory;
import com.lukou.publishervideo.utils.netUtils.KuaishouHttpResult;

import java.util.ArrayList;
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

public class SetAsignerDialog extends BaseDialog {
    private Context context;
    private PublisherVideo publisherVideo;
    @Inject
    ApiFactory apiFactory;
    @BindView(R.id.ll)
    LinearLayout ll;

    public SetAsignerDialog(Context context, PublisherVideo publisherVideo) {
        super(context, R.style.main_dialog);
        this.context = context;
        this.publisherVideo = publisherVideo;
    }

    @Override
    public int setView() {
        return R.layout.set_asigner_dialog_layout;
    }

    @Override
    public void init() {
        /*apiFactory.getAsigner().subscribe(new Action1<KuaishouHttpResult<Asiginer>>() {
            @Override
            public void call(KuaishouHttpResult<Asiginer> asiginerKuaishouHttpResult) {

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        });*/

        List<Asiginer> asiginerList = new ArrayList<>();
        for (int i = 0; i <= 9; i++) {
            Asiginer asiginer = new Asiginer();
            asiginer.setAsignerName("kobe");
            asiginer.setAsignCnt(222);
            asiginerList.add(asiginer);
        }
        setLayout(asiginerList);

    }

    private void setLayout(List<Asiginer> asiginers) {
        int i = -1;
        LinearLayout oldLinearLayout = null;
        for (Asiginer asiginer : asiginers) {
            i++;
            if (i % 3 == 0) {
                oldLinearLayout = new LinearLayout(context);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, LKUtil.dp2px(context, 14), 0, 0);
                oldLinearLayout.setLayoutParams(lp);
                oldLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
                ll.addView(oldLinearLayout);
            }
            addItem(asiginer, oldLinearLayout);
            if (i % 3 != 2) {
                addSpace(oldLinearLayout);
            }
        }
        for (int j = 0; j < 3 - asiginers.size() % 3; j++) {
            addItem(null, oldLinearLayout);
        }
        if (asiginers.size() % 3 == 1) {
            addSpace(oldLinearLayout);
        }


        addBottomBar();
    }

    private void addSpace(LinearLayout oldLinearLayout) {
        View space = new View(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LKUtil.dp2px(context, 16), LKUtil.dp2px(context, 3));
        space.setLayoutParams(lp);
        oldLinearLayout.addView(space);
    }

    private void addBottomBar() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        View view = View.inflate(context, R.layout.main_dialog_bottom_bar, null);
        ll.addView(view);
    }

    private void addItem(Asiginer asiginer, LinearLayout oldLinearLayout) {
        Button button = new Button(context);
        button.setTextSize(12);
        button.setPadding(0, 0, 0, 0);
        button.setGravity(CENTER);
        button.setTextColor(context.getResources().getColor(R.color.white));
        button.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.main_dialog_bt_selecter));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LKUtil.dp2px(context, 32));
        lp.weight = 1;
        button.setLayoutParams(lp);
        oldLinearLayout.addView(button);

        if (asiginer != null) {
            button.setText(asiginer.getAsignerName() + "(" + asiginer.getAsignCnt() + "个)");
        } else {
            button.setVisibility(View.INVISIBLE);
        }
    }


    private void setTagListner() {
        for (int i = 0; i < ll.getChildCount(); i++) {
            if (ll.getChildAt(i) instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) ll.getChildAt(i);
                for (int k = 0; k < viewGroup.getChildCount(); k++) {
                    if (viewGroup.getChildAt(k) instanceof Button) {
                        Button button = (Button) viewGroup.getChildAt(k);
                        button.setOnClickListener(view -> {

                        });
                    }
                }
            }
        }
    }

}
