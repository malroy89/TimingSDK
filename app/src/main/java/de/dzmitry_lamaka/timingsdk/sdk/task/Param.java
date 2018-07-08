package de.dzmitry_lamaka.timingsdk.sdk.task;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import de.dzmitry_lamaka.timingsdk.sdk.persistance.Time;

public class Param {
    @NonNull
    public final TaskType taskType;
    @Nullable
    public final Time time;

    public Param(@NonNull final TaskType taskType, @Nullable final Time time) {
        this.taskType = taskType;
        this.time = time;
    }

    public Param(@NonNull final TaskType taskType) {
        this(taskType, null);
    }

}
