package de.dzmitry_lamaka.timingsdk.sdk;

import android.support.annotation.NonNull;

import de.dzmitry_lamaka.timingsdk.sdk.di.DefaultConfig;
import de.dzmitry_lamaka.timingsdk.sdk.di.DefaultDependenciesFactory;
import de.dzmitry_lamaka.timingsdk.sdk.di.Injector;

class InjectorBuilder {

    private InjectorBuilder() {
    }

    @NonNull
    static Injector build() {
        return new Injector(new DefaultDependenciesFactory(new DefaultConfig()));
    }
}
