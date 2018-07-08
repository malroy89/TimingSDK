package de.dzmitry_lamaka.timingsdk.sdk.http;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HttpRequestTest {

    @Test(expected = IllegalStateException.class)
    public void build_whenUrlIsNull() {
        new HttpRequest.Builder()
            .setBody("body")
            .addHeader("key","value")
            .setHttpMethod(HttpMethod.POST)
            .build();
    }

    @Test(expected = IllegalStateException.class)
    public void build_whenUrlIsEmpty() {
        new HttpRequest.Builder()
            .setUrl("")
            .setBody("body")
            .addHeader("key","value")
            .setHttpMethod(HttpMethod.POST)
            .build();
    }

    @Test
    public void build_whenHttpMethodIsNull_whenHeadersIsNull() {
        final HttpRequest testSubject = new HttpRequest.Builder().setUrl("url").build();
        assertThat(testSubject).isNotNull();
        assertThat(testSubject.getHttpMethod()).isEqualTo(HttpMethod.POST);
        assertThat(testSubject.getHeaders()).isEmpty();
    }

    @Test
    public void build() {
        final HttpRequest testSubject = new HttpRequest.Builder()
            .setUrl("url")
            .setBody("body")
            .setHttpMethod(HttpMethod.GET)
            .addHeader("key1", "value1")
            .addHeader("key2", "value2")
            .build();
        assertThat(testSubject).isNotNull();
        assertThat(testSubject.getHttpMethod()).isEqualTo(HttpMethod.GET);
        assertThat(testSubject.getHeaders()).hasSize(2).containsKeys("key1", "key2").containsValues("value1", "value2");
        assertThat(testSubject.getBody()).isEqualTo("body");
        assertThat(testSubject.getUrl()).isEqualTo("url");
    }

}
