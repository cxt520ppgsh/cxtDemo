package com.lukou.publishervideo.view.dialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.flexbox.FlexboxLayout;
import com.lukou.publishervideo.R;
import com.lukou.publishervideo.base.BaseBottomDialog;
import com.lukou.publishervideo.base.BaseDialog;
import com.lukou.publishervideo.model.bean.Asiginer;
import com.lukou.publishervideo.model.bean.PublisherVideo;
import com.lukou.publishervideo.model.net.ApiFactory;
import com.lukou.publishervideo.view.activity.HomeActivity;
import com.lukou.publishervideo.utils.LKUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.Gravity.CENTER;

/**
 * Created by cxt on 2018/6/15.
 */

public class SetAsignerDialog extends BaseBottomDialog {
    private Context context;
    @BindView(R.id.asigner_lay)
    FlexboxLayout asignerLay;
    Button sellectButtun;
    private SetAsignerFinishListener setAsignerFinishListener;

    public SetAsignerDialog(Context context) {
        super(context, R.style.main_dialog);
        this.context = context;
    }

    @Override
    public int setLayoutResource() {
        return R.layout.set_asigner_dialog_layout;
    }

    @Override
    public void init() {
        ApiFactory.getInstance().getAsigner()
                .subscribe(list -> {
                    setLayout(list);
                    setTagListner();
                }, throwable -> {

                });
    }


    private void setSetAsignerDialog(SetAsignerFinishListener setAsignerFinishListener) {
        this.setAsignerFinishListener = setAsignerFinishListener;
    }

    @OnClick(R.id.cancel_btn)
    public void cancelListener(View v) {
        this.dismiss();
    }

    @OnClick(R.id.confirm_btn)
    public void confirmListener(View v) {
        if (sellectButtun != null) {
            String[] strings = sellectButtun.getText().toString().split("\\(");
            String name = strings[0];
            int asignCount = Integer.parseInt(strings[1].split("个")[0]);
            setAsignerFinishListener.onSetAsignerSuccess(name, asignCount);
            this.dismiss();
        }
    }

    private void setLayout(List<Asiginer> asiginers) {
        for (Asiginer asiginer : asiginers) {
            addItem(asiginer);
        }
    }

    private void addItem(Asiginer asiginer) {
        Button button = new Button(context);
        button.setTextSize(12);
        button.setPadding(0, 0, 0, 0);
        button.setGravity(CENTER);
        button.setTextColor(context.getResources().getColor(R.color.white));
        button.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.main_dialog_bt_selecter));
        FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LKUtil.dp2px(context, 32));
        lp.setMargins(0, LKUtil.dip2px(context, 8), 0, LKUtil.dip2px(context, 8));
        button.setLayoutParams(lp);
        asignerLay.addView(button);
        if (asiginer != null) {
            button.setText(asiginer.getAsignerName() + "(" + asiginer.getAsignCnt() + "个)");
        } else {
            button.setVisibility(View.INVISIBLE);
        }
    }

    private void setTagListner() {
        for (int i = 0; i < asignerLay.getChildCount(); i++) {
            if (asignerLay.getChildAt(i) instanceof Button) {
                Button button = (Button) asignerLay.getChildAt(i);
                button.setOnClickListener(view -> {
                    setButtonSellected(button);
                });
            }
        }
    }

    private void setButtonSellected(Button sellectButton) {
        sellectButtun = sellectButton;
        for (int i = 0; i < asignerLay.getChildCount(); i++) {
            if (asignerLay.getChildAt(i) instanceof Button) {
                Button button = (Button) asignerLay.getChildAt(i);
                if (button == sellectButton) {
                    button.setSelected(true);
                } else {
                    button.setSelected(false);
                }
            }
        }
    }

    public static class Builder {
        private Context context;
        private SetAsignerFinishListener setTagFinishListener;

        public Builder(final Context context) {
            this.context = context;
        }


        public SetAsignerDialog.Builder setSetAsignerDialog(SetAsignerFinishListener setTagFinishListener) {
            this.setTagFinishListener = setTagFinishListener;
            return this;
        }

        private SetAsignerDialog create() {
            SetAsignerDialog dialog = new SetAsignerDialog(context);
            dialog.setSetAsignerDialog(setTagFinishListener);
            return dialog;
        }

        public SetAsignerDialog show() {
            final SetAsignerDialog dialog = create();
            dialog.show();
            return dialog;
        }

    }


    public interface SetAsignerFinishListener {
        void onSetAsignerSuccess(String name, int count);

        void onSetAsignerTagFaild();
    }
}
