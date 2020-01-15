package com.example.spotter_workoutlog.database;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutor {

    private static AppExecutor sInstance;
    private static final int THREAD_COUNT = 3;
    private final Executor diskIO;
    private final Executor networkIO;
    private final Executor mainThread;

    AppExecutor(Executor diskIO, Executor networkIO, Executor mainThread) {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;
    }

    public static AppExecutor getInstance(){
        if (sInstance == null){
            sInstance = new AppExecutor(Executors.newSingleThreadExecutor(), Executors.newFixedThreadPool(THREAD_COUNT), new MainThreadExecutor());
        }
        return sInstance;
    }

    public Executor getDiskIO() {
        return diskIO;
    }

    public Executor getNetworkIO() {
        return networkIO;
    }

    public Executor getMainThread() {
        return mainThread;
    }

    private static class MainThreadExecutor implements Executor {
        private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
