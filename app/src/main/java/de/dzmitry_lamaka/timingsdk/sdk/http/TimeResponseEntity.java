package de.dzmitry_lamaka.timingsdk.sdk.http;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

public class TimeResponseEntity {
    @NonNull
    private static final String JSON_FIELD_ID = "id";
    @NonNull
    private static final String JSON_FIELD_SECONDS = "seconds";
    @NonNull
    private final String seconds;
    private final int id;

    private TimeResponseEntity(final int id, @NonNull final String seconds) {
        this.id = id;
        this.seconds = seconds;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getSeconds() {
        return seconds;
    }

    public static class Builder {
        @Nullable
        private Integer id;
        @Nullable
        private final String seconds;

        public Builder(@NonNull final String data) {
            try {
                final JSONObject json = new JSONObject(data);
                if (json.optInt(JSON_FIELD_ID, -1) != -1) {
                    id = json.optInt(JSON_FIELD_ID);
                }
                seconds = json.optString(JSON_FIELD_SECONDS, null);
            } catch (@NonNull final JSONException e) {
                throw new IllegalStateException("Cannot parse data: [" + data + "]");
            }
        }

        @Nullable
        public TimeResponseEntity build() {
            if (id == null || seconds == null || seconds.isEmpty()) {
                return null;
            }
            return new TimeResponseEntity(id, seconds);
        }
    }
}
