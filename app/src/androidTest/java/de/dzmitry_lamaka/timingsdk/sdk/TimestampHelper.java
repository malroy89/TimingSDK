package de.dzmitry_lamaka.timingsdk.sdk;

public class TimestampHelper {
    private final static long ONE_SECOND_MILLIS = 1000;
    private long initialTimestampMillis;

    public TimestampHelper(final long initialTimestampMillis) {
        this.initialTimestampMillis = initialTimestampMillis;
    }

    public long getTimestamp() {
        return initialTimestampMillis;
    }

    public long plusOneSecond() {
        return initialTimestampMillis = initialTimestampMillis + ONE_SECOND_MILLIS;
    }

    public long minusOneSecond() {
        return initialTimestampMillis = initialTimestampMillis - ONE_SECOND_MILLIS;
    }

    public long minusOneMillisecond() {
        return initialTimestampMillis = initialTimestampMillis - 1;
    }
}
