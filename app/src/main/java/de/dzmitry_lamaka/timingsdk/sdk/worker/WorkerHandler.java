package de.dzmitry_lamaka.timingsdk.sdk.worker;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;

public class WorkerHandler extends Handler {
    private static final int MSG_ID_EXECUTE_TASK = 0;

    WorkerHandler(Looper looper) {
        super(looper);
    }

    public void sendTask(@NonNull final Task task, @Nullable final Worker.SuccessListener successListener,
                         @Nullable final Worker.ErrorListener errorListener) {
        obtainMessage(MSG_ID_EXECUTE_TASK, new HandlerDataHolder(task, successListener, errorListener)).sendToTarget();
    }

    @Override
    public void handleMessage(@NonNull final Message msg) {
        switch (msg.what) {
            case MSG_ID_EXECUTE_TASK:
                final HandlerDataHolder handlerDataHolder = (HandlerDataHolder) msg.obj;
                executeTask(handlerDataHolder.task, handlerDataHolder.successListener, handlerDataHolder.errorListener);
                break;
            default:
                throw new IllegalStateException("Cannot process msg: [" + msg + "]");
        }
    }

    private void executeTask(@NonNull final Task task, @Nullable final Worker.SuccessListener successListener,
                             @Nullable final Worker.ErrorListener errorListener) {
        try {
            task.perform();
            if (successListener != null) {
                successListener.onSuccess(task);
            }
        } catch (@NonNull final IOException e) {
            if (errorListener != null) {
                errorListener.onFailed(e);
            }
        }
    }

    private static class HandlerDataHolder {
        @NonNull
        private final Task task;
        @Nullable
        private final Worker.SuccessListener successListener;
        @Nullable
        private final Worker.ErrorListener errorListener;

        private HandlerDataHolder(@NonNull final Task task, @Nullable final Worker.SuccessListener successListener,
                                  @Nullable final Worker.ErrorListener errorListener) {
            this.task = task;
            this.successListener = successListener;
            this.errorListener = errorListener;
        }
    }
}
