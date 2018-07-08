package de.dzmitry_lamaka.timingsdk.sdk.task;

import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import java.io.IOException;

import de.dzmitry_lamaka.timingsdk.sdk.SendTimeApiCall;
import de.dzmitry_lamaka.timingsdk.sdk.persistance.Storage;
import de.dzmitry_lamaka.timingsdk.sdk.persistance.Time;
import de.dzmitry_lamaka.timingsdk.sdk.worker.Task;

public class SendTask implements Task {
    @NonNull
    private final SendTimeApiCall sendTimeApiCall;
    @NonNull
    private final Storage storage;
    @NonNull
    private final Time time;

    SendTask(@NonNull final SendTimeApiCall sendTimeApiCall, @NonNull final Storage storage, @NonNull final Time time) {
        this.sendTimeApiCall = sendTimeApiCall;
        this.storage = storage;
        this.time = time;
    }

    @WorkerThread
    @Override
    public void perform() throws IOException {
        sendTimeApiCall.send(time);
        storage.delete(time);
    }

}
