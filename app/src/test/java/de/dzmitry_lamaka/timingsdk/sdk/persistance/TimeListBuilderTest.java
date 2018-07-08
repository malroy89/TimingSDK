package de.dzmitry_lamaka.timingsdk.sdk.persistance;

import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeListBuilderTest {

    @Test
    public void build() {
        final Map<String, Object> values = new HashMap<>();
        values.put("key1", 1L);
        values.put("key2", "string");
        values.put("key3", 2L);
        values.put("key4", "string");
        List<Time> testSubject = new TimeListBuilder(values).build();
        assertThat(testSubject).hasSize(2);
        assertThat(testSubject.get(0).toString()).isEqualTo("1");
        assertThat(testSubject.get(1).toString()).isEqualTo("2");
    }

    @Test
    public void build_whenNoValues() {
        assertThat(new TimeListBuilder(Collections.<String, Object>emptyMap()).build()).isEmpty();
    }
}
