package com.flyaudio.packagemanager.model.pkg;


import com.flyaudio.packagemanager.model.net.download.DownloadCallBack;

import rx.Observable;
import rx.schedulers.Schedulers;

public class PkgInstallManager {

    private PkgChecker pkgChecker;
    private PkgDownloader pkgDownloader;
    private PkgInstaller pkgInstaller;

    public static PkgInstallManager getInstance() {
        return SingletonInstance.INSTANCE;
    }

    public void install(String url, DownloadCallBack downloadCallBack, InstallListener installListener) {
        Observable.just(url)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(url1 -> {
                    pkgChecker.doWork(url1);
                    return url1;
                }).map(url12 -> {
            pkgDownloader.downloadFile(url, "123.apk", new DownloadCallBack() {
                @Override
                public void onProgress(int progress) {
                    downloadCallBack.onProgress(progress);
                }

                @Override
                public void onCompleted() {
                    downloadCallBack.onCompleted();
                    pkgInstaller.doWork("", new InstallListener() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onFinish() {

                        }

                        @Override
                        public void onFail(Throwable e) {

                        }
                    });
                }

                @Override
                public void onError(String msg) {
                    downloadCallBack.onError(msg);
                }
            });
            return url12;
        }).subscribe();
    }

    public void pause(String url){
        pkgDownloader.pause();
    }

    public void reStart(String url){
        pkgDownloader.reStart();
    }

    private PkgInstallManager() {
        pkgChecker = new PkgChecker();
        pkgDownloader = new PkgDownloader();
        pkgInstaller = new PkgInstaller();
    }

    private static class SingletonInstance {
        private static final PkgInstallManager INSTANCE = new PkgInstallManager();
    }

}
