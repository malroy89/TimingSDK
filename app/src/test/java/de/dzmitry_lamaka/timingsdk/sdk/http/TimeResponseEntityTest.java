package de.dzmitry_lamaka.timingsdk.sdk.http;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeResponseEntityTest {

    @Test
    public void build() {
        final TimeResponseEntity testSubject =
            new TimeResponseEntity.Builder("{\"seconds\": \"45\", \"id\": 101}").build();
        assertThat(testSubject).isNotNull();
        assertThat(testSubject.getId()).isEqualTo(101);
        assertThat(testSubject.getSeconds()).isEqualTo("45");
    }

    @Test
    public void build_whenIdIsMissing() {
        final TimeResponseEntity testSubject =
            new TimeResponseEntity.Builder("{\"seconds\": \"45\"}").build();
        assertThat(testSubject).isNull();
    }

    @Test
    public void build_whenSecondsNull() {
        final TimeResponseEntity testSubject =
            new TimeResponseEntity.Builder("{\"seconds\": null, \"id\": 101}").build();
        assertThat(testSubject).isNull();
    }

    @Test
    public void build_whenSecondsEmpty() {
        final TimeResponseEntity testSubject =
            new TimeResponseEntity.Builder("{\"seconds\": \"\", \"id\": 101}").build();
        assertThat(testSubject).isNull();
    }

    @Test
    public void build_whenSecondsIsMissing() {
        final TimeResponseEntity testSubject =
            new TimeResponseEntity.Builder("{\"id\": 101}").build();
        assertThat(testSubject).isNull();
    }

    @Test(expected = IllegalStateException.class)
    public void build_whenInvalidJson() {
        new TimeResponseEntity.Builder("{invalid_json}").build();
    }
}
