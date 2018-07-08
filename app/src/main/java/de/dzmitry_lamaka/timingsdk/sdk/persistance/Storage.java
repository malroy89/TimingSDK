package de.dzmitry_lamaka.timingsdk.sdk.persistance;

import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import java.util.List;

public interface Storage {
    @WorkerThread
    void save(@NonNull Time time);

    @WorkerThread
    void delete(@NonNull Time time);

    @WorkerThread
    @NonNull
    List<Time> getAll();
}
