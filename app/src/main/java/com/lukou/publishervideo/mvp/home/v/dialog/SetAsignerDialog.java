package com.lukou.publishervideo.mvp.home.v.dialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lukou.publishervideo.R;
import com.lukou.publishervideo.base.BaseDialog;
import com.lukou.publishervideo.bean.Asiginer;
import com.lukou.publishervideo.mvp.home.v.activity.HomeActivity;
import com.lukou.publishervideo.utils.LKUtil;
import com.lukou.publishervideo.utils.netUtils.ApiFactory;

import java.util.List;

import butterknife.BindView;

import static android.view.Gravity.CENTER;

/**
 * Created by cxt on 2018/6/15.
 */

public class SetAsignerDialog extends BaseDialog {
    private Context context;
    @BindView(R.id.ll)
    LinearLayout ll;
    HomeActivity homeActivity;
    Button sellectButtun;

    public SetAsignerDialog(Context context) {
        super(context, R.style.main_dialog);
        this.context = context;
        homeActivity = (HomeActivity) context;
    }

    @Override
    public int setView() {
        return R.layout.set_asigner_dialog_layout;
    }

    @Override
    public void init() {
        homeActivity.addSubscription(ApiFactory.getInstance().getAsigner()
                .subscribe(httpResult -> {
                    setLayout(httpResult.list);
                    setTagListner();
                }, throwable -> {
                    throwable.printStackTrace();
                }));
    }

    private void setLayout(List<Asiginer> asiginers) {
        int i = -1;
        LinearLayout oldLinearLayout = null;
        for (Asiginer asiginer : asiginers) {
            i++;
            //换行
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
        if (asiginers.size() % 3 != 0) {
            for (int j = 0; j < 3 - asiginers.size() % 3; j++) {
                addItem(null, oldLinearLayout);
            }
            if (asiginers.size() % 3 == 1) {
                addSpace(oldLinearLayout);
            }
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
                        if (button.getText().toString().equals("取消")) {
                            button.setOnClickListener(view -> {
                                this.dismiss();
                            });

                        } else if (button.getText().toString().equals("确定")) {
                            button.setOnClickListener(view -> {
                                if (sellectButtun != null) {
                                    String[] strings = sellectButtun.getText().toString().split("\\(");
                                    String name = strings[0];
                                    int asignCount = Integer.parseInt(strings[1].split("个")[0]);
                                    homeActivity.refresh(HomeActivity.SET_ASIGNER, name, asignCount);
                                    this.dismiss();
                                }
                            });

                        } else {
                            button.setOnClickListener(view -> {
                                setButtonSellected(button);
                            });

                        }

                    }
                }
            }
        }
    }

    private void setButtonSellected(Button sellectButton) {
        sellectButtun = sellectButton;
        for (int i = 0; i < ll.getChildCount(); i++) {
            if (ll.getChildAt(i) instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) ll.getChildAt(i);
                for (int k = 0; k < viewGroup.getChildCount(); k++) {
                    if (viewGroup.getChildAt(k) instanceof Button) {
                        Button button = (Button) viewGroup.getChildAt(k);
                        if (!button.getText().equals("取消") && !button.getText().equals("确定")) {
                            if (button == sellectButton) {
                                button.setSelected(true);
                            } else {
                                button.setSelected(false);
                            }
                        }
                    }
                }
            }
        }
    }

}
