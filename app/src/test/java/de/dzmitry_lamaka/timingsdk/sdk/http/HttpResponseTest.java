package de.dzmitry_lamaka.timingsdk.sdk.http;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HttpResponseTest {

    @Test
    public void build() {
        final HttpResponse testSubject = new HttpResponse.Builder()
            .setData("data")
            .setStatusCode(200)
            .build();
        assertThat(testSubject).isNotNull();
        assertThat(testSubject.getData()).isEqualTo("data");
        assertThat(testSubject.getStatusCode()).isEqualTo(200);
    }

    @Test
    public void build_whenDataIsNull() {
        final HttpResponse testSubject = new HttpResponse.Builder()
            .setStatusCode(200)
            .build();
        assertThat(testSubject).isNotNull();
        assertThat(testSubject.getData()).isEqualTo("");
        assertThat(testSubject.getStatusCode()).isEqualTo(200);
    }

    @Test
    public void build_whenStatusCodeIsInvalid() {
        assertThat(new HttpResponse.Builder().setStatusCode(10).build()).isNull();
        assertThat(new HttpResponse.Builder().setStatusCode(600).build()).isNull();
    }
}
