package de.dzmitry_lamaka.timingsdk.sdk.di;

import android.support.annotation.NonNull;

public class DefaultConfig implements Config {
    @NonNull
    private static final String SHARED_PREFS_NAME = "adjust_task";
    @NonNull
    private static final String URL = "https://jsonplaceholder.typicode.com/posts";

    @NonNull
    @Override
    public String getUrl() {
        return URL;
    }

    @NonNull
    @Override
    public String getStorageName() {
        return SHARED_PREFS_NAME;
    }
}
