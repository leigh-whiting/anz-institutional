package util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Helper functions for dealing with dates and time
 */
public final class DateTimeUtil {

    /**
     * Create an Instant from the given date in UTC.
     */
    public static Instant utcInstant(final int year, final int month, final int day) {
        return LocalDateTime.of(year, month, day, 0, 0).toInstant(ZoneOffset.UTC);
    }
}
