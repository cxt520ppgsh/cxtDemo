package com.flyaudio.packagemanager.model.pkg;

public class PkgInstaller extends AbstractPkgWorker {

    public PkgInstaller() {
        super(AbstractPkgWorker.CHECK);
    }


    protected void doWork(String message, InstallListener installListener) {
        installListener.onFinish();
    }
}
