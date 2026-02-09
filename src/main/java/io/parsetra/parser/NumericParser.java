package io.parsetra.parser;

import io.parsetra.util.Validation;

/**
 * Parses numeric strings with optional decimal. Strict: no leading/trailing junk, at most one decimal.
 * Thread-safe.
 */
public final class NumericParser {

    private NumericParser() {}

    /**
     * Parses the string as a double. Strict: no leading/trailing whitespace, at most one decimal point.
     * Throws IllegalArgumentException on empty, invalid, or multiple decimals.
     */
    public static double parseDoubleStrict(String s, String context) {
        Validation.requireNonEmpty(s, "number");
        if (s.trim() != s) {
            throw new IllegalArgumentException("Invalid number in " + context + ": leading/trailing whitespace not allowed");
        }
        return parseDoubleInternal(s, context, true);
    }

    /**
     * Parses the string as a double. Lenient: trims first. At most one decimal point.
     */
    public static double parseDoubleLenient(String s, String context) {
        String t = Validation.requireNonBlank(s, "number");
        return parseDoubleInternal(t, context, false);
    }

    private static double parseDoubleInternal(String s, String context, boolean strict) {
        int decimalCount = 0;
        int start = 0;
        int end = s.length();
        if (s.isEmpty()) {
            throw new IllegalArgumentException("Empty number in " + context);
        }
        if (s.charAt(0) == '-' || s.charAt(0) == '+') {
            start = 1;
        }
        if (start >= end) {
            throw new IllegalArgumentException("Invalid number in " + context + ": '" + s + "'");
        }
        for (int i = start; i < end; i++) {
            char c = s.charAt(i);
            if (c == '.') {
                decimalCount++;
                if (decimalCount > 1) {
                    throw new IllegalArgumentException("Invalid number in " + context + ": multiple decimals in '" + s + "'");
                }
            } else if (!Character.isDigit(c)) {
                throw new IllegalArgumentException("Invalid number in " + context + ": '" + s + "'");
            }
        }
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number in " + context + ": '" + s + "'", e);
        }
    }
}
