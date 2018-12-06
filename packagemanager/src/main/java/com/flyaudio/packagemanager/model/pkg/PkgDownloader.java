package com.flyaudio.packagemanager.model.pkg;

import android.util.Log;

import com.flyaudio.packagemanager.model.net.download.DownloadCallBack;

import rx.Subscription;

public class PkgDownloader extends AbstractPkgWorker {
    private static final String TAG = "PkgDownloader";
    private Subscription subscription;
    private String url;
    private String fileName;
    private boolean stopDownload = false;
    private DownloadCallBack downloadCallback;

    public PkgDownloader() {
        super(AbstractPkgWorker.DOWNLOAD);
    }

    public void downloadFile(final String url, final String fileName, final DownloadCallBack downloadCallback) {

    }

    public void pause() {
        if (subscription != null) {
            stopDownload = true;
            subscription.unsubscribe();
        }
    }

    public void reStart() {
        Log.d(TAG, "reStart");
        downloadFile(url, fileName, downloadCallback);
    }

}

