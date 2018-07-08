package de.dzmitry_lamaka.timingsdk.sdk.di;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.HandlerThread;
import android.support.annotation.NonNull;

import de.dzmitry_lamaka.timingsdk.sdk.Logger;
import de.dzmitry_lamaka.timingsdk.sdk.SaveWorkerSuccessListener;
import de.dzmitry_lamaka.timingsdk.sdk.SendTimeApiCall;
import de.dzmitry_lamaka.timingsdk.sdk.SimpleLogger;
import de.dzmitry_lamaka.timingsdk.sdk.TimeProcessor;
import de.dzmitry_lamaka.timingsdk.sdk.http.HttpClient;
import de.dzmitry_lamaka.timingsdk.sdk.persistance.SharedPrefStorage;
import de.dzmitry_lamaka.timingsdk.sdk.persistance.Storage;
import de.dzmitry_lamaka.timingsdk.sdk.persistance.Time;
import de.dzmitry_lamaka.timingsdk.sdk.task.TimeTaskFactory;
import de.dzmitry_lamaka.timingsdk.sdk.task.TimeTaskFactoryImpl;
import de.dzmitry_lamaka.timingsdk.sdk.worker.Worker;

public class DefaultDependenciesFactory implements DependenciesFactory {
    @NonNull
    private final Config config;

    public DefaultDependenciesFactory(@NonNull final Config config) {
        this.config = config;
    }

    @NonNull
    @Override
    public TimeProcessor newProcessor(@NonNull Context context) {
        final Storage storage = newStorage(getSharedPrefs(context));
        final Logger logger = newLogger();
        final SendTimeApiCall sendTimeApiCall = newSendTimeApiCall(newHttpClient(), logger, config.getUrl());
        final TimeTaskFactory timeTaskFactory = newTaskFactory(storage, sendTimeApiCall);
        final Worker sendWorker = newSendWorker(newHandlerThread());
        final Worker saveWorker = newSaveWorker(newHandlerThread());
        saveWorker.setSuccessListener(newSuccessListener(sendWorker, timeTaskFactory));
        return new TimeProcessor(timeTaskFactory, saveWorker, sendWorker, logger);
    }

    @NonNull
    @Override
    public HttpClient newHttpClient() {
        return new HttpClient();
    }

    @NonNull
    @Override
    public Logger newLogger() {
        return new SimpleLogger();
    }

    @NonNull
    @Override
    public Time newTime() {
        return new Time();
    }

    @NonNull
    @Override
    public Worker newSendWorker(@NonNull final HandlerThread handlerThread) {
        return new Worker(handlerThread);
    }

    @NonNull
    @Override
    public Worker newSaveWorker(@NonNull final HandlerThread handlerThread) {
        return new Worker(handlerThread);
    }

    @NonNull
    @Override
    public Worker.SuccessListener newSuccessListener(@NonNull final Worker sendWorker,
                                                     @NonNull final TimeTaskFactory timeTaskFactory) {
        return new SaveWorkerSuccessListener(sendWorker, timeTaskFactory);
    }

    @NonNull
    @Override
    public TimeTaskFactory newTaskFactory(@NonNull final Storage storage,
                                          @NonNull final SendTimeApiCall sendTimeApiCall) {
        return new TimeTaskFactoryImpl(storage, sendTimeApiCall);
    }

    @NonNull
    @Override
    public SendTimeApiCall newSendTimeApiCall(@NonNull final HttpClient httpClient, @NonNull final Logger logger,
                                              @NonNull final String url) {
        return new SendTimeApiCall(httpClient, logger, url);
    }

    @NonNull
    @Override
    public Storage newStorage(@NonNull final SharedPreferences sharedPreferences) {
        return new SharedPrefStorage(sharedPreferences);
    }

    @NonNull
    @Override
    public SharedPreferences getSharedPrefs(@NonNull final Context context) {
        return context.getSharedPreferences(config.getStorageName(), Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public HandlerThread newHandlerThread() {
        return new HandlerThread("WorkerThread");
    }
}
