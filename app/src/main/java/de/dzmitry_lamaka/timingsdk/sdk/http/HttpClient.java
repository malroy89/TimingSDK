package de.dzmitry_lamaka.timingsdk.sdk.http;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

public class HttpClient {
    private static final int HTTP_CODE_BAD_REQUEST = 400;

    @WorkerThread
    @Nullable
    public HttpResponse execute(@NonNull final HttpRequest httpRequest) throws IOException {
        HttpURLConnection connection = null;
        InputStream stream = null;
        try {
            connection = buildConnection(httpRequest);
            final String body = httpRequest.getBody();
            if (body != null) {
                final OutputStream outputStream = connection.getOutputStream();
                outputStream.write(body.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();
            }
            final int statusCode = connection.getResponseCode();
            if (statusCode < HTTP_CODE_BAD_REQUEST) {
                stream = connection.getInputStream();
            } else {
                stream = connection.getErrorStream();
            }
            return new HttpResponse.Builder()
                .setData(new String(IOUtils.toByteArray(stream), Charset.forName("UTF-8")))
                .setStatusCode(statusCode)
                .build();
        } finally {
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    @NonNull
    private HttpURLConnection buildConnection(@NonNull final HttpRequest httpRequest) throws IOException {
        URL url = new URL(httpRequest.getUrl());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(httpRequest.getHttpMethod().name());
        for (final Map.Entry<String, String> entry : httpRequest.getHeaders().entrySet()) {
            connection.setRequestProperty(entry.getKey(), entry.getValue());
        }
        return connection;
    }

}
