package io.parsetra.util;

/**
 * Input validation helpers. Thread-safe.
 */
public final class Validation {

    private Validation() {}

    /**
     * Throws IllegalArgumentException if the string is null or empty after trim.
     */
    public static String requireNonBlank(String input, String paramName) {
        if (input == null) {
            throw new IllegalArgumentException(paramName + " must not be null");
        }
        String trimmed = input.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException(paramName + " must not be empty");
        }
        return trimmed;
    }

    /**
     * Throws IllegalArgumentException if the string is null or empty (no trim).
     */
    public static String requireNonEmpty(String input, String paramName) {
        if (input == null) {
            throw new IllegalArgumentException(paramName + " must not be null");
        }
        if (input.isEmpty()) {
            throw new IllegalArgumentException(paramName + " must not be empty");
        }
        return input;
    }

    /**
     * Throws IllegalArgumentException if value is negative.
     */
    public static long requireNonNegative(long value, String paramName) {
        if (value < 0) {
            throw new IllegalArgumentException(paramName + " must not be negative: " + value);
        }
        return value;
    }
}
