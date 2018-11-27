package com.flyaudio.publishervideo.view.dialog;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.flexbox.FlexboxLayout;
import com.flyaudio.publishervideo.R;
import com.flyaudio.base.ui.BaseBottomDialog;
import com.flyaudio.publishervideo.databinding.SetTagDialogLayoutBinding;
import com.flyaudio.publishervideo.model.bean.PublisherVideo;
import com.flyaudio.publishervideo.model.net.ApiFactory;
import com.flyaudio.base.utils.LKUtil;
import com.flyaudio.base.utils.TagUtil;

import static android.view.Gravity.CENTER;

/**
 * Created by cxt on 2018/6/15.
 */

public class SetTagDialog extends BaseBottomDialog {
    private Context context;
    private PublisherVideo publisherVideo;
    private SetTagFinishListener setTagFinishListener;
    SetTagDialogLayoutBinding binding;

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

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        binding = DataBindingUtil.bind(view);
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
        binding.close.setOnClickListener(view -> dismiss());
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
        binding.tagLay.addView(button);
        if (tags != null) {
            button.setText(tags);
        } else {
            button.setVisibility(View.INVISIBLE);
        }
    }

    private void setTagButton() {
        for (int i = 0; i < binding.rl.getChildCount(); i++) {
            if (binding.rl.getChildAt(i) instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) binding.rl.getChildAt(i);
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
