package com.lukou.publishervideo.utils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import retrofit2.http.PUT;

/**
 * Created by cxt on 2018/6/18.
 */

public class ThreadPoolUtil {
    private static ThreadPoolExecutor singleThreadPool;

    public static void initThreadPool() {
        singleThreadPool = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(0));
        singleThreadPool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

    }

    public static ExecutorService getSingleThreadPool() {
        return singleThreadPool;
    }

    public static void setRunnable(Runnable task) {
        singleThreadPool.execute(task);
    }

    private class MyThread extends Thread {

    }

}
