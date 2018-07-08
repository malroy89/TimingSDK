package de.dzmitry_lamaka.timingsdk.sdk;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import de.dzmitry_lamaka.timingsdk.sdk.http.HttpClient;
import de.dzmitry_lamaka.timingsdk.sdk.http.HttpRequest;
import de.dzmitry_lamaka.timingsdk.sdk.http.HttpResponse;
import de.dzmitry_lamaka.timingsdk.sdk.persistance.Time;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SendTimeApiCallTest {
    @Mock
    private HttpClient httpClient;
    @Mock
    private Logger logger;
    @Mock
    private Time time;
    @Mock
    private JSONObject json;
    @Mock
    private HttpResponse response;

    private SendTimeApiCall testSubject;

    @Before
    public void setUp() throws Exception {
        when(time.asJson(any(JSONObject.class))).thenReturn(json);
        when(json.toString()).thenReturn("request_body");
        testSubject = new SendTimeApiCall(httpClient, logger, "url");
    }

    @Test(expected = IOException.class)
    public void send_whenResponseIsNull() throws IOException {
        when(httpClient.execute(any(HttpRequest.class))).thenReturn(null);
        testSubject.send(time);
    }

    @Test(expected = IOException.class)
    public void send_whenStatusCodeIsInvalid() throws IOException {
        when(httpClient.execute(any(HttpRequest.class))).thenReturn(response);
        when(response.getStatusCode()).thenReturn(400);
        testSubject.send(time);
    }

    @Test(expected = IOException.class)
    public void send_whenUnexpectedResponse() throws IOException {
        when(httpClient.execute(any(HttpRequest.class))).thenReturn(response);
        when(response.getStatusCode()).thenReturn(200);
        when(response.getData()).thenReturn("{\"seconds\":\"\"}");
        testSubject.send(time);
    }

    @Test(expected = IOException.class)
    public void send_whenCannotConvertTimeToJson() throws IOException, JSONException {
        when(time.asJson(any(JSONObject.class))).thenThrow(new JSONException("Test"));
        testSubject.send(time);
    }

    @Test(expected = IOException.class)
    public void send_whenExecuteThrowsException() throws IOException {
        when(httpClient.execute(any(HttpRequest.class))).thenThrow(new IOException());
        testSubject.send(time);
    }

    @Test
    public void send() throws IOException {
        when(httpClient.execute(any(HttpRequest.class))).thenReturn(response);
        when(response.getStatusCode()).thenReturn(200);
        when(response.getData()).thenReturn("{\"seconds\": \"45\", \"id\": 101}");
        testSubject.send(time);
        verify(logger).d("id: [101], seconds: [45]");
    }
}
