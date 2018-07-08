package de.dzmitry_lamaka.timingsdk.sdk.di;

import android.support.annotation.NonNull;

public interface Config {
    @NonNull
    String getUrl();

    @NonNull
    String getStorageName();
}
