package de.dzmitry_lamaka.timingsdk.sdk.di;

import android.content.Context;
import android.support.annotation.NonNull;

import de.dzmitry_lamaka.timingsdk.sdk.TimeProcessor;
import de.dzmitry_lamaka.timingsdk.sdk.persistance.Time;

public class Injector {
    @NonNull
    private final DependenciesFactory dependenciesFactory;

    public Injector(@NonNull final DependenciesFactory dependenciesFactory) {
        this.dependenciesFactory = dependenciesFactory;
    }

    @NonNull
    public TimeProcessor timeProcessor(@NonNull final Context context) {
        return dependenciesFactory.newProcessor(context);
    }

    @NonNull
    public Time time() {
        return dependenciesFactory.newTime();
    }
}
