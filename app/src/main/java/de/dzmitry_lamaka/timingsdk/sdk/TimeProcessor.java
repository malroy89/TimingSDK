package de.dzmitry_lamaka.timingsdk.sdk;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import de.dzmitry_lamaka.timingsdk.sdk.persistance.Time;
import de.dzmitry_lamaka.timingsdk.sdk.task.Param;
import de.dzmitry_lamaka.timingsdk.sdk.task.TaskType;
import de.dzmitry_lamaka.timingsdk.sdk.task.TimeTaskFactory;
import de.dzmitry_lamaka.timingsdk.sdk.worker.Task;
import de.dzmitry_lamaka.timingsdk.sdk.worker.Worker;

public class TimeProcessor {
    @NonNull
    private final TimeTaskFactory timeTaskFactory;
    @NonNull
    private final Worker saveWorker;
    @NonNull
    private final Worker sendWorker;
    @NonNull
    private final Logger logger;
    @Nullable
    private Time previous;

    public TimeProcessor(@NonNull final TimeTaskFactory timeTaskFactory, @NonNull final Worker saveWorker,
                         @NonNull final Worker sendWorker, @NonNull final Logger logger) {
        this.timeTaskFactory = timeTaskFactory;
        this.saveWorker = saveWorker;
        this.sendWorker = sendWorker;
        this.logger = logger;
    }

    @MainThread
    void process(@NonNull final Time time) {
        if (isSameSecondWithinMinute(time)) {
            logger.d("Same second within the minute");
            return;
        }
        previous = time;
        final Task saveTask = timeTaskFactory.createTask(new Param(TaskType.SAVE, time));
        saveWorker.addTask(saveTask);
    }

    private boolean isSameSecondWithinMinute(Time time) {
        return previous != null && time.sameSecondWithinMinute(previous);
    }

    @MainThread
    void processExisting() {
        final Task sendAllTask = timeTaskFactory.createTask(new Param(TaskType.SEND_ALL));
        sendWorker.addTask(sendAllTask);
    }

    void release() {
        saveWorker.release();
        sendWorker.release();
    }
}
