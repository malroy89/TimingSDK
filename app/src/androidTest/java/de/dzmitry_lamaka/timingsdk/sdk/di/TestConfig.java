package de.dzmitry_lamaka.timingsdk.sdk.di;

import android.support.annotation.NonNull;

public class TestConfig implements Config {
    @NonNull
    private final String url;
    @NonNull
    private final String storageName;

    public TestConfig(@NonNull final String url, @NonNull final String storageName) {
        this.url = url;
        this.storageName = storageName;
    }

    @NonNull
    @Override
    public String getUrl() {
        return url;
    }

    @NonNull
    @Override
    public String getStorageName() {
        return storageName;
    }
}
