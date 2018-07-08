package de.dzmitry_lamaka.timingsdk.sdk.task;

import android.support.annotation.NonNull;

import de.dzmitry_lamaka.timingsdk.sdk.worker.Task;

public interface TimeTaskFactory {
    @NonNull
    Task createTask(@NonNull Param param);
}
