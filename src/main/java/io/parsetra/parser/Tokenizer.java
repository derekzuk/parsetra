package io.parsetra.parser;

import io.parsetra.util.Validation;

import java.util.ArrayList;
import java.util.List;

/**
 * Tokenizes a string into number+unit pairs (e.g. "2h 30m" or "2h30m" -&gt; ["2","h"], ["30","m"]).
 * Deterministic character-by-character parsing. Thread-safe.
 */
public final class Tokenizer {

    private Tokenizer() {}

    /**
     * Tokenize input into list of (numberPart, unitPart). Strict: no leading/trailing whitespace,
     * only single space allowed between segments.
     */
    public static List<Token> tokenizeStrict(String input, String context) {
        String s = Validation.requireNonEmpty(input, "input");
        if (s != s.trim()) {
            throw new IllegalArgumentException("Strict " + context + ": leading/trailing whitespace not allowed");
        }
        return tokenize(s, false, context);
    }

    /**
     * Tokenize input into list of (numberPart, unitPart). Lenient: trims and allows multiple spaces between segments.
     */
    public static List<Token> tokenizeLenient(String input, String context) {
        String s = Validation.requireNonBlank(input, "input");
        return tokenize(s, true, context);
    }

    private static List<Token> tokenize(String s, boolean lenient, String context) {
        List<Token> tokens = new ArrayList<>();
        int i = 0;
        int len = s.length();
        while (i < len) {
            if (lenient) {
                while (i < len && Character.isWhitespace(s.charAt(i))) i++;
            } else {
                // Strict: allow at most one space between segments (e.g. "2h 30m")
                if (i < len && Character.isWhitespace(s.charAt(i))) {
                    i++;
                    if (i < len && Character.isWhitespace(s.charAt(i))) {
                        throw new IllegalArgumentException("Strict " + context + ": only single space between segments allowed");
                    }
                }
            }
            if (i >= len) break;

            int numStart = i;
            i = readNumber(s, i, context);
            String numberPart = s.substring(numStart, i);
            if (i >= len) {
                throw new IllegalArgumentException("Missing unit after number in " + context + ": '" + numberPart + "'");
            }
            if (lenient) {
                while (i < len && Character.isWhitespace(s.charAt(i))) i++;
            }
            if (i >= len) {
                throw new IllegalArgumentException("Missing unit after number in " + context + ": '" + numberPart + "'");
            }
            int unitStart = i;
            i = readUnit(s, i);
            String unitPart = s.substring(unitStart, i);
            tokens.add(new Token(numberPart, unitPart));
        }
        if (tokens.isEmpty()) {
            throw new IllegalArgumentException("No number+unit segments in " + context + ": '" + s + "'");
        }
        return tokens;
    }

    private static int readNumber(String s, int start, String context) {
        int i = start;
        int len = s.length();
        if (i < len && (s.charAt(i) == '-' || s.charAt(i) == '+')) i++;
        boolean hasDigit = false;
        boolean hasDecimal = false;
        while (i < len) {
            char c = s.charAt(i);
            if (Character.isDigit(c)) {
                hasDigit = true;
                i++;
            } else if (c == '.' && !hasDecimal) {
                hasDecimal = true;
                i++;
            } else if (c == '.' && hasDecimal) {
                throw new IllegalArgumentException("Invalid number in " + context + ": multiple decimals");
            } else {
                break;
            }
        }
        if (!hasDigit) {
            throw new IllegalArgumentException("Invalid number in " + context + ": no digits in '" + s.substring(start, Math.min(start + 20, len)) + "'");
        }
        return i;
    }

    private static int readUnit(String s, int start) {
        int i = start;
        int len = s.length();
        while (i < len && Character.isLetter(s.charAt(i))) {
            i++;
        }
        return i;
    }
}
