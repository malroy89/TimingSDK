package de.dzmitry_lamaka.timingsdk.sdk.worker;

import android.os.HandlerThread;
import android.support.annotation.AnyThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


public class Worker {
    @NonNull
    private final HandlerThread handlerThread;
    @Nullable
    private volatile WorkerHandler handler;
    @Nullable
    private SuccessListener successListener;
    @Nullable
    private ErrorListener errorListener;
    @NonNull
    private final Object locker = new Object();

    public Worker(@NonNull final HandlerThread handlerThread) {
        this.handlerThread = handlerThread;
    }

    public void setSuccessListener(@Nullable final SuccessListener successListener) {
        this.successListener = successListener;
    }

    public void setErrorListener(@Nullable final ErrorListener errorListener) {
        this.errorListener = errorListener;
    }

    @AnyThread
    public void addTask(@NonNull final Task task) {
        getHandler().sendTask(task, successListener, errorListener);
    }

    public void release() {
        handlerThread.quit();
    }

    @NonNull
    private WorkerHandler getHandler() {
        WorkerHandler result = handler;
        if (result == null) {
            synchronized (locker) {
                result = handler;
                if (result == null) {
                    handlerThread.start();
                    handler = result = new WorkerHandler(handlerThread.getLooper());
                }
            }
        }
        return result;
    }

    public interface SuccessListener {
        void onSuccess(@NonNull Task task);
    }

    public interface ErrorListener {
        void onFailed(@NonNull Exception reason);
    }
}
