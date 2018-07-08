package de.dzmitry_lamaka.timingsdk.sdk.di;

import android.support.annotation.NonNull;

import de.dzmitry_lamaka.timingsdk.sdk.persistance.Time;

public class TestDependencyFactory extends DefaultDependenciesFactory {
    @NonNull
    private final TestDependenciesProvider testDependenciesProvider;

    public TestDependencyFactory(@NonNull final Config config,
                                 @NonNull final TestDependenciesProvider testDependenciesProvider) {
        super(config);
        this.testDependenciesProvider = testDependenciesProvider;
    }

    @NonNull
    @Override
    public Time newTime() {
        return testDependenciesProvider.createTime();
    }
}
