package de.dzmitry_lamaka.timingsdk.sdk.task;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import de.dzmitry_lamaka.timingsdk.sdk.SendTimeApiCall;
import de.dzmitry_lamaka.timingsdk.sdk.persistance.Storage;
import de.dzmitry_lamaka.timingsdk.sdk.persistance.Time;
import de.dzmitry_lamaka.timingsdk.sdk.worker.Task;

public class TimeTaskFactoryImpl implements TimeTaskFactory {
    @NonNull
    private final Storage storage;
    @NonNull
    private final SendTimeApiCall sendTimeApiCall;

    public TimeTaskFactoryImpl(@NonNull final Storage storage, @NonNull final SendTimeApiCall sendTimeApiCall) {
        this.storage = storage;
        this.sendTimeApiCall = sendTimeApiCall;
    }

    @NonNull
    @Override
    public Task createTask(@NonNull final Param param) {
        final Time time = param.time;
        final TaskType taskType = param.taskType;
        switch (taskType) {
            case SAVE:
                throwIfNull(time);
                return new SaveTask(storage, time);
            case SEND:
                throwIfNull(time);
                return new SendTask(sendTimeApiCall, storage, time);
            case SEND_ALL:
                return new SendAllTask(storage, this);
            default:
                throw new IllegalStateException("Unhandled task type: [" + taskType + "]");
        }
    }

    private void throwIfNull(@Nullable final Time time) {
        if (time == null) {
            throw new IllegalStateException("Cannot be null");
        }
    }

}
