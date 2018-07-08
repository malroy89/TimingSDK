package de.dzmitry_lamaka.timingsdk.sdk;

import android.support.annotation.NonNull;
import android.util.Log;

public class SimpleLogger implements Logger {
    @NonNull
    private static final String TAG = "Timing_SDK";

    public SimpleLogger() {
    }

    @Override
    public void d(@NonNull final String message) {
        Log.d(TAG, message);
    }
}
