package io.parsetra.util;

/**
 * Safe arithmetic and conversion helpers. Thread-safe.
 */
public final class MathUtils {

    private MathUtils() {}

    /**
     * Adds two long values with overflow check. Throws IllegalArgumentException on overflow.
     */
    public static long addExactOrThrow(long a, long b, String context) {
        long result = a + b;
        if (((a ^ result) & (b ^ result)) < 0) {
            throw new IllegalArgumentException("Overflow in " + context);
        }
        return result;
    }

    /**
     * Multiplies two long values with overflow check. Throws IllegalArgumentException on overflow.
     */
    public static long multiplyExactOrThrow(long a, long b, String context) {
        long result = a * b;
        if (b != 0 && result / b != a) {
            throw new IllegalArgumentException("Overflow in " + context);
        }
        return result;
    }

    /**
     * Converts a double to long with range check. Rejects NaN, infinity, and values outside long range.
     */
    public static long toLongExact(double value, String context) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new IllegalArgumentException("Invalid number in " + context + ": " + value);
        }
        if (value > Long.MAX_VALUE || value < Long.MIN_VALUE) {
            throw new IllegalArgumentException("Value out of range in " + context + ": " + value);
        }
        return (long) value;
    }

    /**
     * Rounds double to long; throws if result would overflow or value is NaN/Infinite.
     */
    public static long roundToLong(double value, String context) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new IllegalArgumentException("Invalid number in " + context + ": " + value);
        }
        if (value > Long.MAX_VALUE || value < Long.MIN_VALUE) {
            throw new IllegalArgumentException("Value out of range in " + context + ": " + value);
        }
        return Math.round(value);
    }
}
