package de.dzmitry_lamaka.timingsdk.sdk;

import android.content.Context;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import de.dzmitry_lamaka.timingsdk.sdk.di.Injector;

public class TimingSdk {
    @NonNull
    private static final TimingSdk INSTANCE = new TimingSdk(InjectorBuilder.build());
    @NonNull
    private final Injector injector;
    @Nullable
    private TimeProcessor processor;

    private TimingSdk(@NonNull final Injector injector) {
        this.injector = injector;
    }

    @NonNull
    public static TimingSdk getInstance() {
        return INSTANCE;
    }

    @MainThread
    public void onTrackSeconds(@NonNull final Context context) {
        getProcessor(context).process(injector.time());
    }

    @MainThread
    public void onResume(@NonNull final Context context) {
        getProcessor(context).processExisting();
    }

    @MainThread
    public void onDestroy() {
        if (processor != null) {
            processor.release();
            processor = null;
        }
    }

    @NonNull
    private TimeProcessor getProcessor(@NonNull final Context context) {
        if (processor == null) {
            processor = injector.timeProcessor(context);
        }
        return processor;
    }
}
