package io.parsetra.duration;

import java.time.Duration;

/**
 * Formats {@link java.time.Duration} as human-readable strings.
 * Thread-safe.
 */
public final class DurationFormatter {

    private static final long NANOS_PER_MS = 1_000_000L;
    private static final long NANOS_PER_S = 1_000_000_000L;
    private static final long NANOS_PER_M = 60 * NANOS_PER_S;
    private static final long NANOS_PER_H = 60 * NANOS_PER_M;
    private static final long NANOS_PER_D = 24 * NANOS_PER_H;

    private DurationFormatter() {}

    /**
     * Format as compact string, largest unit first (e.g. "2h30m", "150m", "1d").
     */
    public static String format(Duration duration) {
        if (duration == null) {
            throw new IllegalArgumentException("duration must not be null");
        }
        long totalNanos = duration.toNanos();
        if (totalNanos == 0) {
            return "0s";
        }
        boolean neg = totalNanos < 0;
        if (neg) totalNanos = -totalNanos;
        StringBuilder sb = new StringBuilder();
        if (neg) sb.append('-');
        long d = totalNanos / NANOS_PER_D;
        if (d > 0) {
            sb.append(d).append('d');
            totalNanos -= d * NANOS_PER_D;
        }
        long h = totalNanos / NANOS_PER_H;
        if (h > 0) {
            sb.append(h).append('h');
            totalNanos -= h * NANOS_PER_H;
        }
        long m = totalNanos / NANOS_PER_M;
        if (m > 0) {
            sb.append(m).append('m');
            totalNanos -= m * NANOS_PER_M;
        }
        long s = totalNanos / NANOS_PER_S;
        if (s > 0) {
            sb.append(s).append('s');
            totalNanos -= s * NANOS_PER_S;
        }
        long ms = totalNanos / NANOS_PER_MS;
        if (ms > 0) {
            sb.append(ms).append("ms");
        } else if (totalNanos > 0) {
            sb.append(totalNanos).append("ns");
        }
        if (sb.length() == (neg ? 1 : 0)) {
            sb.append("0s");
        }
        return sb.toString();
    }

    /**
     * Format as compact single unit, using the smallest unit that yields an integer (e.g. "150m" for 2h30m).
     */
    public static String formatCompact(Duration duration) {
        if (duration == null) {
            throw new IllegalArgumentException("duration must not be null");
        }
        long totalNanos = Math.abs(duration.toNanos());
        if (totalNanos == 0) {
            return "0s";
        }
        String prefix = duration.toNanos() < 0 ? "-" : "";
        // Use smallest unit that gives a whole number (prefer minutes for 150m over 2h for 2h30m)
        if (totalNanos % NANOS_PER_D == 0) {
            return prefix + (totalNanos / NANOS_PER_D) + "d";
        }
        if (totalNanos % NANOS_PER_H == 0) {
            return prefix + (totalNanos / NANOS_PER_H) + "h";
        }
        if (totalNanos % NANOS_PER_M == 0) {
            return prefix + (totalNanos / NANOS_PER_M) + "m";
        }
        if (totalNanos % NANOS_PER_S == 0) {
            return prefix + (totalNanos / NANOS_PER_S) + "s";
        }
        if (totalNanos % NANOS_PER_MS == 0) {
            return prefix + (totalNanos / NANOS_PER_MS) + "ms";
        }
        return prefix + totalNanos + "ns";
    }

    /**
     * Format with spaces between units (e.g. "2h 30m").
     */
    public static String formatHuman(Duration duration) {
        if (duration == null) {
            throw new IllegalArgumentException("duration must not be null");
        }
        long totalNanos = duration.toNanos();
        if (totalNanos == 0) {
            return "0s";
        }
        boolean neg = totalNanos < 0;
        if (neg) totalNanos = -totalNanos;
        StringBuilder sb = new StringBuilder();
        if (neg) sb.append("- ");
        boolean first = true;
        long d = totalNanos / NANOS_PER_D;
        if (d > 0) {
            if (!first) sb.append(' ');
            sb.append(d).append('d');
            totalNanos -= d * NANOS_PER_D;
            first = false;
        }
        long h = totalNanos / NANOS_PER_H;
        if (h > 0) {
            if (!first) sb.append(' ');
            sb.append(h).append('h');
            totalNanos -= h * NANOS_PER_H;
            first = false;
        }
        long m = totalNanos / NANOS_PER_M;
        if (m > 0) {
            if (!first) sb.append(' ');
            sb.append(m).append('m');
            totalNanos -= m * NANOS_PER_M;
            first = false;
        }
        long s = totalNanos / NANOS_PER_S;
        if (s > 0) {
            if (!first) sb.append(' ');
            sb.append(s).append('s');
            totalNanos -= s * NANOS_PER_S;
            first = false;
        }
        long ms = totalNanos / NANOS_PER_MS;
        if (ms > 0) {
            if (!first) sb.append(' ');
            sb.append(ms).append("ms");
        } else if (totalNanos > 0) {
            if (!first) sb.append(' ');
            sb.append(totalNanos).append("ns");
        }
        if (sb.length() == (neg ? 2 : 0)) {
            sb.append("0s");
        }
        return sb.toString();
    }
}
