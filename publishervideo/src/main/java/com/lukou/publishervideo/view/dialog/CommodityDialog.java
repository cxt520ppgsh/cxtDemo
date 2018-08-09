package com.lukou.publishervideo.view.dialog;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.View;
import android.widget.TextView;

import com.lukou.publishervideo.R;
import com.lukou.base.ui.BaseBottomDialog;
import com.lukou.publishervideo.databinding.CommodityDialogLayoutBinding;
import com.lukou.publishervideo.model.bean.PublisherVideo;


/**
 * Created by cxt on 2018/6/15.
 */

public class CommodityDialog extends BaseBottomDialog {
    private Context context;
    CommodityDialogLayoutBinding binding;
    private PublisherVideo publisherVideo;
    private SetFinishListener setFinishListener;

    public CommodityDialog(Context context) {
        super(context, R.style.main_dialog);
        this.context = context;
    }

    @Override
    public int setLayoutResource() {
        return R.layout.commodity_dialog_layout;
    }

    @Override
    public void init() {
        binding.publisherName.setText("达人名称：" + publisherVideo.getPublisherName() + "");
        binding.publisherId.setText("达人ID：" + publisherVideo.getKid() + "");
        binding.playCount.setText("播放量：" + publisherVideo.getPlayCnt() + "");
        binding.playTime.setText("播放时间：" + publisherVideo.getCreateTime() + "");
        binding.deleteTime.setText("删除时间：" + publisherVideo.getDeleteTime() + "");
        binding.back.setOnClickListener(view -> dismiss());
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        binding = DataBindingUtil.bind(view);
    }
    private void setSetFinishListner(SetFinishListener setFinishListner) {
        this.setFinishListener = setFinishListner;
    }

    private void setPublisherVideo(PublisherVideo publisherVideo) {
        this.publisherVideo = publisherVideo;
    }

    public static class Builder {
        private Context context;
        private SetFinishListener setFinishListener;
        private PublisherVideo publisherVideo;

        public Builder(final Context context) {
            this.context = context;
        }


        public Builder setSetFinishLister(SetFinishListener setFinishListener) {
            this.setFinishListener = setFinishListener;
            return this;
        }

        public Builder setPublisherVideo(PublisherVideo publisherVideo) {
            this.publisherVideo = publisherVideo;
            return this;
        }

        private CommodityDialog create() {
            CommodityDialog dialog = new CommodityDialog(context);
            dialog.setSetFinishListner(setFinishListener);
            dialog.setPublisherVideo(publisherVideo);
            return dialog;
        }

        public CommodityDialog show() {
            final CommodityDialog dialog = create();
            dialog.show();
            return dialog;
        }

    }


    public interface SetFinishListener {
        void onSetSuccess(String name, int count);

        void onSetFaild();
    }

}
