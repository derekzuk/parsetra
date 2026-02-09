package io.parsetra.duration;

import java.time.Duration;

/**
 * Facade for parsing and formatting human-readable durations.
 * <p>
 * Examples:
 * <pre>
 * Duration d = Durations.parse("2h 30m");
 * Duration d = Durations.parse("1.5d");
 * Duration d = Durations.parseStrict("2h30m");
 * String s = Durations.format(Duration.ofMinutes(150));  // "2h30m"
 * String s = Durations.formatCompact(d);                // "150m"
 * String s = Durations.formatHuman(d);                  // "2h 30m"
 * </pre>
 */
public final class Durations {

    private Durations() {}

    /**
     * Parse a human-readable duration (lenient: trims, allows multiple spaces).
     * Supports multi-segment (2h 30m), compact (2h30m), and fractional (1.5d).
     */
    public static Duration parse(String input) {
        return DurationParser.parse(input);
    }

    /**
     * Parse with strict tokenization: no leading/trailing whitespace, single space between segments.
     */
    public static Duration parseStrict(String input) {
        return DurationParser.parseStrict(input);
    }

    /**
     * Parse with lenient tokenization: trim and allow multiple spaces.
     */
    public static Duration parseLenient(String input) {
        return DurationParser.parseLenient(input);
    }

    /**
     * Format duration as compact string (e.g. "2h30m").
     */
    public static String format(Duration duration) {
        return DurationFormatter.format(duration);
    }

    /**
     * Format as single unit (e.g. "150m").
     */
    public static String formatCompact(Duration duration) {
        return DurationFormatter.formatCompact(duration);
    }

    /**
     * Format with spaces (e.g. "2h 30m").
     */
    public static String formatHuman(Duration duration) {
        return DurationFormatter.formatHuman(duration);
    }
}
