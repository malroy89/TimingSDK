package de.dzmitry_lamaka.timingsdk.sdk.persistance;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Time {
    @NonNull
    private static final String JSON_FIELD_SECONDS = "seconds";
    @NonNull
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("ss", Locale.getDefault());
    private long timestamp;

    public Time() {
        this.timestamp = System.currentTimeMillis();
    }

    public Time(final long timestamp) {
        this.timestamp = timestamp;
    }

    public void toPrefs(@NonNull final SharedPreferences.Editor prefs) {
        prefs.putLong(getPrefKey(), timestamp);
    }

    @NonNull
    public String getPrefKey() {
        return toString();
    }

    @NonNull
    public JSONObject asJson(@NonNull final JSONObject jsonObject) throws JSONException {
        jsonObject.put(JSON_FIELD_SECONDS, SIMPLE_DATE_FORMAT.format(new Date(timestamp)));
        return jsonObject;
    }

    @Override
    public String toString() {
        return String.valueOf(timestamp);
    }

    public boolean sameSecondWithinMinute(@NonNull final Time compareTo) {
        return compare(toEpochTimestamp(timestamp), toEpochTimestamp(compareTo.timestamp)) == 0;
    }

    private static long toEpochTimestamp(long timestampMillis) {
        return timestampMillis / 1000;
    }

    private static int compare(final long x, final long y) {
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }
}
