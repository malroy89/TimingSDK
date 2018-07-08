package de.dzmitry_lamaka.timingsdk.sdk.http;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class HttpResponse {
    @NonNull
    private final String response;
    private final int statusCode;

    private HttpResponse(int statusCode, @NonNull String data) {
        this.statusCode = statusCode;
        this.response = data;
    }

    @NonNull
    public String getData() {
        return response;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public static class Builder {
        @Nullable
        private String data;
        @Nullable
        private Integer statusCode;

        @NonNull
        public Builder setData(@Nullable final String data) {
            this.data = data;
            return this;
        }

        @NonNull
        public Builder setStatusCode(@Nullable final Integer statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        @Nullable
        public HttpResponse build() {
            if (!isValidStatusCode(statusCode)) {
                return null;
            }
            if (data == null) {
                data = "";
            }
            return new HttpResponse(statusCode, data);
        }

        private static boolean isValidStatusCode(@Nullable final Integer statusCode) {
            return statusCode != null && statusCode >= 100 && statusCode < 600;
        }
    }
}
