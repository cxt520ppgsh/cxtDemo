package com.lukou.publishervideo.view.dialog;

import android.content.Context;
import android.widget.TextView;

import com.lukou.publishervideo.R;
import com.lukou.publishervideo.base.BaseBottomDialog;
import com.lukou.publishervideo.base.BaseDialog;
import com.lukou.publishervideo.model.bean.PublisherVideo;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by cxt on 2018/6/15.
 */

public class CommodityDialog extends BaseBottomDialog {
    private Context context;
    @BindView(R.id.publisher_name)
    TextView publisher_name;
    @BindView(R.id.publisher_id)
    TextView publisher_id;
    @BindView(R.id.play_count)
    TextView play_count;
    @BindView(R.id.play_time)
    TextView play_time;
    @BindView(R.id.delete_time)
    TextView delete_time;
    @BindView(R.id.never_watch)
    TextView never_watch;
    @BindView(R.id.back)
    TextView back;
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
        publisher_name.setText("达人名称：" + publisherVideo.getPublisherName() + "");
        publisher_id.setText("达人ID：" + publisherVideo.getKid() + "");
        play_count.setText("播放量：" + publisherVideo.getPlayCnt() + "");
        play_time.setText("播放时间：" + publisherVideo.getCreateTime() + "");
        delete_time.setText("删除时间：" + publisherVideo.getDeleteTime() + "");
    }

    @OnClick(R.id.never_watch)
    void never_watch() {

    }

    @OnClick(R.id.back)
    void back() {
        this.dismiss();
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
