package de.dzmitry_lamaka.timingsdk.sdk.di;

import android.support.annotation.NonNull;

import de.dzmitry_lamaka.timingsdk.sdk.persistance.Time;

public abstract class TestDependenciesProvider {
    @NonNull
    public abstract Time createTime();
}
