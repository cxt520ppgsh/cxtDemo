package com.lukou.publishervideo.view.dialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.flexbox.FlexboxLayout;
import com.lukou.publishervideo.R;
import com.lukou.publishervideo.base.BaseBottomDialog;
import com.lukou.publishervideo.base.BaseDialog;
import com.lukou.publishervideo.model.bean.PublisherVideo;
import com.lukou.publishervideo.model.net.ApiFactory;
import com.lukou.publishervideo.view.activity.HomeActivity;
import com.lukou.publishervideo.utils.LKUtil;
import com.lukou.publishervideo.utils.TagUtil;
import com.lukou.publishervideo.view.widget.VideoRecycleView;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

import static android.view.Gravity.CENTER;

/**
 * Created by cxt on 2018/6/15.
 */

public class SetTagDialog extends BaseBottomDialog {
    private Context context;
    private PublisherVideo publisherVideo;
    private SetTagFinishListener setTagFinishListener;
    @BindView(R.id.rl)
    RelativeLayout rl;
    @BindView(R.id.tag_lay)
    FlexboxLayout tagLay;

    public SetTagDialog(Context context) {
        super(context, R.style.main_dialog);
        this.context = context;
    }

    @Override
    public int setLayoutResource() {
        return R.layout.set_tag_dialog_layout;
    }

    @Override
    public void init() {
        setLayout();
    }

    private void setSetTagFinishListener(SetTagFinishListener setTagFinishListener) {
        this.setTagFinishListener = setTagFinishListener;
    }

    private void setPublisherVideo(PublisherVideo publisherVideo) {
        this.publisherVideo = publisherVideo;
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
                            ApiFactory.getInstance().setTag(publisherVideo.getFid(), TagUtil.getTagId(button.getText().toString())).subscribe(kuaishouHttpResult -> {
                                setTagFinishListener.onSetTagSuccess();
                                publisherVideo.setType(TagUtil.getTagId(button.getText().toString()));
                                dismiss();
                            }, throwable -> {
                                setTagFinishListener.onSetTagFaild();
                            });
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

    public static class Builder {

        private Context context;
        private SetTagFinishListener setTagFinishListener;
        private PublisherVideo publisherVideo;

        public Builder(final Context context) {
            this.context = context;
        }


        public SetTagDialog.Builder setSetTagFinishListener(SetTagFinishListener setTagFinishListener) {
            this.setTagFinishListener = setTagFinishListener;
            return this;
        }

        public SetTagDialog.Builder setPublisherVideo(PublisherVideo publisherVideo) {
            this.publisherVideo = publisherVideo;
            return this;
        }

        private SetTagDialog create() {
            SetTagDialog dialog = new SetTagDialog(context);
            dialog.setSetTagFinishListener(setTagFinishListener);
            dialog.setPublisherVideo(publisherVideo);
            return dialog;
        }

        public SetTagDialog show() {
            final SetTagDialog dialog = create();
            dialog.show();
            return dialog;
        }

    }

    public interface SetTagFinishListener {
        void onSetTagSuccess();

        void onSetTagFaild();
    }
}
