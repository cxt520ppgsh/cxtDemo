package com.flyaudio.packagemanager.model.net.download;

public interface DownloadCallBack {
    void onProgress(int progress);
    void onError(String msg);
    void onCompleted();
}
