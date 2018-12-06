package com.flyaudio.packagemanager.model.pkg;

public abstract class AbstractPkgWorker {
    public static int CHECK = 1;
    public static int DOWNLOAD = 2;
    public static int INSTALL = 3;
    public static int FINISH = 4;
    public static int ERROR = 5;

    protected int level;

    private AbstractPkgWorker() {

    }

    public AbstractPkgWorker(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

}
