package de.dzmitry_lamaka.timingsdk.sdk.task;

import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import java.io.IOException;
import java.util.List;

import de.dzmitry_lamaka.timingsdk.sdk.persistance.Storage;
import de.dzmitry_lamaka.timingsdk.sdk.persistance.Time;
import de.dzmitry_lamaka.timingsdk.sdk.worker.Task;

public class SendAllTask implements Task {
    @NonNull
    private final Storage storage;
    @NonNull
    private final TimeTaskFactory timeTaskFactory;

    SendAllTask(@NonNull final Storage storage, @NonNull final TimeTaskFactory timeTaskFactory) {
        this.storage = storage;
        this.timeTaskFactory = timeTaskFactory;
    }

    @WorkerThread
    @Override
    public void perform() throws IOException {
        final List<Time> notSentItems = storage.getAll();
        for (final Time item : notSentItems) {
            timeTaskFactory.createTask(new Param(TaskType.SEND, item)).perform();
        }
    }

}
