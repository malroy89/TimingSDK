package de.dzmitry_lamaka.timingsdk.sdk.http;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    @NonNull
    private final HttpMethod httpMethod;
    @NonNull
    private final String url;
    @Nullable
    private final String body;
    @NonNull
    private Map<String, String> headers;

    private HttpRequest(@NonNull final HttpMethod httpMethod, @NonNull final String url, @Nullable final String body,
        @NonNull final Map<String, String> headers) {
        this.httpMethod = httpMethod;
        this.url = url;
        this.body = body;
        this.headers = Collections.unmodifiableMap(headers);
    }

    @NonNull
    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    @NonNull
    public String getUrl() {
        return url;
    }

    @Nullable
    public String getBody() {
        return body;
    }

    @NonNull
    public Map<String, String> getHeaders() {
        return headers;
    }

    public static class Builder {
        @Nullable
        private HttpMethod httpMethod;
        @Nullable
        private String url;
        @Nullable
        private String body;
        @Nullable
        private Map<String, String> headers;

        @NonNull
        public Builder setHttpMethod(@Nullable final HttpMethod httpMethod) {
            this.httpMethod = httpMethod;
            return this;
        }

        @NonNull
        public Builder setUrl(@Nullable final String url) {
            this.url = url;
            return this;
        }

        @NonNull
        public Builder setBody(@Nullable final String body) {
            this.body = body;
            return this;
        }

        @NonNull
        public Builder addHeader(@NonNull final String name, @NonNull final String value) {
            if (headers == null) {
                headers = new HashMap<>();
            }
            headers.put(name, value);
            return this;
        }

        @NonNull
        public HttpRequest build() {
            if (url == null || url.isEmpty()) {
                throw new IllegalStateException("Url is empty!");
            }
            if (httpMethod == null) {
                httpMethod = HttpMethod.POST;
            }
            if (headers == null) {
                headers = Collections.emptyMap();
            }
            return new HttpRequest(httpMethod, url, body, headers);
        }
    }
}
