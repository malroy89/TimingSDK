package de.dzmitry_lamaka.timingsdk.sdk;

public class TimestampHelper {
    private final static long ONE_SECOND_MILLIS = 1000;
    private final long initialTimestampMillis;

    public TimestampHelper(final long initialTimestampMillis) {
        this.initialTimestampMillis = initialTimestampMillis;
    }

    public long getTimestamp() {
        return initialTimestampMillis;
    }

    public long plusOneSecond() {
        return new TimestampHelper(initialTimestampMillis + ONE_SECOND_MILLIS).getTimestamp();
    }

    public long minusOneSecond() {
        return new TimestampHelper(initialTimestampMillis - ONE_SECOND_MILLIS).getTimestamp();
    }

    public long minusOneMillisecond() {
        return new TimestampHelper(initialTimestampMillis - 1).getTimestamp();
    }
}
