package com.flyaudio.packagemanager.model.pkg;

public interface InstallListener {
    public void onStart();

    public void onFinish();

    public void onFail(Throwable e);
}
