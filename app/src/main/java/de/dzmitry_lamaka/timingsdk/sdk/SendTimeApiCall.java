package de.dzmitry_lamaka.timingsdk.sdk;

import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import de.dzmitry_lamaka.timingsdk.sdk.http.HttpClient;
import de.dzmitry_lamaka.timingsdk.sdk.http.HttpMethod;
import de.dzmitry_lamaka.timingsdk.sdk.http.HttpRequest;
import de.dzmitry_lamaka.timingsdk.sdk.http.HttpResponse;
import de.dzmitry_lamaka.timingsdk.sdk.http.TimeResponseEntity;
import de.dzmitry_lamaka.timingsdk.sdk.persistance.Time;

public class SendTimeApiCall {
    private static final int HTTP_CODE_BAD_REQUEST = 400;
    @NonNull
    private static final String HEADER_KEY_CONTENT_TYPE = "Content-Type";
    @NonNull
    private static final String CONTENT_TYPE_JSON = "application/json; charset=UTF-8";
    @NonNull
    private static final String LOG_MESSAGE = "id: [%s], seconds: [%s]";
    @NonNull
    private final HttpClient httpClient;
    @NonNull
    private final Logger logger;
    @NonNull
    private final String url;

    public SendTimeApiCall(@NonNull final HttpClient httpClient, @NonNull final Logger logger, @NonNull final String url) {
        this.httpClient = httpClient;
        this.logger = logger;
        this.url = url;
    }

    @WorkerThread
    public void send(@NonNull final Time time) throws IOException {
        try {
            final HttpRequest httpRequest = new HttpRequest.Builder()
                .setUrl(url)
                .setBody(time.asJson(new JSONObject()).toString())
                .setHttpMethod(HttpMethod.POST)
                .addHeader(HEADER_KEY_CONTENT_TYPE, CONTENT_TYPE_JSON)
                .build();
            final HttpResponse httpResponse = httpClient.execute(httpRequest);
            if (httpResponse == null) {
                throw new IOException("Failed to send [" + time + "]. Reason: received invalid response");
            }
            int statusCode = httpResponse.getStatusCode();
            if (statusCode >= HTTP_CODE_BAD_REQUEST) {
                throw new IOException("Failed to send [" + time + "]. Reason: status code: [" + statusCode + "]");
            }
            final String response = httpResponse.getData();
            final TimeResponseEntity timeResponseEntity = new TimeResponseEntity.Builder(response).build();
            if (timeResponseEntity == null) {
                throw new IOException("Failed to send [" + time + "]. Reason: cannot parse response: [" + response + "]");
            }
            logger.d(String.format(LOG_MESSAGE, timeResponseEntity.getId(), timeResponseEntity.getSeconds()));
        } catch (@NonNull final IOException | JSONException e) {
            throw new IOException("Failed to send [" + time + "]. Reason: [" + e.getMessage() + "]");
        }
    }

}
