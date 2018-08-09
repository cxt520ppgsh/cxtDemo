package com.lukou.publishervideo.view.dialog;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.flexbox.FlexboxLayout;
import com.lukou.publishervideo.R;
import com.lukou.base.ui.BaseBottomDialog;
import com.lukou.publishervideo.databinding.SetAsignerDialogLayoutBinding;
import com.lukou.publishervideo.model.bean.Asiginer;
import com.lukou.publishervideo.model.net.ApiFactory;
import com.lukou.base.utils.LKUtil;

import java.util.List;


import static android.view.Gravity.CENTER;

/**
 * Created by cxt on 2018/6/15.
 */

public class SetAsignerDialog extends BaseBottomDialog {
    private Context context;
    Button sellectButtun;
    SetAsignerDialogLayoutBinding binding;
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
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        binding = DataBindingUtil.bind(view);
    }

    @Override
    public void init() {
        ApiFactory.getInstance().getAsigner()
                .subscribe(list -> {
                    setLayout(list);
                    setTagListner();
                }, throwable -> {

                });
        binding.cancelBtn.setOnClickListener(view -> dismiss());
        binding.confirmBtn.setOnClickListener(view -> {
            if (sellectButtun != null) {
                String[] strings = sellectButtun.getText().toString().split("\\(");
                String name = strings[0];
                int asignCount = Integer.parseInt(strings[1].split("个")[0]);
                setAsignerFinishListener.onSetAsignerSuccess(name, asignCount);
                dismiss();
            }
        });
    }

    private void setSetAsignerDialog(SetAsignerFinishListener setAsignerFinishListener) {
        this.setAsignerFinishListener = setAsignerFinishListener;
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
        binding.asignerLay.addView(button);
        if (asiginer != null) {
            button.setText(asiginer.getAsignerName() + "(" + asiginer.getAsignCnt() + "个)");
        } else {
            button.setVisibility(View.INVISIBLE);
        }
    }

    private void setTagListner() {
        for (int i = 0; i <  binding.asignerLay.getChildCount(); i++) {
            if ( binding.asignerLay.getChildAt(i) instanceof Button) {
                Button button = (Button)  binding.asignerLay.getChildAt(i);
                button.setOnClickListener(view -> {
                    setButtonSellected(button);
                });
            }
        }
    }

    private void setButtonSellected(Button sellectButton) {
        sellectButtun = sellectButton;
        for (int i = 0; i <  binding.asignerLay.getChildCount(); i++) {
            if ( binding.asignerLay.getChildAt(i) instanceof Button) {
                Button button = (Button)  binding.asignerLay.getChildAt(i);
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
