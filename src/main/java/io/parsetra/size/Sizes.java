package io.parsetra.size;

/**
 * Facade for parsing and formatting human-readable data sizes.
 * <p>
 * Examples:
 * <pre>
 * long bytes = Sizes.parse("10MB");
 * long bytes = Sizes.parse("1.5GB");
 * long bytes = Sizes.parse("2GiB");
 * String s = Sizes.format(1536);        // "1.5KB"
 * String s = Sizes.formatBinary(1536);   // "1.5KiB"
 * String s = Sizes.formatBytes(1536);    // "1536B"
 * </pre>
 */
public final class Sizes {

    private Sizes() {}

    /**
     * Parse a human-readable size (lenient). Returns byte count.
     */
    public static long parse(String input) {
        return SizeParser.parse(input);
    }

    /**
     * Parse with strict tokenization.
     */
    public static long parseStrict(String input) {
        return SizeParser.parseStrict(input);
    }

    /**
     * Parse with lenient tokenization (e.g. " 10 mb ").
     */
    public static long parseLenient(String input) {
        return SizeParser.parseLenient(input);
    }

    /**
     * Format byte count using decimal units (e.g. "1.5KB").
     */
    public static String format(long bytes) {
        return SizeFormatter.format(bytes);
    }

    /**
     * Format using binary units (e.g. "1.5KiB").
     */
    public static String formatBinary(long bytes) {
        return SizeFormatter.formatBinary(bytes);
    }

    /**
     * Format as exact bytes (e.g. "1536B").
     */
    public static String formatBytes(long bytes) {
        return SizeFormatter.formatBytes(bytes);
    }
}
