package io.parsetra.size;

import io.parsetra.util.Validation;

/**
 * Formats byte counts as human-readable size strings.
 * Thread-safe.
 */
public final class SizeFormatter {

    private static final long K = 1000L;
    private static final long KI = 1024L;

    private static final long[] DECIMAL_UNITS = { 1, K, K*K, K*K*K, K*K*K*K };
    private static final String[] DECIMAL_SUFFIX = { "B", "KB", "MB", "GB", "TB" };

    private static final long[] BINARY_UNITS = { 1, KI, KI*KI, KI*KI*KI, KI*KI*KI*KI };
    private static final String[] BINARY_SUFFIX = { "B", "KiB", "MiB", "GiB", "TiB" };

    private SizeFormatter() {}

    /**
     * Format bytes using decimal units (1000-based), e.g. "1.5KB", "10MB".
     */
    public static String format(long bytes) {
        return formatWithUnits(bytes, DECIMAL_UNITS, DECIMAL_SUFFIX);
    }

    /**
     * Format bytes using binary units (1024-based), e.g. "1.5KiB", "10MiB".
     */
    public static String formatBinary(long bytes) {
        return formatWithUnits(bytes, BINARY_UNITS, BINARY_SUFFIX);
    }

    /**
     * Format as exact byte count, e.g. "1536B".
     */
    public static String formatBytes(long bytes) {
        Validation.requireNonNegative(bytes, "bytes");
        return bytes + "B";
    }

    private static String formatWithUnits(long bytes, long[] units, String[] suffixes) {
        Validation.requireNonNegative(bytes, "bytes");
        if (bytes == 0) {
            return "0B";
        }
        int i = units.length - 1;
        while (i > 0 && bytes < units[i]) {
            i--;
        }
        long unit = units[i];
        String suffix = suffixes[i];
        if (bytes % unit == 0) {
            return (bytes / unit) + suffix;
        }
        double value = (double) bytes / unit;
        String num = formatOneDecimal(value);
        return num + suffix;
    }

    private static String formatOneDecimal(double value) {
        if (value >= 100) {
            return String.format("%.0f", value);
        }
        if (value >= 10) {
            return String.format("%.1f", value);
        }
        if (value >= 1) {
            return String.format("%.1f", value);
        }
        return String.format("%.2f", value);
    }
}
