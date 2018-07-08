package de.dzmitry_lamaka.timingsdk.sdk.persistance;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TimeListBuilder {
    @NonNull
    private final Map<String, ?> allValues;

    TimeListBuilder(@NonNull final Map<String, ?> allValues) {
        this.allValues = Collections.unmodifiableMap(allValues);
    }

    @NonNull
    public List<Time> build() {
        if (allValues.isEmpty()) {
            return Collections.emptyList();
        }
        final List<Time> result = new ArrayList<>();
        for (Object value : allValues.values()) {
            if (value instanceof Long) {
                result.add(new Time((Long) value));
            }
        }
        return result;
    }
}
