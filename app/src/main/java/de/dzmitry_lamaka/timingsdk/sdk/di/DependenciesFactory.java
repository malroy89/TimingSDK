package de.dzmitry_lamaka.timingsdk.sdk.di;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.HandlerThread;
import android.support.annotation.NonNull;

import de.dzmitry_lamaka.timingsdk.sdk.Logger;
import de.dzmitry_lamaka.timingsdk.sdk.SendTimeApiCall;
import de.dzmitry_lamaka.timingsdk.sdk.TimeProcessor;
import de.dzmitry_lamaka.timingsdk.sdk.http.HttpClient;
import de.dzmitry_lamaka.timingsdk.sdk.persistance.Storage;
import de.dzmitry_lamaka.timingsdk.sdk.persistance.Time;
import de.dzmitry_lamaka.timingsdk.sdk.task.TimeTaskFactory;
import de.dzmitry_lamaka.timingsdk.sdk.worker.Worker;

public interface DependenciesFactory {
    @NonNull
    TimeProcessor newProcessor(@NonNull final Context context);

    @NonNull
    HttpClient newHttpClient();

    @NonNull
    Logger newLogger();

    @NonNull
    Time newTime();

    @NonNull
    Worker newSendWorker(@NonNull final HandlerThread handlerThread);

    @NonNull
    Worker newSaveWorker(@NonNull final HandlerThread handlerThread);

    @NonNull
    Worker.SuccessListener newSuccessListener(
        @NonNull final Worker sendWorker,
        @NonNull final TimeTaskFactory timeTaskFactory
    );

    @NonNull
    TimeTaskFactory newTaskFactory(
        @NonNull final Storage storage,
        @NonNull final SendTimeApiCall sendTimeApiCall
    );

    @NonNull
    SendTimeApiCall newSendTimeApiCall(
        @NonNull final HttpClient httpClient, @NonNull final Logger logger,
        @NonNull final String url
    );
    @NonNull
    Storage newStorage(@NonNull final SharedPreferences sharedPreferences);

    @NonNull
    SharedPreferences getSharedPrefs(@NonNull final Context context);

    @NonNull
    HandlerThread newHandlerThread();
}
