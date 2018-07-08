package de.dzmitry_lamaka.timingsdk.sdk.task;

import android.support.annotation.NonNull;

import de.dzmitry_lamaka.timingsdk.sdk.persistance.Storage;
import de.dzmitry_lamaka.timingsdk.sdk.persistance.Time;
import de.dzmitry_lamaka.timingsdk.sdk.worker.Task;

public class SaveTask implements Task {
    @NonNull
    private final Storage storage;
    @NonNull
    private final Time time;

    SaveTask(@NonNull final Storage storage, @NonNull final Time time) {
        this.storage = storage;
        this.time = time;
    }

    @NonNull
    public Time getTime() {
        return time;
    }

    @Override
    public void perform() {
        storage.save(time);
    }
}
