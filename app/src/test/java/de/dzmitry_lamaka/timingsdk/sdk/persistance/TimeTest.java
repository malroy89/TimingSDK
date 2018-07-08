package de.dzmitry_lamaka.timingsdk.sdk.persistance;

import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import de.dzmitry_lamaka.timingsdk.sdk.TimestampHelper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TimeTest {
    private static final long TIMESTAMP_MILLIS = 1530826842701L; // Thursday, July 5, 2018 9:40:42.701 PM

    @Test
    public void toPrefs() {
        final SharedPreferences.Editor editor = mock(SharedPreferences.Editor.class);
        new Time(100500L).toPrefs(editor);
        verify(editor).putLong("100500", 100500L);
    }

    @Test
    public void asJson() throws JSONException {
        final JSONObject testSubject = new Time(TIMESTAMP_MILLIS).asJson(mock(JSONObject.class));
        verify(testSubject).put("seconds", "42");
    }

    @Test
    public void isLessAndNotEqualTo() {
        final TimestampHelper timestampHelper = new TimestampHelper(TIMESTAMP_MILLIS);
        assertThat(new Time(TIMESTAMP_MILLIS).sameSecondWithinMinute(new Time(timestampHelper.minusOneSecond()))).isFalse();
        assertThat(new Time(TIMESTAMP_MILLIS).sameSecondWithinMinute(new Time(TIMESTAMP_MILLIS))).isTrue();
        assertThat(new Time(TIMESTAMP_MILLIS).sameSecondWithinMinute(new Time(timestampHelper.minusOneMillisecond()))).isTrue();
        assertThat(new Time(TIMESTAMP_MILLIS).sameSecondWithinMinute(new Time(timestampHelper.plusOneSecond()))).isFalse();
    }
}
