package de.dzmitry_lamaka.timingsdk.sdk.worker;

import android.support.annotation.WorkerThread;

import java.io.IOException;

public interface Task {
    @WorkerThread
    void perform() throws IOException;
}
